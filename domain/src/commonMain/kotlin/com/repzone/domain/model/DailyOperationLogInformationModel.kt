package com.repzone.domain.model

import com.repzone.core.enums.DailyOperationType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class DailyOperationLogInformationModel(
  val id: Long,
  val batteryLevel: Long?,
  val date: Instant?,
  val description: String?,
  val localTime: Instant?,
  val repzoneLeaveRequestId: Long?,
  val repzoneLeaveRequestUniqueId: String?,
  val type: DailyOperationType,
){
  fun getFormatDescription(): String? {
     return description
       ?.split(",")
       ?.lastOrNull()
       ?: description
  }
}
