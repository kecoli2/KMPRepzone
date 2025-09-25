package com.repzone.network.api

import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.request.RefreshTokenRequest
import com.repzone.network.models.response.LoginResponse
import com.repzone.network.models.response.RefreshTokenResponse

interface ITokenApiController {
    suspend fun pushToken(tokenRequest: LoginRequest): ApiResult<LoginResponse>
    suspend fun refreshToken(tokenRequest: RefreshTokenRequest): ApiResult<RefreshTokenResponse>
}