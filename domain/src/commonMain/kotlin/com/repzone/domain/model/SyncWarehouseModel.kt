package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncWarehouseModel(
  val id: Long,
  val mobileCloseToDamagedReturns: Long?,
  val mobileCloseToReturns: Long?,
  val mobileCloseToSales: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val organizationName: String?,
  val recordDateUtc: Long?,
  val state: StateType,
)
