package com.repzone.domain.model

data class CrashLogInformationModel(
  val id: Long,
  val level: Long?,
  val logDate: Long?,
  val logMessage: String?,
)
