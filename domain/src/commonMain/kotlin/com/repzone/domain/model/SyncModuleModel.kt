package com.repzone.domain.model

import com.repzone.core.enums.UIModule

data class SyncModuleModel(
    var syncType: Long,
    var requestUrl: String?,
    var requestFilter: String?,
    var lastSyncDate: Long?,
    var requestType: RequestType? = null,
    var moduleType: UIModule? = null,
)

enum class RequestType {
    GET,
    POST,
    PUT,
    DELETE
}