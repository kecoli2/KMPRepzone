import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "${providers.gradleProperty("APP_NAMESPACE_BASE").get()}." + providers.gradleProperty("APP_NAMESPACE_CORE").get()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    androidLibrary {
        androidResources.enable = true
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
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
            baseName = providers.gradleProperty("APP_NAMESPACE_IOS_CORE").getOrElse("CoreKit")
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
                implementation(libs.compose.runtime)
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
                // Add KMP dependencies here

                //Compose Resources
                implementation(libs.compose.components.resources)
            }
        }
        androidMain {
            dependencies {
                //LIBS IMPORT
                implementation(files("libs/ZSDK_ANDROID_API.jar"))
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }
}

// BuildConfig generation
val uiModuleName: String = providers.gradleProperty("ACTIVE_UI_MODULE").getOrElse("UIModule.NEW")
val appVersion: String = libs.versions.application.versionname.get()
val isDebug: Boolean = providers.gradleProperty("DEBUG").getOrElse("false").toBoolean()
val defaultThemeColor: String = providers.gradleProperty("DEFAULT_THEME").getOrElse("ThemeType.DEFAULT")
val apiEndpoint: String = providers.gradleProperty("API_ENDPOINT").getOrElse("")

val generateBuildConfig = tasks.register("generateBuildConfig") {
    val outputDir = file("src/commonMain/kotlin/com/repzone/core/config")
    val outputFile = File(outputDir, "BuildConfig.kt")
    outputs.file(outputFile)

    inputs.property("uiModuleName", uiModuleName)
    inputs.property("isDebug", isDebug)
    inputs.property("appVersion", appVersion)
    inputs.property("defaultThemeColor", defaultThemeColor)
    inputs.property("apiEndpoint", apiEndpoint)

    doLast {
        outputDir.mkdirs()

        outputFile.writeText("""
            package com.repzone.core.config
            
            /**
             * Otomatik generate
             * ELLE DUZELTMEYINIZ
             *              
             */
            object BuildConfig {
                private val uiModule: UIModule = $uiModuleName
                const val apiEndpoint: String = "$apiEndpoint"                
                const val IS_DEBUG: Boolean = $isDebug
                const val APP_VERSION: String = "$appVersion"
                val THEME_NAME: ThemeType = $defaultThemeColor
                
                val activeUIModule: UIModule
                    get() = uiModule
                
                fun isUIModuleActive(module: UIModule): Boolean {
                    return uiModule == module
                }
            }
            
            enum class UIModule {
                NEW,
                LEGACY
            }
            
            enum class ThemeType{
                DEFAULT,
                RED,
                BLUE,
                YELLOW,
                GREEN,
                PURPLE
            }
        """.trimIndent())
    }
}

// KMP'de doğru task isimleri
tasks.withType<KotlinCompile>().configureEach {
    dependsOn(generateBuildConfig)
}

// Android-specific task varsa
tasks.configureEach {
    if (name.contains("compile") && name.contains("Kotlin")) {
        dependsOn(generateBuildConfig)
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.repzone.core.generated.resources"
    generateResClass = always
}
