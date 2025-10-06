package com.repzone.domain.model

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
  val state: Long?,
)
