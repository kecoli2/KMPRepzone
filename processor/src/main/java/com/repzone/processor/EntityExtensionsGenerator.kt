package com.repzone.database.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.repzone.processor.TableSchema
import java.io.OutputStreamWriter

object EntityExtensionsGenerator {

    fun generate(
        schemas: List<TableSchema>,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        logger.info("EntityExtensionsGenerator: Starting generation for ${schemas.size} schemas")

        schemas.forEach { schema ->
            try {
                logger.info("Generating extensions for: ${schema.tableName}")
                generateExtensionsForEntity(schema, codeGenerator, logger)
            } catch (e: Exception) {
                logger.error("Failed to generate extensions for ${schema.tableName}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun generateExtensionsForEntity(
        schema: TableSchema,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        val entityName = if (schema.tableName.endsWith("Entity")) {
            schema.tableName
        } else {
            "${schema.tableName}Entity"
        }

        val fileName = "${entityName}Extensions"
        val packageName = "com.repzone.database"

        logger.info("Creating file: $packageName.$fileName")

        // Dependencies
        val dependencies = Dependencies(aggregating = false)

        // Kod oluştur
        val code = buildString {
            appendLine("package $packageName")
            appendLine()
            appendLine("// Auto-generated extensions for $entityName")
            appendLine()

            // Metadata object
            appendLine("object ${entityName}Metadata {")
            appendLine("    val tableName: String = \"${schema.tableName}\"")
            append("    val columns: List<String> = listOf(")
            append(schema.columns.joinToString(", ") { "\"${it.name}\"" })
            appendLine(")")
            appendLine("}")
            appendLine()

            // toValuesList function
            appendLine("fun $entityName.toValuesList(): List<Any?> = listOf(")
            schema.columns.forEachIndexed { index, column ->
                val comma = if (index < schema.columns.size - 1) "," else ""
                appendLine("    this.${column.name.replace("`", "")}$comma")
            }
            appendLine(")")
            appendLine()

            // toSqlValuesString function
            appendLine("fun $entityName.toSqlValuesString(): String {")
            appendLine("    return toValuesList()")
            appendLine("        .mapIndexed { index, value ->")
            appendLine("            when {")
            appendLine("                value == null -> \"NULL\"")
            appendLine("                isStringField(index) -> (value as? String)?.quote() ?: \"NULL\"")
            appendLine("                value is Boolean -> if (value) \"1\" else \"0\"")
            appendLine("                else -> value.toString()")
            appendLine("            }")
            appendLine("        }")
            appendLine("        .joinToString(\", \", prefix = \"(\", postfix = \")\")")
            appendLine("}")
            appendLine()

            // isStringField function
            val stringIndices = schema.columns
                .mapIndexedNotNull { index, column ->
                    if (column.type?.uppercase() == "TEXT") index else null
                }

            appendLine("private fun isStringField(index: Int): Boolean {")
            if (stringIndices.isEmpty()) {
                appendLine("    val stringFields = emptySet<Int>()")
            } else {
                appendLine("    val stringFields = setOf(${stringIndices.joinToString(", ")})")
            }
            appendLine("    return index in stringFields")
            appendLine("}")
            appendLine()

            // quote function
            appendLine("private fun String.quote() = \"'\${replace(\"'\", \"''\")}'\"")
        }

        logger.info("Generated code length: ${code.length} characters")

        try {
            val file = codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileName
            )

            // OutputStreamWriter kullan (KSP'nin OutputStream'i)
            OutputStreamWriter(file, Charsets.UTF_8).use { writer ->
                writer.write(code)
                writer.flush()
            }

            logger.info("Successfully written: $fileName.kt (${code.length} bytes)")
        } catch (e: FileAlreadyExistsException) {
            logger.warn("File already exists, skipping: $fileName.kt")
        } catch (e: Exception) {
            logger.error("Failed to write file: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}