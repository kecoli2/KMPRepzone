package com.repzone.domain.model

data class SyncPackageCustomFieldProductModel(
  val id: Long,
  val dataType: Long?,
  val dateMax: Long?,
  val dateMin: Long?,
  val decimalMax: Double?,
  val decimalMin: Double?,
  val defaultValue: String?,
  val description: String?,
  val fieldName: String?,
  val integerMax: Long?,
  val integerMin: Long?,
  val itemList: String?,
  val mandatory: Long?,
  val order: Long?,
  val packageId: Long?,
  val packageName: String?,
  val productId: Long?,
  val stringMax: Long?,
  val value: String?,
)
