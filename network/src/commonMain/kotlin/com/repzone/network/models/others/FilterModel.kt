package com.repzone.network.models.others

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterModel(
    @SerialName("Field")
    var field: String? = null,
    @SerialName("Value")
    var values: String? = null,
    @SerialName("Operator")
    var operator: FilterOperator
)

enum class FilterOperator{
    Equals,                 //=
    NotEquals,              // !=
    LessThan,               // <
    GreaterThan,            // >
    LessThanOrEqualTo,      // <=
    GreaterThanEqualTo,     // >=
    StartWith6,              // 'abc%'
    EndWith7,                // '%abc'
    Contains,               // in (1,2,3 )
    NotContains,            // not in (1,2,3 )
    IsNull,                // IS NULL
    IsNotNull              // IS NOT NULL
}