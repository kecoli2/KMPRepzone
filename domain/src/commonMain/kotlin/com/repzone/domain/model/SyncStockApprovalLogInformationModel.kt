package com.repzone.domain.model

data class SyncStockApprovalLogInformationModel(
  val id: Long,
  val approValDate: Long?,
  val data: String?,
  val referenceDocUniqueId: String?,
  val totalProductKind: Long?,
  val totalProductQuantity: Double?,
)
