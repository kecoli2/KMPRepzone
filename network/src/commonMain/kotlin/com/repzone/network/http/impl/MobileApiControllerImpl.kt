package com.repzone.network.http.impl

import com.repzone.core.constant.IMobileApiControllerConstant
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.NumberTemplateType
import com.repzone.network.api.IMobileApiController
import com.repzone.network.dto.LastDocumentModelDto
import com.repzone.network.http.extensions.safeDelete
import com.repzone.network.http.extensions.safeGet
import com.repzone.network.http.extensions.toApiException
import com.repzone.network.http.wrapper.ApiResult
import io.ktor.client.HttpClient

class MobileApiControllerImpl(private val client: HttpClient): IMobileApiController {
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

    override suspend fun getLastDocNumbers(docGroupType: Int, docTypeId: Int): ApiResult<List<LastDocumentModelDto>> {
        return  try {
            val response = client.safeGet<List<LastDocumentModelDto>>(IMobileApiControllerConstant.LAST_DOC_NUMBERS){
                url {
                    parameters.append("docGroupType", docGroupType.toString())
                    parameters.append("docTypeId", docTypeId.toString())
                }
            }
            response
        }
        catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }

    override suspend fun deleteDocumentFromAPI(documentGroup: DocumentActionType, documentUniqueId: String): ApiResult<String?> {
        return  try {
            val response = client.safeDelete<String?>(IMobileApiControllerConstant.DELETE_DOCUMENT){
                url {
                    parameters.append("documentTypeGroup", documentGroup.ordinal.toString())
                    parameters.append("documentUniqueId", documentUniqueId)
                }
            }
            response
        }
        catch (e: Exception){
            ApiResult.Error(e.toApiException())
        }
    }
    //endregion

}