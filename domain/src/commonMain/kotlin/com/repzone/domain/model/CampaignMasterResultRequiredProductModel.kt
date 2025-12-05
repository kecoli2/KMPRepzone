package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class CampaignMasterResultRequiredProductModel(
  val id: Long,
  val campaignMasterResultId: Long?,
  val modificationDateUtc: Long?,
  val productId: Long?,
  val quantity: Long?,
  val recordDateUtc: Long?,
  val state: StateType?,
)
