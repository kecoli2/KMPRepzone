package com.repzone.domain.model

data class SyncCustomerEmailModel(
  val id: Long,
  val companyName: String?,
  val customerId: Long?,
  val email: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val recordDateUtc: Long?,
  val state: Long?,
  val title: String?,
)
