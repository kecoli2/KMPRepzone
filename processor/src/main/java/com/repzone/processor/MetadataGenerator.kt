package com.repzone.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

object MetadataGenerator {

    fun generate(
        schema: TableSchema,
        propertyNullability: Map<String, Boolean>?,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        val packageName = "com.repzone.database.metadata"

        val entityName = if (schema.tableName.endsWith("Entity")) {
            schema.tableName
        } else {
            "${schema.tableName}Entity"
        }

        val metadataName = "${schema.tableName}Metadata"

        // Metadata object oluştur
        val metadataObject = TypeSpec.objectBuilder(metadataName)
            .addSuperinterface(ClassName("com.repzone.database.runtime", "EntityMetadata"))
            .addProperty(generateTableNameProperty(schema))
            .addProperty(generateColumnsProperty(schema))
            .addProperty(generatePrimaryKeyProperty())
            .addFunction(generateCreateInstanceFunction(schema, entityName, propertyNullability))
            .addFunction(generateExtractValuesFunction(schema, entityName))
            .build()

        // File oluştur - SQLDelight entity import'unu ekle
        val file = FileSpec.builder(packageName, metadataName)
            .addImport("com.repzone.database", entityName)
            .addType(metadataObject)
            .build()

        // Write to file
        try {
            codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = packageName,
                fileName = metadataName
            ).use { output ->
                output.writer().use { writer ->
                    file.writeTo(writer)
                }
            }
        } catch (e: FileAlreadyExistsException) {
            logger.warn("File already exists, skipping: $metadataName")
        }
    }

    private fun generateTableNameProperty(schema: TableSchema): PropertySpec {
        return PropertySpec.builder("tableName", String::class, KModifier.OVERRIDE)
            .initializer("%S", schema.tableName)
            .build()
    }

    private fun generateColumnsProperty(schema: TableSchema): PropertySpec {
        val listType = ClassName("kotlin.collections", "List")
        val columnMetadataType = ClassName("com.repzone.database.runtime", "ColumnMetadata")
        val propertyType = listType.parameterizedBy(columnMetadataType)

        val codeBlock = CodeBlock.builder()
            .add("listOf(\n")
            .indent()

        schema.columns.forEachIndexed { index, column ->
            codeBlock.add(generateColumnMetadata(column))
            if (index < schema.columns.size - 1) {
                codeBlock.add(",\n")
            }
        }

        codeBlock.unindent()
            .add("\n)")

        return PropertySpec.builder("columns", propertyType, KModifier.OVERRIDE)
            .initializer(codeBlock.build())
            .build()
    }

    private fun generateColumnMetadata(column: ColumnSchema): CodeBlock {
        val columnTypeEnum = ClassName("com.repzone.database.runtime", "ColumnType")
        val foreignKeyClass = ClassName("com.repzone.database.runtime", "ForeignKeyConstraint")
        val foreignKeyActionEnum = ClassName("com.repzone.database.runtime", "ForeignKeyAction")

        return CodeBlock.builder()
            .add("%T(\n", ClassName("com.repzone.database.runtime", "ColumnMetadata"))
            .indent()
            .add("name = %S,\n", column.name)
            .add("type = %T.%L,\n", columnTypeEnum, column.type)
            .add("isNullable = %L,\n", !column.isNotNull)
            .add("isPrimaryKey = %L,\n", column.isPrimaryKey)
            .add("isAutoIncrement = %L,\n", column.isAutoIncrement)
            .add("defaultValue = %L,\n", column.defaultValue?.let { "\"$it\"" } ?: "null")
            .apply {
                if (column.foreignKey != null) {
                    val fk = column.foreignKey
                    add("foreignKey = %T(\n", foreignKeyClass)
                    indent()
                    add("referencedTable = %S,\n", fk.referencedTable)
                    add("referencedColumn = %S,\n", fk.referencedColumn)
                    add("onDelete = %T.%L,\n", foreignKeyActionEnum, mapForeignKeyAction(fk.onDelete))
                    add("onUpdate = %T.%L\n", foreignKeyActionEnum, mapForeignKeyAction(fk.onUpdate))
                    unindent()
                    add(")\n")
                } else {
                    add("foreignKey = null\n")
                }
            }
            .unindent()
            .add(")")
            .build()
    }

    private fun generatePrimaryKeyProperty(): PropertySpec {
        return PropertySpec.builder(
            "primaryKey",
            ClassName("com.repzone.database.runtime", "ColumnMetadata"),
            KModifier.OVERRIDE
        )
            .initializer("columns.firstOrNull { it.isPrimaryKey } ?: columns.first()")
            .build()
    }

    private fun generateCreateInstanceFunction(
        schema: TableSchema,
        entityName: String,
        propertyNullability: Map<String, Boolean>?
    ): FunSpec {
        val cursorType = ClassName("com.repzone.database.runtime", "Cursor")

        val codeBlock = CodeBlock.builder()
            .add("return %L(\n", entityName)
            .indent()

        schema.columns.forEachIndexed { index, column ->
            val cursorMethod = getCursorMethod(column.type)

            // Property'nin nullable olup olmadığını kontrol et
            val isPropertyNullable = propertyNullability?.get(column.name) ?: !column.isNotNull

            // Eğer property non-null ve cursor nullable dönüyorsa !! ekle
            val nullableOperator = if (!isPropertyNullable) "!!" else ""

            codeBlock.add(
                "%L = cursor.%L(offset + %L)%L",
                column.name,
                cursorMethod,
                index,
                nullableOperator
            )

            if (index < schema.columns.size - 1) {
                codeBlock.add(",\n")
            } else {
                codeBlock.add("\n")
            }
        }

        codeBlock.unindent()
            .add(")")

        return FunSpec.builder("createInstance")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("cursor", cursorType)
            .addParameter("offset", Int::class)
            .returns(Any::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateExtractValuesFunction(schema: TableSchema, entityName: String): FunSpec {
        val codeBlock = CodeBlock.builder()
            .addStatement("require(entity is %L)", entityName)
            .add("return mapOf(\n")
            .indent()

        schema.columns.forEachIndexed { index, column ->
            codeBlock.add("%S to entity.%L", column.name, column.name)
            if (index < schema.columns.size - 1) {
                codeBlock.add(",\n")
            } else {
                codeBlock.add("\n")
            }
        }

        codeBlock.unindent()
            .add(")")

        return FunSpec.builder("extractValues")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("entity", Any::class)
            .returns(
                Map::class.asClassName().parameterizedBy(
                    String::class.asClassName(),
                    Any::class.asClassName().copy(nullable = true)
                )
            )
            .addCode(codeBlock.build())
            .build()
    }

    private fun getCursorMethod(columnType: String): String {
        return when (columnType) {
            "TEXT" -> "getString"
            "INTEGER" -> "getLong"
            "REAL" -> "getDouble"
            "BLOB" -> "getBytes"
            else -> "getString"
        }
    }

    private fun mapForeignKeyAction(action: String): String {
        return when (action.uppercase().replace(" ", "_")) {
            "CASCADE" -> "CASCADE"
            "SET_NULL" -> "SET_NULL"
            "SET_DEFAULT" -> "SET_DEFAULT"
            "RESTRICT" -> "RESTRICT"
            else -> "NO_ACTION"
        }
    }
}