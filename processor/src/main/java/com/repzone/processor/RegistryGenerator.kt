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
            .addFunction(generateGetMetadataFunction(schemas))
            .build()

        // DatabaseExtensions fonksiyonları
        val insertFunction = generateInsertFunction()
        val updateFunction = generateUpdateFunction()
        val deleteFunction = generateDeleteFunction()
        val insertOrReplaceFunction = generateInsertOrReplaceFunction()

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
            .addFunction(insertOrReplaceFunction)
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

    private fun generateInsertFunction(): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata = %T.getMetadata(entity)",
                ClassName("com.repzone.database.runtime", "EntityMetadataRegistry"))
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }")
            .addStatement("val columnNames = insertColumns.joinToString(\", \") { it.name }")
            .addStatement("val placeholders = insertColumns.joinToString(\", \") { \"?\" }")
            .addStatement("val sql = \"INSERT INTO \${metadata.tableName} (\$columnNames) VALUES (\$placeholders)\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = insertColumns.size")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("insertColumns.forEachIndexed { index, col ->")
            .indent()
            .addStatement("%T(this, index, values[col.name])",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}")
            .unindent()
            .addStatement("}.value")

        return FunSpec.builder("insertGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Long::class)
            .addCode(codeBlock.build())
            .build()
    }

    // Basitleştirilmiş UPDATE
    private fun generateUpdateFunction(): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata = %T.getMetadata(entity)",
                ClassName("com.repzone.database.runtime", "EntityMetadataRegistry"))
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val pk = metadata.primaryKey")
            .addStatement("val updateColumns = metadata.columns.filterNot { it.isPrimaryKey }")
            .addStatement("val setClause = updateColumns.joinToString(\", \") { \"\${it.name} = ?\" }")
            .addStatement("val whereClause = \"\${pk.name} = ?\"")
            .addStatement("val sql = \"UPDATE \${metadata.tableName} SET \$setClause WHERE \$whereClause\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = updateColumns.size + 1")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("updateColumns.forEachIndexed { index, col ->")
            .indent()
            .addStatement("%T(this, index, values[col.name])",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}")
            .addStatement("%T(this, updateColumns.size, values[pk.name])",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}.value.toInt()")

        return FunSpec.builder("updateGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Int::class)
            .addCode(codeBlock.build())
            .build()
    }

    // Basitleştirilmiş DELETE
    private fun generateDeleteFunction(): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata = %T.getMetadata(entity)",
                ClassName("com.repzone.database.runtime", "EntityMetadataRegistry"))
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val pk = metadata.primaryKey")
            .addStatement("val sql = \"DELETE FROM \${metadata.tableName} WHERE \${pk.name} = ?\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = 1")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("%T(this, 0, values[pk.name])",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}.value.toInt()")

        return FunSpec.builder("deleteGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Int::class)
            .addCode(codeBlock.build())
            .build()
    }

    // Basitleştirilmiş INSERT OR REPLACE
    private fun generateInsertOrReplaceFunction(): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata = %T.getMetadata(entity)",
                ClassName("com.repzone.database.runtime", "EntityMetadataRegistry"))
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }")
            .addStatement("val columnNames = insertColumns.joinToString(\", \") { it.name }")
            .addStatement("val placeholders = insertColumns.joinToString(\", \") { \"?\" }")
            .addStatement("val sql = \"INSERT OR REPLACE INTO \${metadata.tableName} (\$columnNames) VALUES (\$placeholders)\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = insertColumns.size")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("insertColumns.forEachIndexed { index, col ->")
            .indent()
            .addStatement("%T(this, index, values[col.name])",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}")
            .unindent()
            .addStatement("}.value")

        return FunSpec.builder("insertOrReplaceGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Long::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateGetMetadataFunction(schemas: List<TableSchema>): FunSpec {
        val entityMetadataType = ClassName("com.repzone.database.runtime", "EntityMetadata")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .add("return when (entity::class.simpleName) {\n")
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
            .add("}")

        return FunSpec.builder("getMetadata")
            .addParameter("entity", anyType)
            .returns(entityMetadataType)
            .addCode(codeBlock.build())
            .build()
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
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata: %T = when (entity::class.simpleName) {",
                ClassName("com.repzone.database.runtime", "EntityMetadata"))
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
            .addStatement("}")
            .addStatement("")
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }")
            .addStatement("val columnNames = insertColumns.joinToString(\", \") { it.name }")
            .addStatement("val placeholders = insertColumns.joinToString(\", \") { \"?\" }")
            .addStatement("val sql = \"INSERT INTO \${metadata.tableName} (\$columnNames) VALUES (\$placeholders)\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = insertColumns.size")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("insertColumns.forEachIndexed { index, col ->")
            .indent()
            .addStatement("%T(this, index, values[col.name])",  // 0-based index
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}")
            .unindent()
            .addStatement("}.value")

        return FunSpec.builder("insertGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Long::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateInsertOrReplaceFunction(schemas: List<TableSchema>): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata: %T = when (entity::class.simpleName) {",
                ClassName("com.repzone.database.runtime", "EntityMetadata"))
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
            .addStatement("}")
            .addStatement("")
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val insertColumns = metadata.columns.filterNot { it.isAutoIncrement }")
            .addStatement("val columnNames = insertColumns.joinToString(\", \") { it.name }")
            .addStatement("val placeholders = insertColumns.joinToString(\", \") { \"?\" }")
            .addStatement("val sql = \"INSERT OR REPLACE INTO \${metadata.tableName} (\$columnNames) VALUES (\$placeholders)\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = insertColumns.size")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("insertColumns.forEachIndexed { index, col ->")
            .indent()
            .addStatement("%T(this, index, values[col.name])",
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}")
            .unindent()
            .addStatement("}.value")

        return FunSpec.builder("insertOrReplaceGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Long::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateUpdateFunction(schemas: List<TableSchema>): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata: %T = when (entity::class.simpleName) {",
                ClassName("com.repzone.database.runtime", "EntityMetadata"))
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
            .addStatement("}")
            .addStatement("")
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val pk = metadata.primaryKey")
            .addStatement("val updateColumns = metadata.columns.filterNot { it.isPrimaryKey }")
            .addStatement("val setClause = updateColumns.joinToString(\", \") { \"\${it.name} = ?\" }")
            .addStatement("val whereClause = \"\${pk.name} = ?\"")
            .addStatement("val sql = \"UPDATE \${metadata.tableName} SET \$setClause WHERE \$whereClause\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = updateColumns.size + 1")  // +1 for WHERE clause
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("updateColumns.forEachIndexed { index, col ->")
            .indent()
            .addStatement("%T(this, index, values[col.name])",  // index yerine index (0-based)
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}")
            .addStatement("%T(this, updateColumns.size, values[pk.name])",  // updateColumns.size (0-based için son index)
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}.value.toInt()")

        return FunSpec.builder("updateGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Int::class)
            .addCode(codeBlock.build())
            .build()
    }

    private fun generateDeleteFunction(schemas: List<TableSchema>): FunSpec {
        val sqlDriverType = ClassName("app.cash.sqldelight.db", "SqlDriver")
        val anyType = Any::class.asClassName()

        val codeBlock = CodeBlock.builder()
            .addStatement("val metadata: %T = when (entity::class.simpleName) {",
                ClassName("com.repzone.database.runtime", "EntityMetadata"))
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
            .addStatement("}")
            .addStatement("")
            .addStatement("val values = metadata.extractValues(entity)")
            .addStatement("val pk = metadata.primaryKey")
            .addStatement("val sql = \"DELETE FROM \${metadata.tableName} WHERE \${pk.name} = ?\"")
            .addStatement("")
            .addStatement("return execute(")
            .indent()
            .addStatement("identifier = null,")
            .addStatement("sql = sql,")
            .addStatement("parameters = 1")
            .unindent()
            .addStatement(") {")
            .indent()
            .addStatement("%T(this, 0, values[pk.name])",  // 0-based index
                ClassName("com.repzone.database.runtime", "bindValue"))
            .unindent()
            .addStatement("}.value.toInt()")

        return FunSpec.builder("deleteGenerated")
            .receiver(sqlDriverType)
            .addParameter("entity", anyType)
            .returns(Int::class)
            .addCode(codeBlock.build())
            .build()
    }
}