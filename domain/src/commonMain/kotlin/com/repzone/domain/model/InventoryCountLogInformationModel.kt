package com.repzone.domain.model

data class InventoryCountLogInformationModel(
  val id: Long,
  val approValDate: Long?,
  val data: String?,
  val direction: Long?,
  val totalProductKind: Long?,
  val totalProductQuantity: Double?,
)
