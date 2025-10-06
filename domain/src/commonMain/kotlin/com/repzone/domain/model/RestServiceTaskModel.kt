package com.repzone.domain.model

data class RestServiceTaskModel(
  val id: Long,
  val actionName: String?,
  val callType: Long?,
  val createdTime: Long?,
  val methodPath: String?,
  val requestObject: String?,
  val requestObjectUniqueID: String?,
  val requestType: String?,
  val result: String?,
  val status: Long?,
)
