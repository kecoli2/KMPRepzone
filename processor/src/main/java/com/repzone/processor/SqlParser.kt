package com.repzone.processor

import java.io.File

data class TableSchema(
    val tableName: String,
    val columns: List<ColumnSchema>,
    val isView: Boolean = false
)

data class ColumnSchema(
    val name: String,
    val type: String,  // TEXT, INTEGER, REAL, BLOB
    val isPrimaryKey: Boolean,
    val isNotNull: Boolean,
    val isAutoIncrement: Boolean,
    val defaultValue: String?,
    val foreignKey: ForeignKeySchema?
)

data class ForeignKeySchema(
    val referencedTable: String,
    val referencedColumn: String,
    val onDelete: String,  // CASCADE, SET NULL, etc.
    val onUpdate: String
)

object SqlParser {

    fun parseFile(file: File): List<TableSchema> {
        val content = file.readText()
        return parseContent(content)
    }

    fun parseContent(sql: String): List<TableSchema> {
        val tables = mutableListOf<TableSchema>()

        // CREATE TABLE
        val createTablePattern = """CREATE\s+TABLE(?:\s+IF\s+NOT\s+EXISTS)?\s+(\w+)\s*\((.*?)\);""".toRegex(
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        )

        createTablePattern.findAll(sql).forEach { match ->
            val tableName = match.groupValues[1]
            val columnsBlock = match.groupValues[2]

            try {
                val columns = parseColumns(columnsBlock, tableName)
                if (columns.isNotEmpty()) {
                    tables.add(TableSchema(tableName, columns, isView = false))
                }
            } catch (e: Exception) {
                println("Warning: Failed to parse table $tableName: ${e.message}")
            }
        }

        // CREATE VIEW - SELECT statement'tan kolonları çıkar
        val createViewPattern = """CREATE\s+VIEW(?:\s+IF\s+NOT\s+EXISTS)?\s+(\w+)\s+AS\s+SELECT\s+(.*?)\s+FROM""".toRegex(
            setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
        )

        createViewPattern.findAll(sql).forEach { match ->
            val viewName = match.groupValues[1]
            val selectColumns = match.groupValues[2]

            try {
                val columns = parseViewColumns(selectColumns)
                if (columns.isNotEmpty()) {
                    tables.add(TableSchema(viewName, columns, isView = true))
                }
            } catch (e: Exception) {
                println("Warning: Failed to parse view $viewName: ${e.message}")
            }
        }

        return tables
    }

    private fun parseViewColumns(selectColumns: String): List<ColumnSchema> {
        val columns = mutableListOf<ColumnSchema>()

        // Daha karmaşık parsing için her kolonu ayrı ayrı işle
        var remainingText = selectColumns

        while (remainingText.isNotBlank()) {
            // CAST ifadesi için parantez sayımı yaparak parse et
            if (remainingText.trimStart().startsWith("CAST", ignoreCase = true)) {
                val castStart = remainingText.indexOf("CAST", ignoreCase = true)
                val openParenIndex = remainingText.indexOf("(", castStart)

                if (openParenIndex != -1) {
                    // Parantezleri say
                    var parenCount = 1
                    var currentIndex = openParenIndex + 1

                    while (currentIndex < remainingText.length && parenCount > 0) {
                        when (remainingText[currentIndex]) {
                            '(' -> parenCount++
                            ')' -> parenCount--
                        }
                        currentIndex++
                    }

                    // CAST ifadesinin tamamını al
                    val castExpression = remainingText.substring(castStart, currentIndex)

                    // Alias'ı bul (CAST(...) AS TYPE) Alias)
                    val afterCast = remainingText.substring(currentIndex).trim()
                    val aliasMatch = """^\s*(\w+)""".toRegex().find(afterCast)

                    if (aliasMatch != null) {
                        val alias = aliasMatch.groupValues[1]

                        // Tip bilgisini al
                        val typePattern = """\s+AS\s+(\w+)\s*\)""".toRegex(RegexOption.IGNORE_CASE)
                        val typeMatch = typePattern.find(castExpression)
                        val sqlType = typeMatch?.groupValues?.get(1)?.uppercase() ?: "TEXT"

                        columns.add(ColumnSchema(
                            name = alias,
                            type = mapSqlType(sqlType),
                            isPrimaryKey = false,
                            isNotNull = false,
                            isAutoIncrement = false,
                            defaultValue = null,
                            foreignKey = null
                        ))

                        // Sonraki kolona geç
                        val commaIndex = afterCast.indexOf(",", alias.length)
                        remainingText = if (commaIndex != -1) {
                            afterCast.substring(commaIndex + 1)
                        } else {
                            ""
                        }
                        continue
                    }
                }
            }

            // Normal kolon (CAST değilse)
            val commaIndex = remainingText.indexOf(",")
            val columnText = if (commaIndex != -1) {
                remainingText.substring(0, commaIndex).trim()
            } else {
                remainingText.trim()
            }

            // "column AS alias" veya sadece "column"
            val aliasPattern = """(\S+)\s+AS\s+(\w+)""".toRegex(RegexOption.IGNORE_CASE)
            val aliasMatch = aliasPattern.find(columnText)

            val columnName = if (aliasMatch != null) {
                aliasMatch.groupValues[2]
            } else {
                val parts = columnText.split(Regex("\\s+"))
                val lastPart = parts.lastOrNull()?.trim() ?: ""

                if (lastPart.contains(".")) {
                    lastPart.substringAfterLast(".")
                } else {
                    lastPart
                }
            }

            if (columnName.isNotBlank() &&
                columnName != "*" &&
                !columnName.all { it.isUpperCase() || it.isDigit() }) {
                columns.add(ColumnSchema(
                    name = columnName,
                    type = "TEXT",
                    isPrimaryKey = false,
                    isNotNull = false,
                    isAutoIncrement = false,
                    defaultValue = null,
                    foreignKey = null
                ))
            }

            // Sonraki kolona geç
            remainingText = if (commaIndex != -1) {
                remainingText.substring(commaIndex + 1)
            } else {
                ""
            }
        }

        return columns
    }
    private fun parseColumns(columnsBlock: String, tableName: String): List<ColumnSchema> {
        val columns = mutableListOf<ColumnSchema>()

        // Backtick'leri temizle
        val cleanedBlock = columnsBlock.replace("`", "")
        val foreignKeys = parseForeignKeys(cleanedBlock)

        // Her satırı parse et
        val lines = cleanedBlock.split(",").map { it.trim() }

        lines.forEach { line ->
            // FOREIGN KEY veya CONSTRAINT satırlarını atla
            if (line.uppercase().startsWith("FOREIGN KEY") ||
                line.uppercase().startsWith("CONSTRAINT") ||
                line.uppercase().startsWith("PRIMARY KEY")) {
                return@forEach
            }

            val column = parseColumnDefinition(line, foreignKeys)
            if (column != null) {
                columns.add(column)
            }
        }

        return columns
    }

