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

        resolver.getAllFiles().forEach { file ->
            file.declarations.filterIsInstance<KSClassDeclaration>().forEach { classDecl ->
                val className = classDecl.simpleName.asString()
                if (className.endsWith("Entity")) {
                    val propertyNullability = classDecl.getAllProperties()
                        .associate { prop ->
                            val propName = prop.simpleName.asString()
                            val isNullable = prop.type.resolve().isMarkedNullable
                            propName to isNullable
                        }
                    entityTypeMap[className] = propertyNullability
                    logger.info("Found entity: $className with ${propertyNullability.size} properties")
                }
            }
        }

        // 4. Her schema için metadata generate et
        schemas.forEach { schema ->
            try {
                val entityName = if (schema.tableName.endsWith("Entity")) {
                    schema.tableName
                } else {
                    "${schema.tableName}Entity"
                }

                // Entity'nin gerçek nullable bilgilerini al
                val propertyNullability = entityTypeMap[entityName]

                MetadataGenerator.generate(
                    schema = schema,
                    propertyNullability = propertyNullability,
                    codeGenerator = codeGenerator,
                    logger = logger
                )
                logger.info("Generated metadata for ${schema.tableName}")
            } catch (e: Exception) {
                logger.error("Failed to generate metadata for ${schema.tableName}: ${e.message}")
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
                logger.info("Generated EntityMetadataRegistry")
            } catch (e: Exception) {
                logger.error("Failed to generate registry: ${e.message}")
            }
        }

        // 6. Extensions generate et - YENİ
        if (schemas.isNotEmpty()) {
            try {
                EntityExtensionsGenerator.generate(
                    schemas = schemas,
                    codeGenerator = codeGenerator,
                    logger = logger
                )
                logger.info("Generated entity extensions for ${schemas.size} entities")
            } catch (e: Exception) {
                logger.error("Failed to generate extensions: ${e.message}")
            }
        }

        // 7. Mappers generate et - YENİ
        if (schemas.isNotEmpty()) {
            try {
                EntityMapperGenerator.generate(
                    schemas = schemas,
                    codeGenerator = codeGenerator,
                    logger = logger
                )
                logger.info("Generated entity mappers for ${schemas.size} entities")
            } catch (e: Exception) {
                logger.error("Failed to generate mappers: ${e.message}")
            }
        }

        return emptyList()
    }

    private fun findSqlDelightFiles(): List<File> {
        val sqPath = options["sqldelight.path"] ?: "src/commonMain/sqldelight"

        val sqDir = File(sqPath)
        if (!sqDir.exists()) {
            logger.warn("SQLDelight directory not found: $sqPath")
            return emptyList()
        }

        return sqDir.walkTopDown()
            .filter { it.isFile && it.extension == "sq" }
            .toList()
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