package com.repzone.domain.model

data class SellerNoteViewModel(
  val customerNoteId: Long,
  val noteTaker: String?,
  val beginDate: Long?,
  val endDate: Long?,
  val status: Long?,
  val noteDescription: String?,
  val customerCode: String?,
  val customerName: String?,
  val noteType: String?,
  val customerId: Long?,
)
