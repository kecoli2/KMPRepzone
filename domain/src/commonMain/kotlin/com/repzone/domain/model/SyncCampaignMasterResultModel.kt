package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCampaignMasterResultModel(
  val id: Long,
  val campaignMasterId: Long?,
  val discount: Double?,
  val discountIndex: Long?,
  val isOrGroup: String?,
  val isPercent: Long?,
  val modificationDateUtc: Long?,
  val productBrandId: Long?,
  val productCustomFieldId: Long?,
  val productDynamicListId: Long?,
  val productGroupId: Long?,
  val productId: Long?,
  val productTag: String?,
  val promotionGroup: String?,
  val promotionGroupIndex: Long?,
  val promotionQuantityLimit: Long?,
  val promotionType: Long?,
  val quantity: Double?,
  val recordDateUtc: Long?,
  val resultType: Long?,
  val state: StateType,
  val title: String?,
  val unitId: Long?,
)
