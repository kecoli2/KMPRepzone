package com.repzone.database.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.repzone.processor.TableSchema
import java.io.File

object EntityMapperGenerator {

    data class EntityField(
        val name: String,
        val type: String,
        val isNullable: Boolean
    )

    fun generate(
        schemas: List<TableSchema>,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        logger.info("EntityMapperGenerator: Starting generation for ${schemas.size} schemas")

        schemas.forEach { schema ->
            try {
                logger.info("Generating mapper for: ${schema.tableName}")
                generateMapperForEntity(schema, logger)
            } catch (e: Exception) {
                logger.error("Failed to generate mapper for ${schema.tableName}: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun generateMapperForEntity(
        schema: TableSchema,
        logger: KSPLogger
    ) {
        val entityName = if (schema.tableName.endsWith("Entity")) {
            schema.tableName
        } else {
            "${schema.tableName}Entity"
        }

        val modelName = entityName.replace("Entity", "Model")
        val mapperName = entityName.replace("Entity", "EntityDbMapper")

        val mapperPackage = "com.repzone.data.mapper"
        val entityPackage = "com.repzone.database"
        val modelPackage = "com.repzone.domain.model"

        logger.info("Mapper: $mapperName, Entity: $entityName, Model: $modelName")

        // Target path in data module
        val mapperDir = File("data/src/commonMain/kotlin/${mapperPackage.replace('.', '/')}")
        val mapperFile = File(mapperDir, "$mapperName.kt")

        // Check if mapper already exists
        if (mapperFile.exists()) {
            logger.info("Mapper already exists, skipping: $mapperName at ${mapperFile.absolutePath}")
            return
        }

        // Create directory if not exists
        if (!mapperDir.exists()) {
            mapperDir.mkdirs()
            logger.info("Created mapper directory: ${mapperDir.absolutePath}")
        }

        // Entity fields'ları schema'dan al
        val entityFields = schema.columns.map { column ->
            EntityField(
                name = column.name,
                type = mapSqlTypeToKotlinType(column.type ?: "TEXT"),
                isNullable = !column.isNotNull
            )
        }

        val code = generateMapperCode(
            mapperName = mapperName,
            entityName = entityName,
            modelName = modelName,
            entityFields = entityFields,
            mapperPackage = mapperPackage,
            entityPackage = entityPackage,
            modelPackage = modelPackage
        )

        logger.info("Generated mapper code length: ${code.length} characters")

        try {
            // Write to data module directly
            mapperFile.writeText(code)
            logger.info("Successfully generated mapper: ${mapperFile.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to write mapper file: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun generateMapperCode(
        mapperName: String,
        entityName: String,
        modelName: String,
        entityFields: List<EntityField>,
        mapperPackage: String,
        entityPackage: String,
        modelPackage: String
    ): String = buildString {
        appendLine("package $mapperPackage")
        appendLine()
        appendLine("import $entityPackage.$entityName")
        appendLine("import $modelPackage.$modelName")
        appendLine()
        appendLine("/**")
        appendLine(" * Auto-generated mapper for $entityName")
        appendLine(" * Maps between Entity (database) and Model (domain)")
        appendLine(" */")
        appendLine("object $mapperName {")
        appendLine()

        // toModel function
        appendLine("    /**")
        appendLine("     * Convert Entity to Model")
        appendLine("     */")
        appendLine("    fun $entityName.toModel(): $modelName {")
        appendLine("        return $modelName(")
        entityFields.forEachIndexed { index, field ->
            val comma = if (index < entityFields.size - 1) "," else ""
            appendLine("            ${field.name} = this.${field.name}$comma")
        }
        appendLine("        )")
        appendLine("    }")
        appendLine()

        // toEntity function
        appendLine("    /**")
        appendLine("     * Convert Model to Entity")
        appendLine("     */")
        appendLine("    fun $modelName.toEntity(): $entityName {")
        appendLine("        return $entityName(")
        entityFields.forEachIndexed { index, field ->
            val comma = if (index < entityFields.size - 1) "," else ""
            appendLine("            ${field.name} = this.${field.name}$comma")
        }
        appendLine("        )")
        appendLine("    }")
        appendLine()

        // List extensions
        appendLine("    /**")
        appendLine("     * Convert List of Entities to List of Models")
        appendLine("     */")
        appendLine("    fun List<$entityName>.toModelList(): List<$modelName> {")
        appendLine("        return map { it.toModel() }")
        appendLine("    }")
        appendLine()

        appendLine("    /**")
        appendLine("     * Convert List of Models to List of Entities")
        appendLine("     */")
        appendLine("    fun List<$modelName>.toEntityList(): List<$entityName> {")
        appendLine("        return map { it.toEntity() }")
        appendLine("    }")

        appendLine("}")
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
}