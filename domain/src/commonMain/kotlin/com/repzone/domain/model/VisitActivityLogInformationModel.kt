package com.repzone.domain.model

data class VisitActivityLogInformationModel(
  val id: Long,
  val actionDate: Long?,
  val actionType: Long?,
  val visitActivityId: Long?,
  val visitId: Long?,
)
