package com.repzone.domain.model

data class CustomerNoteSpModel(
  val id: Long,
  val beginDate: Long?,
  val customerId: Long?,
  val endDate: Long?,
  val isPrivate: Long?,
  val modificationDateUtc: Long?,
  val note: String?,
  val noteTypeId: Long?,
  val organizationId: Long?,
  val packageId: String?,
  val recordDateUtc: Long?,
  val state: Long?,
  val status: Long?,
  val userId: Long?,
  val userName: String?,
)
