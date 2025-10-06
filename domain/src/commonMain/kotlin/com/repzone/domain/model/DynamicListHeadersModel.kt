package com.repzone.domain.model

data class DynamicListHeadersModel(
  val id: Long,
  val description: String?,
  val listType: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val shareStatus: Long?,
  val state: Long?,
)
