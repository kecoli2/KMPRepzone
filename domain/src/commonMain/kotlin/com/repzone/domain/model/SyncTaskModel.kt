package com.repzone.domain.model

data class SyncTaskModel(
  val id: Long,
  val beginDate: Long?,
  val customers: String?,
  val endDate: Long?,
  val modificationDateUtc: Long?,
  val note: String?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val status: Long?,
  val target: Long?,
  val targetGroupId: Long?,
  val targetTags: String?,
  val targetType: Long?,
  val taskSource: Long?,
  val title: String?,
)
