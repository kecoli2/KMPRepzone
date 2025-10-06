package com.repzone.domain.model

data class DailyOperationLogInformationModel(
  val id: Long,
  val batteryLevel: Long?,
  val date: Long?,
  val description: String?,
  val localTime: Long?,
  val repzoneLeaveRequestId: Long?,
  val repzoneLeaveRequestUniqueId: String?,
  val type: Long?,
)
