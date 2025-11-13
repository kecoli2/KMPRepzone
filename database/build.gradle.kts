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
    arg("module", "database")
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