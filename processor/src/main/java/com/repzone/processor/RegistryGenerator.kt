package com.repzone.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.*

object RegistryGenerator {

    fun generate(
        schemas: List<TableSchema>,
        codeGenerator: CodeGenerator,
        logger: KSPLogger
    ) {
        val packageName = "com.repzone.database.runtime"

        // EntityMetadataRegistry object
        val registryObject = TypeSpec.objectBuilder("EntityMetadataRegistry")
            .addFunction(generateGetFunction(schemas))
            .build()

        // DatabaseExtensions fonksiyonları
        val insertFunction = generateInsertFunction(schemas)
        val updateFunction = generateUpdateFunction(schemas)
        val deleteFunction = generateDeleteFunction(schemas)

        // File oluştur
        val fileBuilder = FileSpec.builder(packageName, "EntityMetadataRegistryGenerated")

        // Her entity için import ekle - doğru syntax
        schemas.forEach { schema ->
            val entityName = if (schema.tableName.endsWith("Entity")) {
                schema.tableName
            } else {
                "${schema.tableName}Entity"
            }
            // Doğru syntax: addImport(packageName, className)
            fileBuilder.addImport("com.repzone.database", entityName)
        }

        // Type ve function'ları ekle
        val file = fileBuilder
            .addType(registryObject)
            .addFunction(insertFunction)
            .addFunction(updateFunction)
            .addFunction(deleteFunction)
            .build()

        // Write to file
        try {
            codeGenerator.createNewFile(
                dependencies = Dependencies(false),
                packageName = packageName,
                fileName = "EntityMetadataRegistryGenerated"
            ).use { output ->
                output.writer().use { writer ->
                    file.writeTo(writer)
                }
            }
        } catch (e: FileAlreadyExistsException) {
            logger.warn("Registry file already exists, skipping")
        }
    }

