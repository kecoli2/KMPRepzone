package com.repzone.network.models.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LoginRequest(
    @SerialName("user_id")
    val userName: String,
    @SerialName("password")
    val password: String
)
