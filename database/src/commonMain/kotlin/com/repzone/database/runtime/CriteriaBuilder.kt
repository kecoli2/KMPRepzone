package com.repzone.database.runtime

class CriteriaBuilder {
    private val conditions = mutableListOf<Condition>()

    fun criteria(
        field: String,
        equal: Any? = null,
        notEqual: Any? = null,
        like: String? = null,
        notLike: String? = null,  // YENİ
        greaterThan: Any? = null,
        greaterThanOrEqual: Any? = null,  // YENİ
        lessThan: Any? = null,
        lessThanOrEqual: Any? = null,  // YENİ
        isNull: Boolean? = null,
        In: List<Any>? = null,
        notIn: List<Any>? = null,  // YENİ
        between: Pair<Any, Any>? = null  // YENİ
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
            notLike != null -> {  // YENİ
                conditions.add(FieldCondition(field, "NOT LIKE", notLike))
            }
            greaterThan != null -> {
                conditions.add(FieldCondition(field, ">", greaterThan))
            }
            greaterThanOrEqual != null -> {  // YENİ
                conditions.add(FieldCondition(field, ">=", greaterThanOrEqual))
            }
            lessThan != null -> {
                conditions.add(FieldCondition(field, "<", lessThan))
            }
            lessThanOrEqual != null -> {  // YENİ
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
            notIn != null -> {  // YENİ
                conditions.add(InCondition(field, notIn, true))
            }
            between != null -> {  // YENİ
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

    fun and(block: CriteriaBuilder.() -> Unit) {  // YENİ - explicit AND
        val builder = CriteriaBuilder()
        builder.block()
        if (builder.conditions.isNotEmpty()) {
            conditions.add(AndCondition(builder.conditions))
        }
    }

    // YENİ - NOT wrapper
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