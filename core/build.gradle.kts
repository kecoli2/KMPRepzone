import com.google.devtools.ksp.gradle.KspAATask
import org.jetbrains.compose.resources.ResourcesExtension.ResourceClassGeneration
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
}

ksp {
    arg("projectRoot", rootProject.projectDir.absolutePath)
    arg("module", "core")
    arg("incremental", "false")
    arg("uiModuleName", providers.gradleProperty("ACTIVE_UI_MODULE").getOrElse("UIModule.NEW"))
    arg("appVersion", libs.versions.application.versionname.get())
    arg("isDebug", providers.gradleProperty("DEBUG").getOrElse("false"))
    arg("defaultThemeColor", providers.gradleProperty("DEFAULT_THEME").getOrElse("ThemeType.DEFAULT"))
    arg("apiEndpoint", providers.gradleProperty("API_ENDPOINT").getOrElse(""))
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
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
        commonMain {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.koin.core)
                implementation(compose.components.resources)
                implementation(compose.ui)
                implementation(libs.bignum)
            }
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }

        androidMain.dependencies {
            implementation(files("libs/ZSDK_ANDROID_API.jar"))
        }

        iosMain.dependencies {

        }
    }
}

android {
    namespace = "${providers.gradleProperty("APP_NAMESPACE_BASE").get()}." + providers.gradleProperty("APP_NAMESPACE_CORE").get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

compose {
    resources.publicResClass = true
    resources.generateResClass = ResourceClassGeneration.Always
}

dependencies {
    add("kspCommonMainMetadata", project(":processor"))
}

tasks.configureEach {
    if (name.startsWith("compile") && name.contains("Kotlin")) {
        tasks.findByName("kspCommonMainKotlinMetadata")?.let { kspTask ->
            dependsOn(kspTask)
        }
    }
}

afterEvaluate {
    tasks.withType<KotlinCompile>().configureEach {
        tasks.findByName("kspCommonMainKotlinMetadata")?.let { kspTask ->
            dependsOn(kspTask)
        }
    }
}