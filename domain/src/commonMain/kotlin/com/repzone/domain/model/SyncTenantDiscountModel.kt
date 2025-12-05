package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncTenantDiscountModel(
  val id: Long,
  val discountType: Long?,
  val isDeletable: Long?,
  val lineEffectScopeType: Long?,
  val maxPercent: Double?,
  val modificationDateUtc: Long?,
  val name: String?,
  val order: Long?,
  val recordDateUtc: Long?,
  val reserved: Long?,
  val scope: Long?,
  val state: StateType,
  val tenantId: Long?,
)