    private fun generateGetFunction(schemas: List<TableSchema>): FunSpec {
        val entityMetadataType = ClassName("com.repzone.database.runtime", "EntityMetadata")

        val codeBlock = CodeBlock.builder()
            .add("return when (T::class) {\n")
            .indent()

        schemas.forEach { schema ->
            val entityName = if (schema.tableName.endsWith("Entity")) {
                schema.tableName
            } else {
                "${schema.tableName}Entity"
            }
            val metadataName = "${schema.tableName}Metadata"

            codeBlock.add(
                "%L::class -> %T\n",
                entityName,
                ClassName("com.repzone.database.metadata", metadataName)
            )
        }

        codeBlock.add("else -> throw IllegalArgumentException(\"Unknown entity type: \${T::class}\")\n")
            .unindent()
            .add("}")

        return FunSpec.builder("get")
            .addModifiers(KModifier.INLINE)
            .addTypeVariable(TypeVariableName("T", Any::class).copy(reified = true))
            .returns(entityMetadataType)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateInsertFunction(schemas: List<TableSchema>): FunSpec {
        val entityMetadataType = ClassName("com.repzone.database.runtime", "EntityMetadata")
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")

        val codeBlock = CodeBlock.builder()
            .add("val metadata: %T = when (entity::class.simpleName) {\n", entityMetadataType)
            .indent()

        schemas.filter { !it.isView }.forEach { schema ->
            val entityName = if (schema.tableName.endsWith("Entity")) {
                schema.tableName
            } else {
                "${schema.tableName}Entity"
            }
            val metadataName = "${schema.tableName}Metadata"

            codeBlock.add(
                "%S -> %T\n",
                entityName,
                ClassName("com.repzone.database.metadata", metadataName)
            )
        }

        codeBlock.add("else -> throw IllegalArgumentException(\"Unknown entity type: \${entity::class.simpleName}\")\n")
            .unindent()
            .add("}\n\n")
            .add("val values = metadata.extractValues(entity)\n")
            .add("val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }\n")
            .add("val columnNames = insertColumns.joinToString(\", \") { it.name }\n")
            .add("val placeholders = insertColumns.joinToString(\", \") { \"?\" }\n")
            .add("val sql = \"INSERT INTO \${metadata.tableName} (\$columnNames) VALUES (\$placeholders)\"\n\n")
            .add("return execute(\n")
            .indent()
            .add("identifier = null,\n")
            .add("sql = sql,\n")
            .add("parameters = insertColumns.size\n")
            .unindent()
            .add(") {\n")
            .indent()
            .add("insertColumns.forEachIndexed { index, col ->\n")
            .indent()
            .add("%T(this, index, values[col.name])\n",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .add("}\n")
            .unindent()
            .add("}.value\n")

        return FunSpec.builder("insertGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", Any::class)
            .returns(Long::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateUpdateFunction(schemas: List<TableSchema>): FunSpec {
        val entityMetadataType = ClassName("com.repzone.database.runtime", "EntityMetadata")
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")

        val codeBlock = CodeBlock.builder()
            .add("val metadata: %T = when (entity::class.simpleName) {\n", entityMetadataType)
            .indent()

        schemas.filter { !it.isView }.forEach { schema ->
            val entityName = if (schema.tableName.endsWith("Entity")) {
                schema.tableName
            } else {
                "${schema.tableName}Entity"
            }
            val metadataName = "${schema.tableName}Metadata"

            codeBlock.add(
                "%S -> %T\n",
                entityName,
                ClassName("com.repzone.database.metadata", metadataName)
            )
        }

        codeBlock.add("else -> throw IllegalArgumentException(\"Unknown entity type: \${entity::class.simpleName}\")\n")
            .unindent()
            .add("}\n\n")
            .add("val values = metadata.extractValues(entity)\n")
            .add("val pk = metadata.primaryKey\n")
            .add("val updateColumns = metadata.columns.filterNot { it.isPrimaryKey }\n")
            .add("val setClause = updateColumns.joinToString(\", \") { \"\${it.name} = ?\" }\n")
            .add("val whereClause = \"\${pk.name} = ?\"\n")
            .add("val sql = \"UPDATE \${metadata.tableName} SET \$setClause WHERE \$whereClause\"\n\n")
            .add("return execute(\n")
            .indent()
            .add("identifier = null,\n")
            .add("sql = sql,\n")
            .add("parameters = updateColumns.size + 1\n")
            .unindent()
            .add(") {\n")
            .indent()
            .add("updateColumns.forEachIndexed { index, col ->\n")
            .indent()
            .add("%T(this, index, values[col.name])\n",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .add("}\n")
            .add("%T(this, updateColumns.size + 1, values[pk.name])\n",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .add("}.value.toInt()\n")

        return FunSpec.builder("updateGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", Any::class)
            .returns(Int::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateDeleteFunction(schemas: List<TableSchema>): FunSpec {
        val entityMetadataType = ClassName("com.repzone.database.runtime", "EntityMetadata")
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")

        val codeBlock = CodeBlock.builder()
            .add("val metadata: %T = when (entity::class.simpleName) {\n", entityMetadataType)
            .indent()

        schemas.filter { !it.isView }.forEach { schema ->
            val entityName = if (schema.tableName.endsWith("Entity")) {
                schema.tableName
            } else {
                "${schema.tableName}Entity"
            }
            val metadataName = "${schema.tableName}Metadata"

            codeBlock.add(
                "%S -> %T\n",
                entityName,
                ClassName("com.repzone.database.metadata", metadataName)
            )
        }

        codeBlock.add("else -> throw IllegalArgumentException(\"Unknown entity type: \${entity::class.simpleName}\")\n")
            .unindent()
            .add("}\n\n")
            .add("val values = metadata.extractValues(entity)\n")
            .add("val pk = metadata.primaryKey\n")
            .add("val sql = \"DELETE FROM \${metadata.tableName} WHERE \${pk.name} = ?\"\n\n")
            .add("return execute(\n")
            .indent()
            .add("identifier = null,\n")
            .add("sql = sql,\n")
            .add("parameters = 1\n")
            .unindent()
            .add(") {\n")
            .indent()
            .add("%T(this, 1, values[pk.name])\n",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .add("}.value.toInt()\n")

        return FunSpec.builder("deleteGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", Any::class)
            .returns(Int::class)
            .addCode(codeBlock.build())
            .build()
    }
}