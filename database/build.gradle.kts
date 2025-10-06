plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.sqldelight)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "${providers.gradleProperty("APP_NAMESPACE_BASE").get()}." + providers.gradleProperty("APP_NAMESPACE_DATABASE").get()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = providers.gradleProperty("APP_NAMESPACE_IOS_DATABASE").getOrElse("DatabaseKit")
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here
                implementation(libs.sqldelight.runtime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
                dependencies { implementation(libs.sqldelight.androidDriver) }
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
                dependencies { implementation(libs.sqldelight.nativeDriver) }
            }
        }
    }


    tasks.register<GenerateEntityExtensionsTask>("generateEntityExtensions") {
        // SQLDelight generated path
        sourceDir.set(layout.buildDirectory.dir("generated/sqldelight/code/AppDatabase/commonMain/com/repzone/database"))
        outputDir.set(layout.buildDirectory.dir("generated/extensions"))

        // SQLDelight task'ından sonra çalışmalı
        dependsOn("generateCommonMainAppDatabaseInterface")
    }

    kotlin.sourceSets.commonMain {
        kotlin.srcDir(layout.buildDirectory.dir("generated/extensions"))
    }

/*    tasks.named("compileKotlinMetadata") {
        dependsOn("generateEntityExtensions")
    }*/

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
        dependsOn("generateEntityExtensions")
    }
}

tasks.register("generateDomainModels") {
    group = "generation"
    description = "Generate domain models from SQLDelight entities"

    // SQLDelight task'ından sonra çalışsın
    dependsOn("generateCommonMainAppDatabaseInterface")

    doLast {
        val modelPackage = "com.repzone.domain.model"

        // SQLDelight'ın generate ettiği Entity'lerin yolu
        val entityDir = file("build/generated/sqldelight/code/AppDatabase/commonMain/com/repzone/database")

        // Model'lerin yazılacağı klasör (src altında, böylece kalıcı olur)
        val modelDir = file("src/commonMain/kotlin/${modelPackage.replace('.', '/')}")

        if (!entityDir.exists()) {
            println("❌ Entity directory not found: ${entityDir.absolutePath}")
            println("💡 Run SQLDelight generation first!")
            return@doLast
        }

        // Model klasörünü oluştur
        modelDir.mkdirs()

        var generatedCount = 0

        // Tüm Entity dosyalarını bul ve dönüştür
        entityDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" && it.name.endsWith("Entity.kt") }
            .forEach { entityFile ->
                println("📝 Processing: ${entityFile.name}")

                val entityContent = entityFile.readText()
                val modelContent = convertEntityToModel(entityContent, modelPackage)

                // Model dosya adını oluştur
                val modelFileName = entityFile.name.replace("Entity.kt", "Model.kt")
                val modelFile = File(modelDir, modelFileName)

                modelFile.writeText(modelContent)
                println("   ✅ Generated: $modelFileName")
                generatedCount++
            }

        println("\n🎉 Successfully generated $generatedCount model files!")
        println("📂 Location: ${modelDir.absolutePath}")
    }
}

tasks.register("generateEntityMappers") {
    group = "generation"
    description = "Generate entity mappers from SQLDelight entities"

    // SQLDelight generation'dan sonra çalışsın
    dependsOn("generateCommonMainAppDatabaseInterface")

    doLast {
        val mapperPackage = "com.repzone.data.mapper"
        val entityPackage = "com.repzone.database"
        val modelPackage = "com.repzone.domain.model"

        // SQLDelight'ın generate ettiği Entity'lerin yolu
        val entityDir = file("build/generated/sqldelight/code/AppDatabase/commonMain/com/repzone/database")

        // Mapper'ların yazılacağı klasör (data modülü altında)
        val mapperDir = file("data/src/commonMain/kotlin/${mapperPackage.replace('.', '/')}")

        if (!entityDir.exists()) {
            println("❌ Entity directory not found: ${entityDir.absolutePath}")
            println("💡 Run SQLDelight generation first!")
            return@doLast
        }

        // Mapper klasörünü oluştur
        mapperDir.mkdirs()

        var generatedCount = 0

        // Tüm Entity dosyalarını bul ve mapper oluştur
        entityDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" && it.name.endsWith("Entity.kt") }
            .forEach { entityFile ->
                println("📝 Processing: ${entityFile.name}")

                // Entity içeriğini oku ve field'ları parse et
                val entityContent = entityFile.readText()
                val entityFields = parseEntityFields(entityContent)

                // Entity ve Model isimlerini belirle
                val entityName = entityFile.nameWithoutExtension // Örn: SyncProductEntity
                val modelName = entityName.replace("Entity", "Model") // Örn: SyncProductModel
                val mapperName = entityName.replace("Entity", "EntityDbMapper") // Örn: SyncProductEntityDbMapper

                // Mapper içeriğini oluştur
                val mapperContent = generateMapper(
                    mapperName = mapperName,
                    entityName = entityName,
                    modelName = modelName,
                    entityFields = entityFields,
                    mapperPackage = mapperPackage,
                    entityPackage = entityPackage,
                    modelPackage = modelPackage
                )

                // Mapper dosyasını yaz
                val mapperFileName = "$mapperName.kt"
                val mapperFile = File(mapperDir, mapperFileName)

                mapperFile.writeText(mapperContent)
                println("   ✅ Generated: $mapperFileName")
                generatedCount++
            }

        println("\n🎉 Successfully generated $generatedCount mapper files!")
        println("📂 Location: ${mapperDir.absolutePath}")
    }
}