    private fun parseColumnDefinition(
        definition: String,
        foreignKeys: Map<String, ForeignKeySchema>
    ): ColumnSchema? {
        if (definition.isBlank()) return null

        // Backtick'li kolon isimlerini temizle: `Key` -> Key
        val cleanDefinition = definition.replace("`", "")

        // Basit regex ile parse et
        val parts = cleanDefinition.trim().split(Regex("\\s+"))
        if (parts.isEmpty()) return null

        val columnName = parts[0]  // Artık backtick'siz
        val columnType = parts.getOrNull(1)?.uppercase() ?: "TEXT"

        val upperDef = cleanDefinition.uppercase()

        return ColumnSchema(
            name = SqlKeywordEscaper.escapeColumnName(columnName),
            type = mapSqlType(columnType),
            isPrimaryKey = upperDef.contains("PRIMARY KEY"),
            isNotNull = upperDef.contains("NOT NULL"),
            isAutoIncrement = upperDef.contains("AUTOINCREMENT"),
            defaultValue = extractDefault(cleanDefinition),
            foreignKey = foreignKeys[columnName]
        )
    }

    private fun parseForeignKeys(columnsBlock: String): Map<String, ForeignKeySchema> {
        val fkMap = mutableMapOf<String, ForeignKeySchema>()

        // Backtick'leri temizlenmiş versiyonda çalış
        val cleanedBlock = columnsBlock.replace("`", "")

        // Inline REFERENCES pattern
        val inlinePattern = """(\w+)\s+\w+.*?REFERENCES\s+(\w+)\s*\((\w+)\)(?:\s+ON\s+DELETE\s+(\w+(?:\s+\w+)?))?(?:\s+ON\s+UPDATE\s+(\w+(?:\s+\w+)?))?""".toRegex(RegexOption.IGNORE_CASE)

        inlinePattern.findAll(cleanedBlock).forEach { match ->
            val columnName = match.groupValues[1]
            val refTable = match.groupValues[2]
            val refColumn = SqlKeywordEscaper.escapeColumnName(match.groupValues[3])
            val onDelete = match.groupValues.getOrNull(4)?.uppercase() ?: "NO ACTION"
            val onUpdate = match.groupValues.getOrNull(5)?.uppercase() ?: "NO ACTION"

            fkMap[columnName] = ForeignKeySchema(
                referencedTable = refTable,
                referencedColumn = refColumn,
                onDelete = onDelete,
                onUpdate = onUpdate
            )
        }

        // Table-level FOREIGN KEY pattern
        val tableLevelPattern = """FOREIGN\s+KEY\s*\((\w+)\)\s+REFERENCES\s+(\w+)\s*\((\w+)\)(?:\s+ON\s+DELETE\s+(\w+(?:\s+\w+)?))?(?:\s+ON\s+UPDATE\s+(\w+(?:\s+\w+)?))?""".toRegex(RegexOption.IGNORE_CASE)

        tableLevelPattern.findAll(cleanedBlock).forEach { match ->
            val columnName = match.groupValues[1]
            val refTable = match.groupValues[2]
            val refColumn = SqlKeywordEscaper.escapeColumnName(match.groupValues[3])
            val onDelete = match.groupValues.getOrNull(4)?.uppercase() ?: "NO ACTION"
            val onUpdate = match.groupValues.getOrNull(5)?.uppercase() ?: "NO ACTION"

            fkMap[columnName] = ForeignKeySchema(
                referencedTable = refTable,
                referencedColumn = refColumn,
                onDelete = onDelete,
                onUpdate = onUpdate
            )
        }

        return fkMap
    }

    private fun extractDefault(definition: String): String? {
        val defaultPattern = """DEFAULT\s+\(?([^,)]+)\)?""".toRegex(RegexOption.IGNORE_CASE)
        val match = defaultPattern.find(definition)
        return match?.groupValues?.getOrNull(1)?.trim()
    }

    private fun mapSqlType(sqlType: String): String {
        return when {
            sqlType.startsWith("INT") -> "INTEGER"
            sqlType.startsWith("TEXT") || sqlType.startsWith("VARCHAR") || sqlType.startsWith("CHAR") -> "TEXT"
            sqlType.startsWith("REAL") || sqlType.startsWith("FLOAT") || sqlType.startsWith("DOUBLE") -> "REAL"
            sqlType.startsWith("BLOB") -> "BLOB"
            else -> "TEXT"
        }
    }
}