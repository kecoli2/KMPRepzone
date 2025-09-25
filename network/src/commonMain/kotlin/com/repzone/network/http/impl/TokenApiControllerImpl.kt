package com.repzone.network.http.impl

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.request.RefreshTokenRequest
import com.repzone.network.models.response.LoginResponse
import com.repzone.network.models.response.RefreshTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TokenApiControllerImpl(private val client: HttpClient): ITokenApiController {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
/*    override suspend fun pushToken(token: String): Result<Unit> {
        client.post("/orders") { setBody(order) }.body<Unit>()
    }*/

    override suspend fun pushToken(tokenRequest: LoginRequest): ApiResult<LoginResponse> {
        return try{
            val response = client.post(ITokenApiControllerConstant.TOKEN_ENDPOINT) {
                setBody(tokenRequest)
                //contentType(ContentType.Application.Json)
            }
            ApiResult.Success(response.body<LoginResponse>())
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }

    override suspend fun refreshToken(tokenRequest: RefreshTokenRequest): ApiResult<RefreshTokenResponse> {
        return try{
            val response = client.post(ITokenApiControllerConstant.TOKEN_ENDPOINT) {
                setBody(tokenRequest)
                //contentType(ContentType.Application.Json)
            }
            ApiResult.Success(response.body<RefreshTokenResponse>())
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}