tasks.register("generateMapperDI") {
    group = "generation"
    description = "Generate Koin DI definitions for entity mappers"

    // SQLDelight generation'dan sonra çalışsın
    dependsOn("generateCommonMainAppDatabaseInterface")

    doLast {
        val diPackage = "com.repzone.di"
        val mapperPackage = "com.repzone.data.mapper"
        val entityPackage = "com.repzone.database"
        val modelPackage = "com.repzone.domain.model"

        // SQLDelight'ın generate ettiği Entity'lerin yolu
        val entityDir = file("build/generated/sqldelight/code/AppDatabase/commonMain/com/repzone/database")

        // DI dosyasının yazılacağı klasör (data modülü altında)
        val diDir = file("data/src/commonMain/kotlin/${diPackage.replace('.', '/')}")

        if (!entityDir.exists()) {
            println("❌ Entity directory not found: ${entityDir.absolutePath}")
            println("💡 Run SQLDelight generation first!")
            return@doLast
        }

        // DI klasörünü oluştur
        diDir.mkdirs()

        val diDefinitions = mutableListOf<DIDefinition>()

        // Tüm Entity dosyalarını bul ve DI definition oluştur
        entityDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" && it.name.endsWith("Entity.kt") }
            .forEach { entityFile ->
                val entityName = entityFile.nameWithoutExtension // Örn: SyncProductEntity
                val modelName = entityName.replace("Entity", "Model")
                val mapperName = entityName.replace("Entity", "EntityDbMapper")

                // Region adı için base name al (Sync prefix'ini kaldır)
                val regionName = entityName.removePrefix("Sync").removeSuffix("Entity")

                diDefinitions.add(
                    DIDefinition(
                        regionName = regionName,
                        mapperName = mapperName,
                        entityName = entityName,
                        modelName = modelName
                    )
                )
            }

        // DI dosyasını oluştur
        val diContent = generateDIFile(
            diDefinitions = diDefinitions,
            diPackage = diPackage,
            mapperPackage = mapperPackage,
            entityPackage = entityPackage,
            modelPackage = modelPackage
        )

        val diFile = File(diDir, "MapperModule.kt")
        diFile.writeText(diContent)

        println("✅ Generated: MapperModule.kt with ${diDefinitions.size} definitions")
        println("📂 Location: ${diFile.absolutePath}")
    }
}


sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.repzone.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/schema"))
            migrationOutputDirectory.set(file("src/commonMain/sqldelight/migrations"))
        }
    }
}

//region GENERATOR EXTENSIONS FOR SQLDELIGHT
abstract class GenerateEntityExtensionsTask : DefaultTask() {
    @get:InputDirectory
    abstract val sourceDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val sourceDirFile = sourceDir.asFile.get()
        val outputDirFile = outputDir.asFile.get()

        outputDirFile.mkdirs()

