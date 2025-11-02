package com.repzone.database.runtime

object SqlKeywordEscaper {
    private val RESERVED_KEYWORDS = setOf(
        // SQLite Reserved Keywords
        "ABORT", "ACTION", "ADD", "AFTER", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC",
        "ATTACH", "AUTOINCREMENT", "BEFORE", "BEGIN", "BETWEEN", "BY", "CASCADE", "CASE",
        "CAST", "CHECK", "COLLATE", "COLUMN", "COMMIT", "CONFLICT", "CONSTRAINT", "CREATE",
        "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "DATABASE", "DEFAULT",
        "DEFERRABLE", "DEFERRED", "DELETE", "DESC", "DETACH", "DISTINCT", "DROP", "EACH",
        "ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE", "EXISTS", "EXPLAIN", "FAIL", "FOR",
        "FOREIGN", "FROM", "FULL", "GLOB", "GROUP", "HAVING", "IF", "IGNORE", "IMMEDIATE",
        "IN", "INDEX", "INDEXED", "INITIALLY", "INNER", "INSERT", "INSTEAD", "INTERSECT",
        "INTO", "IS", "ISNULL", "JOIN", "KEY", "LEFT", "LIKE", "LIMIT", "MATCH", "NATURAL",
        "NO", "NOT", "NOTNULL", "NULL", "OF", "OFFSET", "ON", "OR", "ORDER", "OUTER", "PLAN",
        "PRAGMA", "PRIMARY", "QUERY", "RAISE", "RECURSIVE", "REFERENCES", "REGEXP", "REINDEX",
        "RELEASE", "RENAME", "REPLACE", "RESTRICT", "RIGHT", "ROLLBACK", "ROW", "SAVEPOINT",
        "SELECT", "SET", "TABLE", "TEMP", "TEMPORARY", "THEN", "TO", "TRANSACTION", "TRIGGER",
        "UNION", "UNIQUE", "UPDATE", "USING", "VACUUM", "VALUES", "VIEW", "VIRTUAL", "WHEN",
        "WHERE", "WITH", "WITHOUT",

        // Common problematic words
        "USER", "GROUP", "ORDER", "DATE", "TIME", "TIMESTAMP", "YEAR", "MONTH", "DAY",
        "HOUR", "MINUTE", "SECOND", "VALUE", "TYPE", "NAME", "COUNT", "SUM", "AVG",
        "MIN", "MAX", "STATUS", "LEVEL", "POSITION", "RANGE", "RANK"
    )

    fun String.escapeKeywordsInQuery(): String {
        var result = this

        // SELECT ve FROM arasındaki kolonları bul
        val selectPattern = """SELECT\s+(.*?)\s+FROM""".toRegex(RegexOption.IGNORE_CASE)
        val match = selectPattern.find(this)

        match?.let {
            val columnsSection = it.groupValues[1]
            val columns = columnsSection.split(",").map { col -> col.trim() }

            columns.forEach { column ->
                // Eğer kolon ismi bir anahtar kelime ise ve zaten escape edilmemişse
                if (RESERVED_KEYWORDS.contains(column.uppercase()) && !column.startsWith("`")) {
                    result = result.replace(
                        Regex("\\b$column\\b", RegexOption.IGNORE_CASE),
                        "`$column`"
                    )
                }
            }
        }

        return result
    }

    fun escapeColumnName(columnName: String): String {
        return if (isReservedKeyword(columnName)) {
            // SQLite için backtick veya çift tırnak
            "`$columnName`"
        } else {
            columnName
        }
    }

    fun escapeTableName(tableName: String): String {
        return if (isReservedKeyword(tableName)) {
            "`$tableName`"
        } else {
            tableName
        }
    }

    fun isReservedKeyword(name: String): Boolean {
        return RESERVED_KEYWORDS.contains(name.uppercase())
    }

    fun escapeColumnNames(columnNames: List<String>): List<String> {
        return columnNames.map { escapeColumnName(it) }
    }

    fun escapeColumnNames(vararg columnNames: String): List<String> {
        return columnNames.map { escapeColumnName(it) }
    }

    fun escapeSqlStatement(
        sql: String,
        columns: List<String>
    ): String {
        var result = sql

        columns.forEach { column ->
            if (isReservedKeyword(column)) {
                // Kelime sınırlarında (word boundary) değiştir
                result = result.replace(
                    Regex("\\b$column\\b", RegexOption.IGNORE_CASE),
                    "`$column`"
                )
            }
        }

        return result
    }
}

fun String.escapeIfReserved(): String {
    return SqlKeywordEscaper.escapeColumnName(this)
}

fun List<String>.escapeIfReserved(): List<String> {
    return SqlKeywordEscaper.escapeColumnNames(this)
}