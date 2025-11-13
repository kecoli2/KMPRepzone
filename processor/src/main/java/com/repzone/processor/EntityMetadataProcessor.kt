package com.repzone.database.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.repzone.processor.MetadataGenerator
import com.repzone.processor.RegistryGenerator
import com.repzone.processor.SqlParser
import java.io.File
class EntityMetadataProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val currentModule = options["module"] ?: ""

        if (currentModule != "database") {
            return emptyList()
        }

        logger.info("EntityMetadataProcessor started")

        // 1. .sq dosyalarını bul
        val sqFiles = findSqlDelightFiles()
        logger.info("Found ${sqFiles.size} .sq files")

        if (sqFiles.isEmpty()) {
            logger.warn("No .sq files found!")
            return emptyList()
        }

        // 2. Her .sq dosyasını parse et
        val schemas = sqFiles.mapNotNull { file ->
            try {
                SqlParser.parseFile(file)
            } catch (e: Exception) {
                logger.error("Failed to parse ${file.name}: ${e.message}")
                null
            }
        }.flatten()

        logger.info("Parsed ${schemas.size} table schemas")

        // 3. SQLDelight entity'leri bul ve tip bilgilerini al
        val entityTypeMap = mutableMapOf<String, Map<String, Boolean>>()
        val entityClassMap = mutableMapOf<String, KSClassDeclaration>()

        logger.info("Searching for SQLDelight entities in KSP...")
        var foundCount = 0

        resolver.getAllFiles().forEach { file ->
            logger.info("Checking file: ${file.fileName}")
            file.declarations.filterIsInstance<KSClassDeclaration>().forEach { classDecl ->
                val className = classDecl.simpleName.asString()
                if (className.endsWith("Entity")) {
                    foundCount++
                    val propertyNullability = classDecl.getAllProperties()
                        .associate { prop ->
                            val propName = prop.simpleName.asString()
                            val isNullable = prop.type.resolve().isMarkedNullable
                            propName to isNullable
                        }
                    entityTypeMap[className] = propertyNullability
                    entityClassMap[className] = classDecl
                    logger.info("✓ Found entity: $className with ${propertyNullability.size} properties")

                    // Debug: Her property'yi logla
                    propertyNullability.forEach { (name, nullable) ->
                        logger.info("    $name: ${if (nullable) "nullable" else "non-null"}")
                    }
                }
            }
        }

        logger.info("Total entities found via KSP: $foundCount")
        logger.info("Entity type map size: ${entityTypeMap.size}")

        // 4. Her schema için metadata generate et
        schemas.forEach { schema ->
            try {
                val entityName = if (schema.tableName.endsWith("Entity")) {
                    schema.tableName
                } else {
                    "${schema.tableName}Entity"
                }

                // VIEW ise entity class'ından kolonları al
                val finalSchema = if (schema.isView) {
                    val entityClass = entityClassMap[entityName]
                    if (entityClass != null) {
                        logger.info("✓ VIEW detected: $entityName, extracting columns from entity class")
                        val columnsFromEntity = extractColumnsFromEntity(entityClass, logger)
                        schema.copy(columns = columnsFromEntity)
                    } else {
                        logger.warn("⚠ VIEW entity not found in KSP: $entityName, using schema columns")
                        schema
                    }
                } else {
                    schema
                }

                // Entity'nin gerçek nullable bilgilerini al
                val propertyNullability = entityTypeMap[entityName]

                logger.info("=" .repeat(70))
                logger.info("Processing: $entityName")
                logger.info("Is View: ${finalSchema.isView}")
                logger.info("Has property nullability: ${propertyNullability != null}")

                if (propertyNullability != null) {
                    logger.info("Property nullability map (${propertyNullability.size} properties):")
                    propertyNullability.forEach { (name, nullable) ->
                        logger.info("  $name: ${if (nullable) "nullable" else "non-null"}")
                    }
                } else {
                    logger.warn("⚠ No property nullability found for $entityName!")
                    logger.warn("  Will use schema info (isNotNull) as fallback")
                }

                logger.info("Schema columns (${finalSchema.columns.size} columns):")
                finalSchema.columns.forEach { col ->
                    logger.info("  ${col.name}: type=${col.type}, isNotNull=${col.isNotNull}")
                }
                logger.info("=" .repeat(70))

                MetadataGenerator.generate(
                    schema = finalSchema,
                    propertyNullability = propertyNullability,
                    codeGenerator = codeGenerator,
                    logger = logger
                )
                logger.info("✓ Generated metadata for ${finalSchema.tableName}")
            } catch (e: Exception) {
                logger.error("✗ Failed to generate metadata for ${schema.tableName}: ${e.message}")
                e.printStackTrace()
            }
        }

        // 5. Registry generate et
        if (schemas.isNotEmpty()) {
            try {
                RegistryGenerator.generate(
                    schemas = schemas,
                    codeGenerator = codeGenerator,
                    logger = logger
                )
                logger.info("✓ Generated EntityMetadataRegistry")
            } catch (e: Exception) {
                logger.error("✗ Failed to generate registry: ${e.message}")
            }
        }

        // 6. Extensions generate et
        if (schemas.isNotEmpty()) {
            try {
                EntityExtensionsGenerator.generate(
                    schemas = schemas,
                    codeGenerator = codeGenerator,
                    logger = logger
                )
                logger.info("✓ Generated entity extensions for ${schemas.size} entities")
            } catch (e: Exception) {
                logger.error("✗ Failed to generate extensions: ${e.message}")
            }
        }

        // 7. Mappers generate et
        if (schemas.isNotEmpty()) {
            try {
                EntityMapperGenerator.generate(
                    schemas = schemas,
                    logger = logger
                )
                logger.info("✓ Generated entity mappers for ${schemas.size} entities")
            } catch (e: Exception) {
                logger.error("✗ Failed to generate mappers: ${e.message}")
            }
        }

        // 8. Domain Models generate et

        if (schemas.isNotEmpty()) {
            try {
                DomainModelGenerator.generate(
                    schemas = schemas,
                    logger = logger
                )
                logger.info("✓ Generated domain models for ${schemas.size} entities")
            } catch (e: Exception) {
                logger.error("✗ Failed to generate domain models: ${e.message}")
            }
        }

        return emptyList()
    }

    private fun findSqlDelightFiles(): List<File> {
        // Önce options'dan al
        var sqPath = options["sqldelight.path"]

        // Yoksa default path'leri dene
        if (sqPath == null) {
            val possiblePaths = listOf(
                "database/src/commonMain/sqldelight",
                "src/commonMain/sqldelight",
                "../database/src/commonMain/sqldelight"
            )

            for (path in possiblePaths) {
                val dir = File(path)
                if (dir.exists() && dir.isDirectory) {
                    sqPath = path
                    logger.info("Found SQLDelight directory at: $path")
                    break
                }
            }
        }

        if (sqPath == null) {
            sqPath = "src/commonMain/sqldelight" // fallback
        }

        val sqDir = File(sqPath)
        if (!sqDir.exists()) {
            logger.warn("SQLDelight directory not found: $sqPath")
            return emptyList()
        }

        logger.info("Searching for .sq files in: ${sqDir.absolutePath}")

        val files = sqDir.walkTopDown()
            .filter { it.isFile && it.extension == "sq" }
            .toList()

        files.forEach { logger.info("  Found: ${it.name}") }

        return files
    }

    private fun extractColumnsFromEntity(
        entityClass: KSClassDeclaration,
        logger: KSPLogger
    ): List<com.repzone.processor.ColumnSchema> {
        logger.info("Extracting columns from entity: ${entityClass.simpleName.asString()}")

        val columns = entityClass.getAllProperties().map { property ->
            val propName = property.simpleName.asString()
            val propType = property.type.resolve()
            val isNullable = propType.isMarkedNullable

            val sqlType = mapKotlinTypeToSql(propType)

            logger.info("  Property: $propName -> SQL Type: $sqlType, Nullable: $isNullable")

            com.repzone.processor.ColumnSchema(
                name = propName,
                type = sqlType,
                isPrimaryKey = false,  // VIEW'larda PK yok
                isNotNull = !isNullable,
                isAutoIncrement = false,
                defaultValue = null,
                foreignKey = null
            )
        }.toList()

        logger.info("✓ Extracted ${columns.size} columns from entity")
        return columns
    }

    private fun mapKotlinTypeToSql(ksType: KSType): String {
        return when (ksType.declaration.simpleName.asString()) {
            "String" -> "TEXT"
            "Long", "Int" -> "INTEGER"
            "Double", "Float" -> "REAL"
            "ByteArray" -> "BLOB"
            "Boolean" -> "INTEGER"
            else -> "TEXT"
        }
    }
}

class EntityMetadataProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return EntityMetadataProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}