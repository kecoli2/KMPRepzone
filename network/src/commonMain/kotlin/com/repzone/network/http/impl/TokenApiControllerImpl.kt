package com.repzone.network.http.impl

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.network.api.ITokenApiController
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.response.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

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

    override suspend fun pushToken(tokenRequest: LoginRequest): Result<LoginResponse> {
        return runCatching {
            client.post(ITokenApiControllerConstant.Token_EndPoint) { setBody(tokenRequest) }.body<LoginResponse>()
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}