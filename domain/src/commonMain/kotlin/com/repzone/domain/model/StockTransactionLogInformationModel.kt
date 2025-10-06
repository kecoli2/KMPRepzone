package com.repzone.domain.model

data class StockTransactionLogInformationModel(
  val id: Long,
  val amount: Double?,
  val direction: Long?,
  val documentGroup: Long?,
  val productId: Long?,
  val referenceId: Long?,
  val transactionDate: Long?,
  val warehouseType: Long?,
)
