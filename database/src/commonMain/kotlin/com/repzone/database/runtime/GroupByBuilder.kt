package com.repzone.database.runtime

class GroupByBuilder {
    internal val groupByFields = mutableListOf<String>()
    internal var havingCondition: Condition = NoCondition

    fun groupBy(vararg fields: String) {
        groupByFields.addAll(fields)
    }

    fun having(block: CriteriaBuilder.() -> Unit) {
        val builder = CriteriaBuilder()
        builder.block()
        havingCondition = builder.build()
    }
}