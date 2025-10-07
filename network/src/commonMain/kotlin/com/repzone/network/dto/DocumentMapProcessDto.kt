package com.repzone.network.dto

import com.repzone.core.enums.DocProcessType
import kotlinx.serialization.Serializable

@Serializable
data class DocumentMapProcessDto(
    val id: Int,
    val name: String? = null,
    @Serializable(with = DocProcessType.Companion.Serializer::class)
    val docProcessType: DocProcessType? = null,
    val steps: List<DocumentMapProcessStepDto>? = null
)
