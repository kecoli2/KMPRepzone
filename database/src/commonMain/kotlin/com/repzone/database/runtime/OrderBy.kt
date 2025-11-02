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
    internal val orderSpecs = mutableListOf<OrderSpec>()  // Bu satır var mı?

    fun order(field: String, desc: Boolean = false) {
        orderSpecs.add(
            OrderSpec(
                field = field.escapeIfReserved(),
                direction = if (desc) OrderDirection.DESC else OrderDirection.ASC
            )
        )
    }
}