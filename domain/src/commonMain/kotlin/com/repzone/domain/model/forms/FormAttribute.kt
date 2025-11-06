package com.repzone.domain.model.forms

data class FormAttribute(
    var id: Int = 0,
    var key: String? = null,
    var value: String? = null,
    var alias: String? = null,
    var description: String? = null,
    var valueType: String? = null,
    var state: Int = 0
)
