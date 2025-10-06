package com.repzone.domain.model

data class ShipmentActionLogInformationModel(
  val id: Long,
  val actionDate: Long?,
  val actionType: Long?,
  val description: String?,
  val reasonId: Long?,
  val shippingDocumentId: String?,
  val shippingPlanId: Long?,
)
