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

    private fun generateMapperForEntity(schema: TableSchema, logger: KSPLogger) {
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
                name = column.name.replace("`", ""),
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
            mapperFile.writeText(code)
            logger.info("Successfully generated mapper: ${mapperFile.absolutePath}")
        } catch (e: Exception) {
            logger.error("Failed to write mapper file: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private fun generateMapperCode(mapperName: String, entityName: String, modelName: String, entityFields: List<EntityField>, mapperPackage: String, entityPackage: String, modelPackage: String): String = buildString {
        appendLine("package $mapperPackage")
        appendLine()
        appendLine("import com.repzone.data.util.Mapper")
        appendLine("import $entityPackage.$entityName")
        appendLine("import $modelPackage.$modelName")
        appendLine()
        appendLine("class $mapperName : Mapper<$entityName, $modelName> {")
        appendLine("    //region Public Method")

        // toDomain method
        appendLine("    override fun toDomain(from: $entityName): $modelName {")
        appendLine("        return $modelName(")
        entityFields.forEachIndexed { index, field ->
            val comma = if (index < entityFields.size - 1) "," else ""
            // Entity: PascalCase (Key), Model: camelCase (key)
            val modelFieldName = field.name.toCamelCase()
            appendLine("            $modelFieldName = from.${field.name}$comma")
        }
        appendLine("        )")
        appendLine("    }")
        appendLine()

        // fromDomain method
        appendLine("    override fun fromDomain(domain: $modelName): $entityName {")
        appendLine("        return $entityName(")
        entityFields.forEachIndexed { index, field ->
            val comma = if (index < entityFields.size - 1) "," else ""
            // Entity: PascalCase (Key), Model: camelCase (key)
            val modelFieldName = field.name.toCamelCase()
            appendLine("            ${field.name} = domain.$modelFieldName$comma")
        }
        appendLine("        )")
        appendLine("    }")

        appendLine("    //endregion")
        appendLine()
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

    // PascalCase -> camelCase
    // Key -> key
    // LastSyncDate -> lastSyncDate
    private fun String.toCamelCase(): String {
        if (this.isEmpty()) return this
        return this.first().lowercase() + this.substring(1)
    }
}