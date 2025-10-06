package com.repzone.domain.model

data class SystemLogInformationModel(
  val id: Long,
  val message: String?,
  val recordDate: Long?,
  val region: String?,
)
