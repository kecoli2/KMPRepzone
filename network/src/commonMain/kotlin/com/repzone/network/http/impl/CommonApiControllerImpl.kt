package com.repzone.network.http.impl

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.core.enums.NumberTemplateType
import com.repzone.core.model.RepresentativeMobileIdentityModel
import com.repzone.network.api.ICommonApiController
import com.repzone.network.http.extensions.safeGet
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import io.ktor.client.HttpClient

class CommonApiControllerImpl(private val client: HttpClient): ICommonApiController {
    //region Public Method
    override suspend fun getDocNumber(docGroupType: Int, docTypeId: Int, numberTemplateType: NumberTemplateType): ApiResult<String?> {
        return  try {
            /*val response = client.safeGet<String?>(ITokenApiControllerConstant.TOKEN_INFO){
                url {
                    parameters.append("tokenType", tokenType)
                    parameters.append("tokenCode", tokenCode)
                }
            }
            response*/
            ApiResult.Success(null)
        }catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }
    //endregion

}