        // SQLDelight generated entity'leri bul
        sourceDirFile.walkTopDown()
            .filter { it.isFile && it.name.endsWith(".kt") && it.name.contains("Entity") }
            .forEach { file ->
                generateExtension(file, outputDirFile)
            }
    }
    private fun generateExtension(entityFile: File, outputDir: File) {
        val content = entityFile.readText()

        if (!content.contains("data class")) return

        val className = entityFile.nameWithoutExtension
        val packageName = content.substringAfter("package ").substringBefore("\n").trim()

        // Field'ları tip bilgisi ile parse et
        val fieldPattern = """val (\w+):\s*([^,\)]+?)(?:\s*[,\)]|$)""".toRegex()
        val fields = fieldPattern.findAll(content).map { matchResult ->
            val name = matchResult.groupValues[1]
            val type = matchResult.groupValues[2].trim()
            name to type
        }.toList()

        if (fields.isEmpty()) return

        // String field indexlerini bul
        val stringFieldIndices = fields.mapIndexedNotNull { idx, (_, type) ->
            if (type.contains("String")) idx else null
        }

        val extensionContent = """
        package $packageName
        
        // Auto-generated metadata for $className
        
        object ${className}Metadata {
            val tableName: String = "${className.replace("Metadata", "")}"
            val columns: List<String> = listOf(${fields.joinToString { "\"${it.first}\"" }})
        }
        
        fun $className.toValuesList(): List<Any?> = listOf(
            ${fields.joinToString(",\n            ") { "this.${it.first}" }}
        )
        
        fun $className.toSqlValuesString(): String {
            return toValuesList()
                .mapIndexed { index, value ->
                    when {
                        value == null -> "NULL"
                        ${if (stringFieldIndices.isNotEmpty()) "isStringField(index) -> (value as? String)?.quote() ?: \"NULL\"" else ""}
                        value is Boolean -> if (value) "1" else "0"
                        else -> value.toString()
                    }
                }
                .joinToString(", ", prefix = "(", postfix = ")")
        }
        
        ${if (stringFieldIndices.isNotEmpty()) """
        private fun isStringField(index: Int): Boolean {
            val stringFields = setOf(${stringFieldIndices.joinToString()})
            return index in stringFields
        }
        
        private fun String.quote() = "'${'$'}{replace("'", "''")}'"
        """ else ""}
    """.trimIndent()

        File(outputDir, "${className}Extensions.kt").writeText(extensionContent)
    }
}
//endregion

//region GENERATOR MODEL CLASS
fun convertEntityToModel(entityContent: String, modelPackage: String): String {
    val lines = entityContent.lines()
    val result = StringBuilder()

    // Package declaration
    result.appendLine("package $modelPackage")
    result.appendLine()

    var inDataClass = false

    for (line in lines) {
        when {
            line.trim().startsWith("public data class") || line.trim().startsWith("data class") -> {
                inDataClass = true

                // Class adını değiştir
                val transformedLine = line
                    .replace("public data class", "data class")
                    .replace("Entity(", "Model(")

                result.appendLine(transformedLine)
            }
            inDataClass && (line.trim().startsWith("public val") || line.trim().startsWith("val")) -> {
                // Field adının ilk harfini küçült
                val fieldMatch = Regex("(?:public )?val ([A-Z][a-zA-Z0-9]*):").find(line)

                if (fieldMatch != null) {
                    val originalFieldName = fieldMatch.groupValues[1]
                    val newFieldName = originalFieldName.replaceFirstChar { it.lowercase() }

                    val transformedLine = line
                        .replace("public val", "  val")
                        .replace("val", "  val")
                        .replace("val $originalFieldName:", "val $newFieldName:")
                        .trimStart()
                        .prependIndent("  ")

                    result.appendLine(transformedLine)
                } else {
                    result.appendLine(line.replace("public val", "  val"))
                }
            }
            line.trim() == ")" -> {
                result.appendLine(line)
                inDataClass = false
            }
        }
    }

    return result.toString()
}
//endregion

//region GENERATOR MAPPER
data class EntityField(
    val name: String,        // Örn: Id
    val modelName: String,   // Örn: id
    val type: String,        // Örn: Long
    val isNullable: Boolean  // Örn: true
)

fun parseEntityFields(entityContent: String): List<EntityField> {
    val fields = mutableListOf<EntityField>()
    val lines = entityContent.lines()

    var inDataClass = false

    for (line in lines) {
        when {
            line.trim().startsWith("public data class") || line.trim().startsWith("data class") -> {
                inDataClass = true
            }
            inDataClass && (line.trim().startsWith("public val") || line.trim().startsWith("val")) -> {
                // Field parse et: public val Id: Long?,
                val fieldMatch = Regex("(?:public )?val ([A-Z][a-zA-Z0-9]*): ([^,)]+)").find(line)

                if (fieldMatch != null) {
                    val fieldName = fieldMatch.groupValues[1]
                    val fieldType = fieldMatch.groupValues[2].trim()
                    val isNullable = fieldType.endsWith("?")

                    fields.add(
                        EntityField(
                            name = fieldName,
                            modelName = fieldName.replaceFirstChar { it.lowercase() },
                            type = fieldType,
                            isNullable = isNullable
                        )
                    )
                }
            }
            line.trim() == ")" -> {
                inDataClass = false
                break
            }
        }
    }

    return fields
}

