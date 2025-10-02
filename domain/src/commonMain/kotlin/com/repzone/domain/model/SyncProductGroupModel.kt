package com.repzone.domain.model

data class SyncProductGroupModel(
    val id: Long,
    val iconIndex: Long?,
    val modificationDateUtc: Long?,
    val name: String?,
    val organizationId: Long?,
    val parentId: Long?,
    val photoPath: String?,
    val recordDateUtc: Long?,
    val shared: Long?,
    val state: Long?,
)
