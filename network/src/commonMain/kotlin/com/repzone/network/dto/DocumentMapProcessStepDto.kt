package com.repzone.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class DocumentMapProcessStepDto(
    val id: Int,
    val state: Int,
    val name: String? = null,
    val objectName: String? = null,
    val stepOrder: Int? = null
)
