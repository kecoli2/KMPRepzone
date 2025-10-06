package com.repzone.domain.model

data class SyncCampaignDynamicListOrganizationsModel(
  val id: Long,
  val campaignMasterId: Long?,
  val dynamicListId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
