package com.repzone.domain.model

import com.repzone.core.enums.StateType

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
  val state: StateType,
  val title: String?,
)
