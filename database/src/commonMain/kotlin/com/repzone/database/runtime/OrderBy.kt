package com.repzone.database.runtime

enum class OrderDirection {
    ASC,
    DESC
}

data class OrderSpec(
    val field: String,
    val direction: OrderDirection
)

class OrderByBuilder {
    private val orders = mutableListOf<OrderSpec>()

    fun order(field: String, desc: Boolean = false) {
        orders.add(
            OrderSpec(
                field = field,
                direction = if (desc) OrderDirection.DESC else OrderDirection.ASC
            )
        )
    }

    internal fun build(): List<OrderSpec> = orders
}