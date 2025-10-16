package com.repzone.network.http.impl

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.core.model.RepresentativeMobileIdentityModel
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.extensions.safeGet
import com.repzone.network.http.extensions.safePost
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.request.RefreshTokenRequest
import com.repzone.network.models.response.LoginResponse
import com.repzone.network.models.response.RefreshTokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TokenApiControllerImpl(private val client: HttpClient): ITokenApiController {
    //region Public Method

    override suspend fun pushToken(tokenRequest: LoginRequest): ApiResult<LoginResponse> {
        return try{
            val response = client.safePost<LoginResponse>(ITokenApiControllerConstant.TOKEN_ENDPOINT) {
                setBody(tokenRequest)
            }
            response
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

    override suspend fun verifyIdentity(tokenType: String, tokenCode: String): ApiResult<RepresentativeMobileIdentityModel> {
        return  try {
            val response = client.safeGet<RepresentativeMobileIdentityModel>(ITokenApiControllerConstant.TOKEN_INFO){
                url {
                    parameters.append("tokenType", tokenType)
                    parameters.append("tokenCode", tokenCode)
                }
            }
           response
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }
    //endregion


}