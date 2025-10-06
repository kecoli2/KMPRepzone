package com.repzone.domain.model

data class SMSValidationLogInformationModel(
  val id: Long,
  val data: String?,
  val requestTime: Long?,
  val responseTime: Long?,
  val result: String?,
  val success: Long?,
  val verified: Long?,
)
