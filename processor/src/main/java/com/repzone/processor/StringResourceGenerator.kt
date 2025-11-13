package com.repzone.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class StringResourceGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // Core modülündeki strings.xml dosyasını bul
        val currentModule = options["module"] ?: ""

        if (currentModule != "core") {
            logger.info("StringResourceGenerator skipped for module: $currentModule")
            return emptyList()
        }

        val projectRoot = options["projectRoot"] ?: run {
            logger.error("projectRoot option not provided")
            return emptyList()
        }

        val stringsXmlPath = "$projectRoot/core/src/commonMain/composeResources/values-tr/strings.xml"
        val stringsFile = File(stringsXmlPath)

        if (!stringsFile.exists()) {
            logger.warn("strings.xml not found at: $stringsXmlPath")
            return emptyList()
        }

        logger.info("Found strings.xml at: $stringsXmlPath")

        // XML'i parse et
        val stringResources = parseStringsXml(stringsFile)

        logger.info("Parsed ${stringResources.size} string resources")

        // Enum dosyasını oluştur
        generateStringResourceEnum(stringResources)

        // Mapping dosyasını oluştur
        generateResourceMapping(stringResources)

        return emptyList()
    }

    private fun parseStringsXml(file: File): List<StringResourceInfo> {
        val resources = mutableListOf<StringResourceInfo>()

        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(file)
            doc.documentElement.normalize()

            val stringNodes = doc.getElementsByTagName("string")

            for (i in 0 until stringNodes.length) {
                val element = stringNodes.item(i) as Element
                val name = element.getAttribute("name")
                val value = element.textContent

                // %s, %d gibi format argümanlarını tespit et
                val formatArgs = detectFormatArgs(value)

                resources.add(
                    StringResourceInfo(
                        name = name,
                        hasArgs = formatArgs.isNotEmpty(),
                        argCount = formatArgs.size,
                        argTypes = formatArgs
                    )
                )
            }
        } catch (e: Exception) {
            logger.error("Error parsing strings.xml: ${e.message}")
        }

        return resources
    }

    private fun detectFormatArgs(value: String): List<FormatArgType> {
        val args = mutableListOf<FormatArgType>()
        val regex = """%(\d+\$)?[sd]""".toRegex()

        regex.findAll(value).forEach { match ->
            val type = when {
                match.value.endsWith("s") -> FormatArgType.STRING
                match.value.endsWith("d") -> FormatArgType.INT
                else -> FormatArgType.ANY
            }
            args.add(type)
        }

        return args
    }

// processor/src/main/kotlin/com/repzone/processor/StringResourceGenerator.kt

    private fun generateStringResourceEnum(resources: List<StringResourceInfo>) {
        val packageName = "com.repzone.core.model"

        try {
            logger.warn("Creating StringResource.kt...")

            val file = codeGenerator.createNewFile(
                Dependencies(false),
                packageName,
                "StringResource"
            )

            logger.warn("Writing StringResource.kt...")

            file.bufferedWriter().use { writer ->
                // Başlangıç - indentation yok
                writer.write("package $packageName\n\n")

                writer.write("/**\n")
                writer.write(" * Auto-generated from strings.xml\n")
                writer.write(" * Do not modify this file manually\n")
                writer.write(" * Generated at: ${java.time.LocalDateTime.now()}\n")
                writer.write(" * Total entries: ${resources.size}\n")
                writer.write(" */\n")

                writer.write("enum class StringResource(\n")
                writer.write("    val hasArgs: Boolean = false,\n")
                writer.write("    val argCount: Int = 0\n")
                writer.write(") {\n")

                // Enum değerleri - her biri 4 space indent
                resources.forEachIndexed { index, resource ->
                    val enumName = resource.name.toScreamingSnakeCase()
                    val line = if (resource.hasArgs) {
                        "    $enumName(hasArgs = true, argCount = ${resource.argCount})"
                    } else {
                        "    $enumName"
                    }

                    // Son elemandan sonra virgül yok
                    if (index < resources.size - 1) {
                        writer.write("$line,\n")
                    } else {
                        writer.write("$line;\n")
                    }
                }

                writer.write("\n")
                writer.write("    companion object {\n")
                writer.write("        fun fromKey(key: String): StringResource? {\n")
                writer.write("            return entries.find {\n")
                writer.write("                it.name.equals(key.replace(\".\", \"_\").replace(\"-\", \"_\"), ignoreCase = true)\n")
                writer.write("            }\n")
                writer.write("        }\n")
                writer.write("    }\n")
                writer.write("}\n")
            }

            logger.warn("✅ Generated StringResource.kt successfully")
        } catch (e: FileAlreadyExistsException) {
            logger.warn("⚠️ StringResource.kt already exists, skipping generation")
        } catch (e: Exception) {
            logger.error("❌ Failed to generate StringResource.kt: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun generateResourceMapping(resources: List<StringResourceInfo>) {
        val packageName = "com.repzone.core.util"

        try {
            logger.warn("Creating StringResourceMapping.kt...")

            val file = codeGenerator.createNewFile(
                Dependencies(false),
                packageName,
                "StringResourceMapping"
            )

            logger.warn("Writing StringResourceMapping.kt...")

            file.bufferedWriter().use { writer ->
                // Başlangıç - indentation yok
                writer.write("package $packageName\n\n")

                writer.write("import com.repzone.core.model.StringResource\n")
                writer.write("import org.jetbrains.compose.resources.StringResource as ComposeStringResource\n")
                writer.write("import repzonemobile.core.generated.resources.Res\n\n")
                writer.write("import repzonemobile.core.generated.resources.*\n\n")

                writer.write("/**\n")
                writer.write(" * Auto-generated mapping from domain StringResource to Compose resources\n")
                writer.write(" * Do not modify this file manually\n")
                writer.write(" * Generated at: ${java.time.LocalDateTime.now()}\n")
                writer.write(" */\n")

                writer.write("fun StringResource.toComposeResource(): ComposeStringResource {\n")
                writer.write("    return when (this) {\n")

                // When branches - 8 space indent
                resources.forEach { resource ->
                    val enumName = resource.name.toScreamingSnakeCase()
                    val resourceName = resource.name.replace("-", "_").replace(".", "_")
                    writer.write("        StringResource.$enumName -> Res.string.$resourceName\n")
                }

                writer.write("    }\n")
                writer.write("}\n")
            }

            logger.warn("✅ Generated StringResourceMapping.kt successfully")
        } catch (e: FileAlreadyExistsException) {
            logger.warn("⚠️ StringResourceMapping.kt already exists, skipping generation")
        } catch (e: Exception) {
            logger.error("❌ Failed to generate StringResourceMapping.kt: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun String.toScreamingSnakeCase(): String {
        return this
            .replace(Regex("([a-z])([A-Z])"), "$1_$2")
            .replace(".", "_")
            .replace("-", "_")
            .uppercase()
    }
}

data class StringResourceInfo(
    val name: String,
    val hasArgs: Boolean,
    val argCount: Int,
    val argTypes: List<FormatArgType>
)

enum class FormatArgType {
    STRING, INT, ANY
}

class StringResourceGeneratorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return StringResourceGenerator(
            environment.codeGenerator,
            environment.logger,
            environment.options
        )
    }
}