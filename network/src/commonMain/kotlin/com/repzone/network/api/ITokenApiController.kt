package com.repzone.network.api

import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.response.LoginResponse

interface ITokenApiController {
    suspend fun pushToken(tokenRequest: LoginRequest): Result<LoginResponse>
}