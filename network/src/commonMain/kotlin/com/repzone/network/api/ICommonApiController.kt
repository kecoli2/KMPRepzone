package com.repzone.network.api

import com.repzone.core.enums.NumberTemplateType
import com.repzone.network.http.wrapper.ApiResult

interface ICommonApiController {
    suspend fun getDocNumber(docGroupType: Int, docTypeId: Int, numberTemplateType: NumberTemplateType): ApiResult<String?>
}