package com.repzone.database.runtime

class CriteriaBuilder {
    private val conditions = mutableListOf<Condition>()

    fun criteria(
        field: String,
        equal: Any? = null,
        notEqual: Any? = null,
        like: String? = null,
        notLike: String? = null,
        greaterThan: Any? = null,
        greaterThanOrEqual: Any? = null,
        lessThan: Any? = null,
        lessThanOrEqual: Any? = null,
        isNull: Boolean? = null,
        In: List<Any>? = null,
        notIn: List<Any>? = null,
        between: Pair<Any, Any>? = null
    ) {
        when {
            equal != null -> {
                conditions.add(FieldCondition(field, "=", equal))
            }
            notEqual != null -> {
                conditions.add(FieldCondition(field, "!=", notEqual))
            }
            like != null -> {
                conditions.add(FieldCondition(field, "LIKE", like))
            }
            notLike != null -> {
                conditions.add(FieldCondition(field, "NOT LIKE", notLike))
            }
            greaterThan != null -> {
                conditions.add(FieldCondition(field, ">", greaterThan))
            }
            greaterThanOrEqual != null -> {
                conditions.add(FieldCondition(field, ">=", greaterThanOrEqual))
            }
            lessThan != null -> {
                conditions.add(FieldCondition(field, "<", lessThan))
            }
            lessThanOrEqual != null -> {
                conditions.add(FieldCondition(field, "<=", lessThanOrEqual))
            }
            isNull != null -> {
                conditions.add(
                    FieldCondition(
                        field,
                        if (isNull) "IS NULL" else "IS NOT NULL",
                        null
                    )
                )
            }
            In != null -> {
                conditions.add(InCondition(field, In, false))
            }
            notIn != null -> {
                conditions.add(InCondition(field, notIn, true))
            }
            between != null -> {
                conditions.add(BetweenCondition(field, between.first, between.second))
            }
        }
    }

    fun or(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        if (builder.conditions.isNotEmpty()) {
            conditions.add(OrCondition(builder.conditions))
        }
    }

    fun and(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        if (builder.conditions.isNotEmpty()) {
            conditions.add(AndCondition(builder.conditions))
        }
    }

    fun not(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        if (builder.conditions.isNotEmpty()) {
            conditions.add(NotCondition(AndCondition(builder.conditions)))
        }
    }

    fun build(): Condition {
        return when {
            conditions.isEmpty() -> NoCondition
            conditions.size == 1 -> conditions[0]
            else -> AndCondition(conditions)
        }
    }
}