package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncRiskDueDayModel(
  val id: Long,
  val customerId: Long?,
  val lastInvoiceDate: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
