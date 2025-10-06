package com.repzone.domain.model

data class SyncFeedbackListModel(
  val id: Long,
  val categoryId: Long?,
  val categoryName: String?,
  val lastFeedbackDate: Long?,
  val message: String?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val organizationName: String?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val rootId: Long?,
  val state: Long?,
  val userId: Long?,
  val userName: String?,
)
