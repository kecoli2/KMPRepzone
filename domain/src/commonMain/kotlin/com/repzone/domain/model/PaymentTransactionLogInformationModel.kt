package com.repzone.domain.model

data class PaymentTransactionLogInformationModel(
  val id: Long,
  val amount: Double?,
  val customerId: Long?,
  val direction: Long?,
  val documentGroup: Long?,
  val referenceId: Long?,
  val transactionDate: Long?,
)
