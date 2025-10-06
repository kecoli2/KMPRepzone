package com.repzone.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PackageCustomFieldDto(
    val id: Int,
    val name: String? = null,
    val packageId: Int,
    val packageName: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val isActive: Boolean,
    val fields: List<PackageCustomFieldProductDto> = emptyList()
)
