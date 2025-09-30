package com.repzone.network.models.response

data class RefreshTokenResponse(val token: String, val expiresIn: Long, val refreshToken: String? = null)