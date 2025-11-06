package com.repzone.domain.model.forms


data class ColumnControl(
    var id: Int = 0,
    var name: String? = null,
    var caption: String? = null,
    var controlType: String? = null,
    var controlValueType: String? = null,
    var returnValue: String? = null,
    var controlText: String? = null,
    var signPoints: List<String> = emptyList()
)
