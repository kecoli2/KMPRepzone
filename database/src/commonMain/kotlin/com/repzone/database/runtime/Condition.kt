package com.repzone.database.runtime

sealed interface Condition {
    fun toSQL(params: MutableList<Any?>): String
}

// Basit condition'lar
data class Equals(val column: String, val value: Any?) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        return if (value == null) {
            "$column IS NULL"
        } else {
            params.add(value)
            "$column = ?"
        }
    }
}

data class NotEquals(val column: String, val value: Any?) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        return if (value == null) {
            "$column IS NOT NULL"
        } else {
            params.add(value)
            "$column != ?"
        }
    }
}

data class Like(val column: String, val pattern: String) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        params.add(pattern)
        return "$column LIKE ?"
    }
}

data class In(val column: String, val values: List<Any?>) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        if (values.isEmpty()) {
            return "1 = 0" // Always false
        }
        val placeholders = values.joinToString(", ") {
            params.add(it)
            "?"
        }
        return "$column IN ($placeholders)"
    }
}

data class NotIn(val column: String, val values: List<Any?>) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        if (values.isEmpty()) {
            return "1 = 1" // Always true
        }
        val placeholders = values.joinToString(", ") {
            params.add(it)
            "?"
        }
        return "$column NOT IN ($placeholders)"
    }
}

data class GreaterThan(val column: String, val value: Any) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        params.add(value)
        return "$column > ?"
    }
}

data class LessThan(val column: String, val value: Any) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        params.add(value)
        return "$column < ?"
    }
}

data class GreaterThanOrEqual(val column: String, val value: Any) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        params.add(value)
        return "$column >= ?"
    }
}

data class LessThanOrEqual(val column: String, val value: Any) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        params.add(value)
        return "$column <= ?"
    }
}

data class IsNull(val column: String) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        return "$column IS NULL"
    }
}

data class IsNotNull(val column: String) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        return "$column IS NOT NULL"
    }
}

// Gruplar
data class AndGroup(val conditions: List<Condition>) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        if (conditions.isEmpty()) return "1 = 1"
        if (conditions.size == 1) return conditions[0].toSQL(params)

        return conditions.joinToString(" AND ", prefix = "(", postfix = ")") {
            it.toSQL(params)
        }
    }
}

data class OrGroup(val conditions: List<Condition>) : Condition {
    override fun toSQL(params: MutableList<Any?>): String {
        if (conditions.isEmpty()) return "1 = 0"
        if (conditions.size == 1) return conditions[0].toSQL(params)

        return conditions.joinToString(" OR ", prefix = "(", postfix = ")") {
            it.toSQL(params)
        }
    }
}

// Empty condition (no WHERE clause)
object NoCondition : Condition {
    override fun toSQL(params: MutableList<Any?>): String = "1 = 1"
}