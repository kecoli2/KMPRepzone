import java.util.Locale

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
    alias(libs.plugins.kotlinSerialization) apply false
}
val tmpDir = if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")) {
    "C:/Temp"
} else {
    "/tmp"
}

allprojects {
    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(
            listOf(
                "-Djava.io.tmpdir=$tmpDir",
                "-Dorg.sqlite.tmpdir=$tmpDir"
            )
        )
    }
    tasks.withType<Test> {
        jvmArgs(
            "-Djava.io.tmpdir=$tmpDir",
            "-Dorg.sqlite.tmpdir=$tmpDir"
        )
    }
}
