package com.repzone.domain.model

data class StockLoadingDemandLogInformationModel(
  val id: Long,
  val recordDate: Long?,
  val restServiceTaskId: Long?,
  val totalProductKind: Long?,
  val totalProductQuantity: Double?,
)
