import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = providers.gradleProperty("APP_NAMESPACE_IOS_SYNC").getOrElse("SyncKit")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.koin.core)
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.serialization.json)

                // Project Dependcy
                implementation(projects.core)
                implementation(projects.data)
                implementation(projects.domain)
                implementation(projects.database)
                implementation(projects.network)
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
    namespace = "${providers.gradleProperty("APP_NAMESPACE_BASE").get()}." + providers.gradleProperty("APP_NAMESPACE_SYNC").get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}