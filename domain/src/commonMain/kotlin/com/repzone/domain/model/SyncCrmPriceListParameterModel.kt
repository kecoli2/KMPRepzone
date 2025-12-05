package com.repzone.domain.model

import com.repzone.core.enums.CrmParameterEntityType
import com.repzone.core.enums.StateType

data class SyncCrmPriceListParameterModel(
  val id: Long,
  val entityId: Long?,
  val entityType: CrmParameterEntityType,
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
  val state: StateType,
)
