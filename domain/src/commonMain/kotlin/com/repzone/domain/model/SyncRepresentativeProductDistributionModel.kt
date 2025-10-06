package com.repzone.domain.model

data class SyncRepresentativeProductDistributionModel(
  val id: Long,
  val groupId: Long?,
  val groupSalesDistributionListId: Long?,
  val groupSalesReturnDistributionListId: Long?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val salesDistributionListId: Long?,
  val salesReturnDistributionListId: Long?,
  val state: Long?,
)
