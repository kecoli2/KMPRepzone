package com.repzone.domain.model

data class SyncEventReasonModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val name: String?,
  val reasonType: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tags: String?,
)
