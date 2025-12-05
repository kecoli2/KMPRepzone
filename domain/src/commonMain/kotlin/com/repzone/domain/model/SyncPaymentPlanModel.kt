package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncPaymentPlanModel(
  val id: Long,
  val code: String?,
  val ediCode: String?,
  val ids: String?,
  val isDefault: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
)
