package com.repzone.domain.model

data class SyncCustomerGroupProductDistributionModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val mustStockListId: Long?,
  val organizationId: Long?,
  val pricePurchaseReturnDamagedListId: Long?,
  val priceSalesDamagedListId: Long?,
  val priceSalesDistributionListId: Long?,
  val priceSalesReturnDistributionListId: Long?,
  val productSalesDistributionListId: Long?,
  val productSalesReturnDistributionListId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
