package com.repzone.domain.model

data class SyncPaymentPlanModel(
  val id: Long,
  val code: String?,
  val ediCode: String?,
  val ids: String?,
  val isDefault: Long?,
  val modificationDateUtc: Long?,
  val name: String?,
  val recordDateUtc: Long?,
  val state: Long?,
  val tenantId: Long?,
)
