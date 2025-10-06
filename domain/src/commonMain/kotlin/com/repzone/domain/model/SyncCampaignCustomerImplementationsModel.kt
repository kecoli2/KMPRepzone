package com.repzone.domain.model

data class SyncCampaignCustomerImplementationsModel(
  val id: Long,
  val campaignMasterId: Long?,
  val count: Long?,
  val customerId: Long?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
