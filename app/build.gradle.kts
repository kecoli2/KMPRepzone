import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)      // google-services.json için
    alias(libs.plugins.firebaseCrashlytics) // Crashlytics Gradle plugin
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Mobile"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)

            //Google Play Service
            implementation(libs.play.services.location)
            implementation(libs.kotlinx.coroutines.play.services)
            implementation(libs.compose.ui.tooling.preview)
          /*  androidLibrary {
                experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
            }*/

        }
        commonMain.dependencies {
            //implementation(project.dependencies.platform(libs.compose.bom))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.ktor.client.core)

            /// Shared Project Implementation
            implementation(projects.core)
            implementation(projects.presentation)
            implementation(projects.presentationLegacy)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(project(":core"))
            implementation(project(":data"))
            implementation(project(":database"))
            implementation(project(":firebase"))
            implementation(project(":network"))
            implementation(project(":presentation"))
            implementation(project(":sync"))
            implementation(project(":domain"))
            implementation(project(":presentation-legacy"))
        }
    }
}

android {
    namespace = providers.gradleProperty("APP_NAMESPACE_APPLICATION_ID").get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = providers.gradleProperty("APP_NAMESPACE_APPLICATION_ID").get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode =  libs.versions.application.version.get().toInt()
        versionName = libs.versions.application.versionname.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {

        }
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.coreUi)
    implementation(projects.database)
    implementation(projects.network)
    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.sync)
    implementation(projects.firebase)

    // UI modülleri - build variant'a göre
    val isDebugMode = providers.gradleProperty("DEBUG").getOrElse("false").toBoolean()
    val activeUiModule = providers.gradleProperty("ACTIVE_UI_MODULE").get()
    if(isDebugMode){
        implementation(projects.presentation)
        implementation(projects.presentationLegacy)
    }else{
        when(activeUiModule){
            "UIModule.LEGACY" -> {
                implementation(projects.presentationLegacy)
            }
            "UIModule.NEW" -> {
                implementation(projects.presentation)
            }
        }
    }
    debugImplementation(compose.uiTooling)
}

//region IOS Gradle Task
tasks.register("assembleXCFramework") {
    group = "build"
    description = "Assembles an XCFramework from all iOS targets."
    dependsOn(
        "linkDebugFrameworkIosArm64",
        "linkDebugFrameworkIosSimulatorArm64"
    )
    doLast {
        val buildDir = buildDir.absolutePath
        val frameworkDirArm64 = "$buildDir/bin/iosArm64/debugFramework/Mobile.framework"
        val frameworkDirSimArm64 = "$buildDir/bin/iosSimulatorArm64/debugFramework/Mobile.framework"
        val outputDir = "$buildDir/xcf"
        exec {
            commandLine(
                "xcodebuild", "-create-xcframework",
                "-framework", frameworkDirArm64,
                "-framework", frameworkDirSimArm64,
                "-output", "$outputDir/Mobile.xcframework"
            )
        }
        println("XCFramework created at $outputDir/Mobile.xcframework")
    }
}
val xcodeFrameworksDir = rootProject.file("../iosApp/Frameworks")

tasks.register<Copy>("copyXCFrameworkToXcode") {
    dependsOn("assembleXCFramework")
    from(buildDir.resolve("XCFrameworks/release"))
    into(xcodeFrameworksDir)
    include("Mobile.xcframework/**")
}
//endregion IOS Gradle Task