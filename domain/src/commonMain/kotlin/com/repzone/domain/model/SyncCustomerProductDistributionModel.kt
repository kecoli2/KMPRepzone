package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCustomerProductDistributionModel(
  val id: Long,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val pricePurchaseReturnDamagedListId: Long?,
  val priceSalesDamagedListId: Long?,
  val priceSalesDistributionListId: Long?,
  val priceSalesReturnDistributionListId: Long?,
  val productSalesDistributionListId: Long?,
  val productSalesReturnDistributionListId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
