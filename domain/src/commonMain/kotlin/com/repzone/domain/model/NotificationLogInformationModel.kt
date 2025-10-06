package com.repzone.domain.model

data class NotificationLogInformationModel(
  val id: Long,
  val body: String?,
  val eventSource: String?,
  val eventType: String?,
  val footer: String?,
  val header: String?,
  val image: String?,
  val logDate: Long?,
)
