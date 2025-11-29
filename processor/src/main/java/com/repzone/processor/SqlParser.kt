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
        // FROM olan view'ler için: SELECT ... FROM
        // FROM olmayan view'ler için: SELECT ... ;
        val createViewWithFromPattern = """CREATE\s+VIEW(?:\s+IF\s+NOT\s+EXISTS)?\s+(\w+)\s+AS\s+SELECT\s+([\s\S]*?)\s+FROM\s+""".toRegex(
            RegexOption.IGNORE_CASE
        )

        val createViewWithoutFromPattern = """CREATE\s+VIEW(?:\s+IF\s+NOT\s+EXISTS)?\s+(\w+)\s+AS\s+SELECT\s+([\s\S]*?)\s*;""".toRegex(
            RegexOption.IGNORE_CASE
        )

        // Önce FROM olan view'leri dene
        createViewWithFromPattern.findAll(sql).forEach { match ->
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

        // FROM olmayan view'ler (sadece literal değerlerle)
        val viewNamesAlreadyParsed = tables.filter { it.isView }.map { it.tableName }.toSet()
        createViewWithoutFromPattern.findAll(sql).forEach { match ->
            val viewName = match.groupValues[1]
            if (viewName in viewNamesAlreadyParsed) return@forEach // Zaten parse edildi

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

        // Virgülle ayır ama parantez içindeki virgülleri koru
        val columnList = splitByCommaRespectingParentheses(selectColumns)

        columnList.forEach { columnText ->
            val trimmed = columnText.trim()
            if (trimmed.isBlank()) return@forEach

            // CAST ifadesi için özel handling
            if (trimmed.startsWith("CAST", ignoreCase = true)) {
                val castColumn = parseCastExpression(trimmed)
                if (castColumn != null) {
                    columns.add(castColumn)
                }
                return@forEach
            }

            // Normal kolon: "value AS alias" veya "table.column AS alias" veya "table.column"
            // Son AS'den sonraki kısım alias
            val lastAsIndex = trimmed.lastIndexOf(" AS ", ignoreCase = true)

            if (lastAsIndex != -1) {
                val alias = trimmed.substring(lastAsIndex + 4).trim()
                if (alias.isNotBlank() && isValidColumnName(alias)) {
                    // Değerin tipini tahmin et
                    val valuePart = trimmed.substring(0, lastAsIndex).trim()
                    val inferredType = inferTypeFromValue(valuePart)

                    columns.add(ColumnSchema(
                        name = alias,
                        type = inferredType,
                        isPrimaryKey = false,
                        isNotNull = false,
                        isAutoIncrement = false,
                        defaultValue = null,
                        foreignKey = null
                    ))
                }
            } else {
                // AS yok, direkt kolon adı (table.column veya column)
                val columnName = if (trimmed.contains(".")) {
                    trimmed.substringAfterLast(".")
                } else {
                    trimmed
                }

                if (isValidColumnName(columnName)) {
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
            }
        }

        return columns
    }

    /**
     * Virgülle ayır ama parantez içindeki virgülleri koru
     */
    private fun splitByCommaRespectingParentheses(text: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var parenDepth = 0

        for (char in text) {
            when {
                char == '(' -> {
                    parenDepth++
                    current.append(char)
                }
                char == ')' -> {
                    parenDepth--
                    current.append(char)
                }
                char == ',' && parenDepth == 0 -> {
                    result.add(current.toString())
                    current = StringBuilder()
                }
                else -> current.append(char)
            }
        }

        if (current.isNotBlank()) {
            result.add(current.toString())
        }

        return result
    }

    /**
     * CAST(value AS TYPE) AS alias veya CAST(value AS TYPE) alias şeklindeki ifadeyi parse et
     */
    private fun parseCastExpression(expression: String): ColumnSchema? {
        // İki format desteklenir:
        // 1. CAST(NULL AS REAL) AS Weight  (AS ile)
        // 2. CAST(R.Id AS INTEGER) AppointmentId  (AS olmadan)

        // Parantezin kapandığı yeri bul
        val openParen = expression.indexOf('(')
        if (openParen == -1) return null

        var parenCount = 1
        var closeParen = openParen + 1
        while (closeParen < expression.length && parenCount > 0) {
            when (expression[closeParen]) {
                '(' -> parenCount++
                ')' -> parenCount--
            }
            closeParen++
        }
        closeParen-- // Son ')' pozisyonu

        // CAST içindeki tip: CAST(... AS TYPE)
        val castContent = expression.substring(openParen + 1, closeParen)
        val typeMatch = """\s+AS\s+(\w+)\s*$""".toRegex(RegexOption.IGNORE_CASE).find(castContent)
        val sqlType = typeMatch?.groupValues?.get(1)?.uppercase() ?: "TEXT"

        // Alias'ı bul - iki format dene
        val afterCast = expression.substring(closeParen + 1).trim()

        // Format 1: CAST(...) AS alias
        val aliasWithAsMatch = """^\s*AS\s+(\w+)""".toRegex(RegexOption.IGNORE_CASE).find(afterCast)
        // Format 2: CAST(...) alias (AS olmadan)
        val aliasWithoutAsMatch = """^\s*(\w+)""".toRegex().find(afterCast)

        val alias = when {
            aliasWithAsMatch != null -> aliasWithAsMatch.groupValues[1]
            aliasWithoutAsMatch != null -> aliasWithoutAsMatch.groupValues[1]
            else -> null
        }

        if (alias != null && isValidColumnName(alias)) {
            return ColumnSchema(
                name = alias,
                type = mapSqlType(sqlType),
                isPrimaryKey = false,
                isNotNull = false,
                isAutoIncrement = false,
                defaultValue = null,
                foreignKey = null
            )
        }

        return null
    }

    /**
     * Geçerli kolon adı mı kontrol et
     */
    private fun isValidColumnName(name: String): Boolean {
        if (name.isBlank()) return false
        if (name == "*") return false
        // SQL keyword'leri değil (AS, FROM, WHERE, etc.)
        val sqlKeywords = setOf("AS", "FROM", "WHERE", "SELECT", "AND", "OR", "NOT", "NULL", "INTEGER", "TEXT", "REAL", "BLOB")
        if (name.uppercase() in sqlKeywords) return false
        // Sadece büyük harf ve rakam değil (tip isimleri olabilir)
        if (name.all { it.isUpperCase() || it.isDigit() }) return false
        return true
    }

    /**
     * Değerden tip tahmin et
     */
    private fun inferTypeFromValue(value: String): String {
        val trimmed = value.trim()
        return when {
            trimmed.startsWith("'") || trimmed.startsWith("\"") -> "TEXT"
            trimmed.contains(".") && trimmed.toDoubleOrNull() != null -> "REAL"
            trimmed.toIntOrNull() != null || trimmed.toLongOrNull() != null -> "INTEGER"
            trimmed.equals("NULL", ignoreCase = true) -> "TEXT"
            else -> "TEXT"
        }
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