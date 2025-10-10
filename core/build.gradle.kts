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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = providers.gradleProperty("APP_NAMESPACE_IOS_CORE").getOrElse("CoreKit")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.kotlin.stdlib)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(compose.components.resources)

        }

        androidMain.dependencies {
            implementation(files("libs/ZSDK_ANDROID_API.jar"))
        }

        iosMain.dependencies {
            // iOS dependencies
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.repzone.core.generated.resources"
    generateResClass = always
}

//region BuildConfig generation
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
            
            import com.repzone.core.enums.*
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
        """.trimIndent())
    }
}

tasks.withType<KotlinCompile>().configureEach {
    dependsOn(generateBuildConfig)
}

tasks.configureEach {
    if (name.contains("compile") && name.contains("Kotlin")) {
        dependsOn(generateBuildConfig)
    }
}
//endregion