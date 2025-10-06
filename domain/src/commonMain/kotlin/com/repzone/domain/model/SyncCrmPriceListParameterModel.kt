package com.repzone.domain.model

data class SyncCrmPriceListParameterModel(
  val id: Long,
  val entityId: Long?,
  val entityType: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val paymentPlanId: Long?,
  val pricePurchaseReturnDamagedListId: Long?,
  val priceSalesDamagedListId: Long?,
  val purchasePriceListId: Long?,
  val purchaseReturnPriceListId: Long?,
  val recordDateUtc: Long?,
  val salesPriceListId: Long?,
  val salesReturnPriceListId: Long?,
  val state: Long?,
)
