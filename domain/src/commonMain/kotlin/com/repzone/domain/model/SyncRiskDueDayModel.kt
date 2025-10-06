package com.repzone.domain.model

data class SyncRiskDueDayModel(
  val id: Long,
  val customerId: Long?,
  val lastInvoiceDate: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
)
