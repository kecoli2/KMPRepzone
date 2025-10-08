package com.repzone.network.dto.form

import kotlinx.serialization.Serializable

@Serializable
data class FormAttributeDto(
    var id: Int? = null,
    var key: String? = null,
    var value: String? = null,
    var alias: String? = null,
    var description: String? = null,
    var valueType: String? = null,
    var state: Int? = null
)
