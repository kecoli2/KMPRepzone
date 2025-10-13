package com.repzone.core.model

import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class TokenResponseModel(
    @SerialName("AccessToken")
    val accessToken: String,
    @SerialName("TokenType")
    val tokenType: String,
    @Serializable(with = InstantSerializer::class)
    @SerialName("ExpiresIn")
    val expiresIn: Instant? = null,
    @SerialName("FirstName")
    val firstName: String? = null,
    @SerialName("LastName")
    val lastName: String? = null,
    @SerialName("Language")
    val language: String? = null,
    @SerialName("Phone")
    val phone: String? = null,
    @SerialName("Role")
    val role: String? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("IssuedAt")
    val issuedAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    @SerialName("ExpiresAt")
    val expiresAt: Instant? = null,
    @SerialName("UserName")
    val userName: String? = null,
    @SerialName("Email")
    val email: String? = null,
    @SerialName("FirstLogin")
    val firstLogin: Boolean,
    @SerialName("UserId")
    val userId: Int,
    @SerialName("PhotoPath")
    val photoPath: String? = null,
    @SerialName("IsAuthSuccessful")
    val isAuthSuccessful: Boolean,
    @SerialName("NeedToChangePassword")
    val needToChangePassword: Boolean,
    @SerialName("TwoFactorVerifyRequired")
    val twoFactorVerifyRequired: Boolean,
    @SerialName("TwoFactorType")
    val twoFactorType: Int,
    @SerialName("TenantId")
    val tenantId: Int,
    @SerialName("RepresentativeId")
    val representativeId: Int,
    @SerialName("OrganizationId")
    val organizationId: Int
)