fun generateMapper(
    mapperName: String,
    entityName: String,
    modelName: String,
    entityFields: List<EntityField>,
    mapperPackage: String,
    entityPackage: String,
    modelPackage: String
): String {
    val result = StringBuilder()

    // Package ve imports
    result.appendLine("package $mapperPackage")
    result.appendLine()
    result.appendLine("import com.repzone.data.util.Mapper")
    result.appendLine("import $entityPackage.$entityName")
    result.appendLine("import $modelPackage.$modelName")
    result.appendLine()

    // Class declaration
    result.appendLine("class $mapperName : Mapper<$entityName, $modelName> {")
    result.appendLine("    //region Field")
    result.appendLine("    //endregion")
    result.appendLine()
    result.appendLine("    //region Properties")
    result.appendLine("    //endregion")
    result.appendLine()
    result.appendLine("    //region Constructor")
    result.appendLine("    //endregion")
    result.appendLine()
    result.appendLine("    //region Public Method")

    // toDomain metodu
    result.appendLine("    override fun toDomain(from: $entityName): $modelName {")
    result.appendLine("        return $modelName(")

    entityFields.forEachIndexed { index, field ->
        val mapping = generateFieldMapping(field, isFromEntity = true)
        val comma = if (index < entityFields.size - 1) "," else ""
        result.appendLine("            ${field.modelName} = $mapping$comma")
    }

    result.appendLine("        )")
    result.appendLine("    }")
    result.appendLine()

    // fromDomain metodu
    result.appendLine("    override fun fromDomain(domain: $modelName): $entityName {")
    result.appendLine("        return $entityName(")

    entityFields.forEachIndexed { index, field ->
        val mapping = generateFieldMapping(field, isFromEntity = false)
        val comma = if (index < entityFields.size - 1) "," else ""
        result.appendLine("            ${field.name} = $mapping$comma")
    }

    result.appendLine("        )")
    result.appendLine("    }")

    result.appendLine("    //endregion")
    result.appendLine()
    result.appendLine("    //region Protected Method")
    result.appendLine("    //endregion")
    result.appendLine()
    result.appendLine("    //region Private Method")
    result.appendLine("    //endregion")
    result.appendLine("}")

    return result.toString()
}

fun generateFieldMapping(field: EntityField, isFromEntity: Boolean): String {
    val baseType = field.type.removeSuffix("?")
    val source = if (isFromEntity) "from.${field.name}" else "domain.${field.modelName}"

    return when {
        // Boolean mapping (Long <-> Boolean)
        baseType == "Long" && (field.name.startsWith("Is") ||
                field.name.startsWith("Close") ||
                field.name.contains("Visible")) -> {
            if (isFromEntity) {
                if (field.isNullable) {
                    "$source?.let { it != 0L }"
                } else {
                    "$source != 0L"
                }
            } else {
                if (field.isNullable) {
                    "$source?.let { if (it) 1L else 0L }"
                } else {
                    "$source.let { if (it) 1L else 0L }"
                }
            }
        }
        // Normal mapping
        else -> source
    }
}
//endregion

//region GENERATOR DI MAPPER
data class DIDefinition(
    val regionName: String,      // Örn: Product
    val mapperName: String,      // Örn: SyncProductEntityDbMapper
    val entityName: String,      // Örn: SyncProductEntity
    val modelName: String        // Örn: SyncProductModel
)

fun generateDIFile(
    diDefinitions: List<DIDefinition>,
    diPackage: String,
    mapperPackage: String,
    entityPackage: String,
    modelPackage: String
): String {
    val result = StringBuilder()

    // Package declaration
    result.appendLine("package $diPackage")
    result.appendLine()

    // Imports
    result.appendLine("import com.repzone.data.util.Mapper")
    result.appendLine("import org.koin.core.qualifier.named")
    result.appendLine("import org.koin.dsl.module")
    result.appendLine()

    // Import all mappers
    diDefinitions.forEach { def ->
        result.appendLine("import $mapperPackage.${def.mapperName}")
    }
    result.appendLine()

    // Import all entities
    diDefinitions.forEach { def ->
        result.appendLine("import $entityPackage.${def.entityName}")
    }
    result.appendLine()

    // Import all models
    diDefinitions.forEach { def ->
        result.appendLine("import $modelPackage.${def.modelName}")
    }
    result.appendLine()

    // Module definition
    result.appendLine("val mapperModule = module {")
    result.appendLine()

    // Her mapper için region block
    diDefinitions.sortedBy { it.regionName }.forEach { def ->
        result.appendLine("    //region ${def.regionName}")
        result.appendLine("    single<Mapper<${def.entityName}, ${def.modelName}>>(named(\"${def.mapperName}\")) { ${def.mapperName}() }")
        result.appendLine("    //endregion")
        result.appendLine()
    }

    result.appendLine("}")

    return result.toString()
}
//endregion