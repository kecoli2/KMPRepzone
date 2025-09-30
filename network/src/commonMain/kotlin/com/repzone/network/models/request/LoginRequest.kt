package com.repzone.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LoginRequest(
    @SerialName("Email")
    val email: String,
    @SerialName("Password")
    val password: String,
    @SerialName("UniqueId")
    val uniqueId: String? = null,
    @SerialName("UseHashing")
    val useHashing: Boolean = false,
)