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
        namespace = "${providers.gradleProperty("APP_NAMESPACE_BASE").get()}." + providers.gradleProperty("APP_NAMESPACE_PRESENTATION_LEGACY").get()
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = providers.gradleProperty("APP_NAMESPACE_IOS_PRESENTATION_LEGACY").getOrElse("PresentationLegacyKit")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                // Add KMP dependencies here

                // Compose
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)

                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.navigation.compose)
                implementation(libs.kotlinx.serialization.json)
                implementation(compose.components.resources)

                implementation(projects.core)
                implementation(projects.coreUi)
                implementation(projects.domain)
                implementation(projects.firebase)
                implementation(projects.network)
                implementation(projects.sync)
            }
        }

        androidMain {
            dependencies {
                //LIBS IMPORT
                implementation(libs.androidx.activity.compose)

            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}