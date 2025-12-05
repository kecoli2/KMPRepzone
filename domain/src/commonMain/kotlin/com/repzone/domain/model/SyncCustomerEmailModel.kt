package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncCustomerEmailModel(
  val id: Long,
  val companyName: String?,
  val customerId: Long?,
  val email: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val recordDateUtc: Long?,
  val state: StateType,
  val title: String?,
)
