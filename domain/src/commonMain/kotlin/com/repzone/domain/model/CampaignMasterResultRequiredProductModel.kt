package com.repzone.domain.model

data class CampaignMasterResultRequiredProductModel(
  val id: Long,
  val campaignMasterResultId: Long?,
  val modificationDateUtc: Long?,
  val productId: Long?,
  val quantity: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
