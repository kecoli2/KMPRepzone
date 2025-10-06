package com.repzone.domain.model

data class SyncCampaignMasterModel(
  val id: Long,
  val documentTypes: String?,
  val end: Long?,
  val groupKey: String?,
  val groupPriority: Long?,
  val implementationCalcType: Long?,
  val implementationType: Long?,
  val isOptional: Long?,
  val maxImplementationAmount: Double?,
  val maxImplementationCount: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val start: Long?,
  val state: Long?,
  val title: String?,
)
