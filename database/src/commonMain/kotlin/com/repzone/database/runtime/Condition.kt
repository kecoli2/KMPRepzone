package com.repzone.database.runtime

sealed interface Condition {
    fun toSQL(params: MutableList<Any?>): String
}

object NoCondition : Condition {
    override fun toSQL(params: MutableList<Any?>): String = ""
}

data class FieldCondition(val field: String, val operator: String, val value: Any?) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        return when (operator) {
            "IS NULL", "IS NOT NULL" -> "$field $operator"
            else -> {
                params.add(value)
                "$field $operator ?"
            }
        }
    }
}

data class InCondition(val field: String, val values: List<Any>, val isNotIn: Boolean = false) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        if (values.isEmpty()) {
            return if (isNotIn) "1=1" else "1=0"
        }

        val placeholders = values.joinToString(", ") { "?" }
        params.addAll(values)

        val operator = if (isNotIn) "NOT IN" else "IN"
        return "$field $operator ($placeholders)"
    }
}
data class BetweenCondition(val field: String, val start: Any, val end: Any) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        params.add(start)
        params.add(end)
        return "$field BETWEEN ? AND ?"
    }
}

data class NotCondition(val condition: Condition) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        val sql = condition.toSQL(params)
        return if (sql.isNotEmpty()) "NOT ($sql)" else ""
    }
}

data class AndCondition(val conditions: List<Condition>) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        val sqlParts = conditions.mapNotNull { condition ->
            val sql = condition.toSQL(params)
            if (sql.isNotEmpty()) sql else null
        }

        return if (sqlParts.isNotEmpty()) {
            sqlParts.joinToString(" AND ", "(", ")")
        } else {
            ""
        }
    }
}

data class OrCondition(val conditions: List<Condition>) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        val sqlParts = conditions.mapNotNull { condition ->
            val sql = condition.toSQL(params)
            if (sql.isNotEmpty()) sql else null
        }

        return if (sqlParts.isNotEmpty()) {
            sqlParts.joinToString(" OR ", "(", ")")
        } else {
            ""
        }
    }
}