// processor/src/main/kotlin/com/repzone/processor/BuildConfigGenerator.kt
package com.repzone.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*

class BuildConfigGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val currentModule = options["module"] ?: ""

        if (currentModule != "core") {
            return emptyList()
        }

        logger.warn("BuildConfigGenerator running for core module")

        // Options'dan değerleri al
        val uiModuleName = options["uiModuleName"] ?: "UIModule.NEW"
        val isDebug = options["isDebug"]?.toBoolean() ?: false
        val appVersion = options["appVersion"] ?: "1.0.0"
        val defaultThemeColor = options["defaultThemeColor"] ?: "ThemeType.DEFAULT"
        val apiEndpoint = options["apiEndpoint"] ?: ""

        generateBuildConfig(uiModuleName, isDebug, appVersion, defaultThemeColor, apiEndpoint)

        return emptyList()
    }

    private fun generateBuildConfig(
        uiModuleName: String,
        isDebug: Boolean,
        appVersion: String,
        defaultThemeColor: String,
        apiEndpoint: String
    ) {
        val packageName = "com.repzone.core.config"

        try {
            logger.warn("Creating BuildConfig.kt...")

            val file = codeGenerator.createNewFile(
                Dependencies(false),
                packageName,
                "BuildConfig"
            )

            file.bufferedWriter().use { writer ->
                writer.write("""
                    package $packageName
                    
                    import com.repzone.core.enums.*
                    
                    /**
                     * Auto-generated BuildConfig
                     * Do not modify this file manually
                     * Generated at: ${java.time.LocalDateTime.now()}
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
                """.trimIndent())
            }

            logger.warn("Generated BuildConfig.kt successfully")
        } catch (e: FileAlreadyExistsException) {
            logger.warn("BuildConfig.kt already exists, skipping generation")
        } catch (e: Exception) {
            logger.error("Failed to generate BuildConfig.kt: ${e.message}")
            e.printStackTrace()
        }
    }
}

class BuildConfigGeneratorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return BuildConfigGenerator(
            environment.codeGenerator,
            environment.logger,
            environment.options
        )
    }
}