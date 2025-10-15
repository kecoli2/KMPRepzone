package com.repzone.core.model

import com.repzone.core.enums.ThemeType
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
    var tokenType: String,
    var refreshToken: String? = null,
    var expiresAtEpochSeconds: Long? = null,
    var themeId: ThemeType = ThemeType.DEFAULT,
    var identity: RepresentativeMobileIdentityModel? = null
)
