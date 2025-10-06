package com.repzone.domain.model

data class SyncCampaignMasterRuleModel(
  val id: Long,
  val campaignMasterId: Long?,
  val discover: String?,
  val isOr: Long?,
  val modificationDateUtc: Long?,
  val operator: Long?,
  val property: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
