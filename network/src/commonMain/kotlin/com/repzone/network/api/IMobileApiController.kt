package com.repzone.network.api

import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.NumberTemplateType
import com.repzone.network.dto.LastDocumentModelDto
import com.repzone.network.http.wrapper.ApiResult

interface IMobileApiController {
    suspend fun getDocNumber(docGroupType: Int, docTypeId: Int, numberTemplateType: NumberTemplateType): ApiResult<String?>
    suspend fun getLastDocNumbers(docGroupType: Int, docTypeId: Int): ApiResult<List<LastDocumentModelDto>>
    suspend fun deleteDocumentFromAPI(documentGroup: DocumentTypeGroup, documentUniqueId: String): ApiResult<String?>
}