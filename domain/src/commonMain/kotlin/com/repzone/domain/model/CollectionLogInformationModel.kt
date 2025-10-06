package com.repzone.domain.model

data class CollectionLogInformationModel(
  val id: Long,
  val collectionDate: Long?,
  val customerId: Long?,
  val documentUniqueId: String?,
  val note: String?,
  val restServiceTaskId: Long?,
  val sumTotal: Double?,
)
