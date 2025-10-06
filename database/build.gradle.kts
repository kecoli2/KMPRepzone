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