package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCustomerSegmentClassificationModel(
  val id: Long,
  val classificationId: Long?,
  val classificationName: String?,
  val customerId: Long?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val shareStatus: Long?,
  val state: StateType,
)
