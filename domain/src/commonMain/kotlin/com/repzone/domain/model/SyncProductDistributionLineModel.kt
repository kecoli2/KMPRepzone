package com.repzone.domain.model

data class SyncProductDistributionLineModel(
  val id: Long,
  val color: String?,
  val displayOrder: Long?,
  val distributionId: Long?,
  val modificationDateUtc: Long?,
  val mustHave: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
