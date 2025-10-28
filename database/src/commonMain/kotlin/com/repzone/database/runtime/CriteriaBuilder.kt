package com.repzone.database.runtime

class CriteriaBuilder {
    //region Field
    private val conditions = mutableListOf<Condition>()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    fun criteria(
        field: String,
        equal: Any? = null,
        notEqual: Any? = null,
        like: String? = null,
        In: List<Any?>? = null,
        notIn: List<Any?>? = null,
        greaterThan: Any? = null,
        lessThan: Any? = null,
        greaterThanOrEqual: Any? = null,
        lessThanOrEqual: Any? = null,
        isNull: Boolean? = null
    ) {
        val condition = when {
            equal != null -> Equals(field, equal)
            notEqual != null -> NotEquals(field, notEqual)
            like != null -> Like(field, like)
            In != null -> In(field, In)
            notIn != null -> NotIn(field, notIn)
            greaterThan != null -> GreaterThan(field, greaterThan)
            lessThan != null -> LessThan(field, lessThan)
            greaterThanOrEqual != null -> GreaterThanOrEqual(field, greaterThanOrEqual)
            lessThanOrEqual != null -> LessThanOrEqual(field, lessThanOrEqual)
            isNull == true -> IsNull(field)
            isNull == false -> IsNotNull(field)
            else -> error("No condition specified for field '$field'")
        }
        conditions.add(condition)
    }

    fun and(block: CriteriaBuilder.() -> Unit) {
        val subBuilder = CriteriaBuilder()
        subBuilder.block()
        conditions.add(AndGroup(subBuilder.conditions))
    }

    fun or(block: CriteriaBuilder.() -> Unit) {
        val subBuilder = CriteriaBuilder()
        subBuilder.block()
        conditions.add(OrGroup(subBuilder.conditions))
    }

    internal fun build(): Condition {
        return when {
            conditions.isEmpty() -> NoCondition
            conditions.size == 1 -> conditions[0]
            else -> AndGroup(conditions)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}