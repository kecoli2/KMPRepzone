package com.repzone.domain.model

data class SyncCampaignOrganizationModel(
  val id: Long,
  val campaignMasterId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
