plugins {
    /*id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)*/
    kotlin("jvm")
}
dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.0")  // Güncelle
    implementation("com.squareup:kotlinpoet:1.18.1")  // Güncelle
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)  // 21'den 17'ye düşür
}
