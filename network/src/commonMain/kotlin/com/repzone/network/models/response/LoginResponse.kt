package com.repzone.network.models.response

import com.repzone.core.model.TokenResponseModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("TenantId")
    val tenantId: String,
    @SerialName("UserId")
    val userId: Int,
    @SerialName("RepresentativeId")
    val representativeId: Int,
    @SerialName("FirstName")
    val firstName: String?,
    @SerialName("LastName")
    val lastName: String?,
    @SerialName("Phone")
    val phone: String?,
    @SerialName("Email")
    val email: String,
    @SerialName("ProfileImageUrl")
    val profileImageUrl: String?,
    @SerialName("TokenResponse")
    val tokenResponse: TokenResponseModel? = null
)
