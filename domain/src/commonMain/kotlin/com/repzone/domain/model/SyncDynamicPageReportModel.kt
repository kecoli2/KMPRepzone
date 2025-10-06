package com.repzone.domain.model

data class SyncDynamicPageReportModel(
  val id: Long,
  val category: String?,
  val code: String?,
  val description: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val quickAccessOrder: Long?,
  val quickAccessShow: Long?,
  val recordDateUtc: Long?,
  val requested: Long?,
  val state: Long?,
)
