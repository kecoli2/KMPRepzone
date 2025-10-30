import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.PathSensitivity

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.ksp)
}

// KSP configuration
ksp {
    // SQLDelight dosyalarının yolu
    arg("sqldelight.path", "${projectDir}/src/commonMain/sqldelight")

    // Incremental processing'i kapat - her zaman full build
    arg("incremental", "false")
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = providers.gradleProperty("APP_NAMESPACE_IOS_DATABASE").getOrElse("DatabaseKit")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                // Add KMP dependencies here
                implementation(libs.sqldelight.runtime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)

                //Project Dependcy
                implementation(projects.core)
                kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                dependencies { implementation(libs.sqldelight.androidDriver) }
            }
        }

        iosMain {
            dependencies {
                dependencies { implementation(libs.sqldelight.nativeDriver) }
            }
        }
    }
}

// Android target için JVM 21 ayarı
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "21"
    }
}

android {
    namespace = "${providers.gradleProperty("APP_NAMESPACE_BASE").get()}." + providers.gradleProperty("APP_NAMESPACE_DATABASE").get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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

dependencies {
    // KSP processor'ı ekle
    add("kspCommonMainMetadata", project(":processor"))
}

afterEvaluate {
    // Tüm Kotlin compile task'larını bul ve KSP'ye bağla
    tasks.matching { task ->
        task.name.startsWith("compile") &&
                task.name.contains("Kotlin") &&
                !task.name.contains("Metadata")  // Metadata hariç
    }.configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.configureEach {
    if (name.startsWith("ksp") && name.contains("Metadata")) {
        dependsOn("generateCommonMainAppDatabaseInterface")
        outputs.upToDateWhen { false }
        inputs.dir("${layout.buildDirectory.get()}/generated/sqldelight/code/AppDatabase/commonMain")
            .withPathSensitivity(PathSensitivity.RELATIVE)
            .optional()
    }
}
//region REGISTER TASKS
/*tasks.withType<KotlinCompile>().configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}*/

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
            return@doLast
        }

        // Model klasörünü oluştur
        modelDir.mkdirs()

        // Tüm Entity dosyalarını bul ve dönüştür
        entityDir.walkTopDown()
            .filter { it.isFile && it.extension == "kt" && it.name.endsWith("Entity.kt") }
            .forEach { entityFile ->
                val entityContent = entityFile.readText()
                val modelContent = convertEntityToModel(entityContent, modelPackage)

                // Model dosya adını oluştur
                val modelFileName = entityFile.name.replace("Entity.kt", "Model.kt")
                val modelFile = File(modelDir, modelFileName)

                modelFile.writeText(modelContent)
            }
    }
}
//endregion REGISTER TASKS

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