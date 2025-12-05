package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCampaignDynamicListOrganizationsModel(
  val id: Long,
  val campaignMasterId: Long?,
  val dynamicListId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
)
