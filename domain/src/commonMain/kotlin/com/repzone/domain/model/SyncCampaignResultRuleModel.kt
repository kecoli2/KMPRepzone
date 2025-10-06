package com.repzone.domain.model

data class SyncCampaignResultRuleModel(
  val id: Long,
  val andDiscover: Double?,
  val andDiscoverLimit: Double?,
  val andDiscoverUnitId: Long?,
  val andIsEqual: Long?,
  val andIsQuantity: Long?,
  val calcOperator: Long?,
  val calcSource: Long?,
  val campaignMasterResultId: Long?,
  val discover: String?,
  val isOr: Long?,
  val isSingleRule: Long?,
  val modificationDateUtc: Long?,
  val operator: Long?,
  val property: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
