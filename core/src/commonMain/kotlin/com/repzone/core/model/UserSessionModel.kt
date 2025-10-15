package com.repzone.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserSessionModel(
    val userId: Int,
    val firstName: String? = null,
    val lastName: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val profileImageUrl: String? = null,
    var token: String? = null,
    var refreshToken: String? = null,
    var expiresAtEpochSeconds: Long? = null,

)
