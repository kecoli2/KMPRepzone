package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncProductPricesModel(
  val id: Long,
  val begin: Long?,
  val description: String?,
  val end: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val parentId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
)
