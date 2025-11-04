package com.repzone.domain.model

data class CustomerItemViewModel(
  val customerId: Long,
  val visitId: Long?,
  val iconIndex: Long?,
  val finishDate: Long?,
  val appointmentId: Long,
  val date: Long?,
  val tagRaw: String?,
  val name: String?,
  val customerCode: String?,
  val customerGroupName: String?,
  val address: String?,
  val latitude: Double?,
  val longitude: Double?,
  val addressType: Long?,
  val imageUri: String?,
  val parentCustomerId: Long?,
  val endDate: Long?,
  val customerBlocked: Long?,
  val sprintId: Long?,
  val showDisplayClock: Long,
)
