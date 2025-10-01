package com.repzone.domain.model

data class SyncModuleModel(
    var syncType: Long,
    var requestUrl: String?,
    var requestFilter: String?,
    var lastSyncDate: Long?,
    var requestType: RequestType? = null,
)

enum class RequestType {
    GET,
    POST,
    PUT,
    DELETE
}