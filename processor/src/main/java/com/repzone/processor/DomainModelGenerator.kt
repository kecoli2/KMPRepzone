package com.repzone.database.processor

import com.google.devtools.ksp.processing.KSPLogger
import com.repzone.processor.TableSchema
import java.io.File

object DomainModelGenerator {

    fun generate(
        schemas: List<TableSchema>,
        logger: KSPLogger
    ) {
        logger.info("DomainModelGenerator: Starting generation for ${schemas.size} schemas")

        schemas.forEach { schema ->
            try {
                logger.info("Generating model for: ${schema.tableName}")
                generateModelForEntity(schema, logger)
            } catch (e: Exception) {
                logger.error("Failed to generate model for ${schema.tableName}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun generateModelForEntity(
        schema: TableSchema,
        logger: KSPLogger
    ) {
        val entityName = if (schema.tableName.endsWith("Entity")) {
            schema.tableName
        } else {
            "${schema.tableName}Entity"
        }

        val modelName = entityName.replace("Entity", "Model")
        val modelPackage = "com.repzone.domain.model"

        logger.info("Model: $modelName, Entity: $entityName")

        // Target path in domain module
        val modelDir = File("domain/src/commonMain/kotlin/${modelPackage.replace('.', '/')}")
        val modelFile = File(modelDir, "$modelName.kt")

        // Check if model already exists
        if (modelFile.exists()) {
            logger.info("Model already exists, skipping: $modelName at ${modelFile.absolutePath}")
            return
        }

        // Create directory if not exists
        if (!modelDir.exists()) {
            modelDir.mkdirs()
            logger.info("Created model directory: ${modelDir.absolutePath}")
        }

        val code = generateModelCode(
            modelName = modelName,
            schema = schema,
            modelPackage = modelPackage
        )

        logger.info("Generated model code length: ${code.length} characters")

        try {
            modelFile.writeText(code)
            logger.info("Successfully generated model: ${modelFile.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to write model file: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun generateModelCode(
        modelName: String,
        schema: TableSchema,
        modelPackage: String
    ): String = buildString {
        appendLine("package $modelPackage")
        appendLine()
        appendLine("data class $modelName(")

        schema.columns.forEachIndexed { index, column ->
            val comma = if (index < schema.columns.size - 1) "," else ""

            // Backtick'leri temizle ve camelCase'e çevir
            val fieldName = column.name.replace("`", "").toCamelCase()

            // Kotlin type
            val kotlinType = mapSqlTypeToKotlinType(column.type)
            val nullableMarker = if (!column.isNotNull) "?" else ""

            appendLine("    val $fieldName: $kotlinType$nullableMarker$comma")
        }

        appendLine(")")
    }

    private fun mapSqlTypeToKotlinType(sqlType: String): String {
        return when (sqlType.uppercase()) {
            "INTEGER" -> "Long"
            "REAL" -> "Double"
            "TEXT" -> "String"
            "BLOB" -> "ByteArray"
            else -> "String"
        }
    }

    // PascalCase -> camelCase
    // Key -> key
    // CampaignMasterResultId -> campaignMasterResultId
    private fun String.toCamelCase(): String {
        if (this.isEmpty()) return this
        return this.first().lowercase() + this.substring(1)
    }
}