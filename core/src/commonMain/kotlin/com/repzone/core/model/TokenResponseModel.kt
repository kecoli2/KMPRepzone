package com.repzone.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseModel(
    @SerialName("AccessToken")
    val accessToken: String,
    @SerialName("TokenType")
    val tokenType: String,
    @SerialName("Email")
    val email: String
)