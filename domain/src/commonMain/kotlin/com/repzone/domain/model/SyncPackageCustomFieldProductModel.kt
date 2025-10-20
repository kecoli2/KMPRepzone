package com.repzone.domain.model

import com.repzone.core.enums.CustomFieldDataType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class SyncPackageCustomFieldProductModel(
  val id: Long,
  val dataType: CustomFieldDataType? = null,
  val dateMin: Instant? = null,
  val dateMax: Instant? = null,
  val decimalMax: Double?,
  val decimalMin: Double?,
  val defaultValue: String?,
  val description: String?,
  val fieldName: String?,
  val integerMax: Long?,
  val integerMin: Long?,
  val itemList: String?,
  val mandatory: Boolean,
  val order: Long?,
  val packageId: Long?,
  val packageName: String?,
  val productId: Long?,
  val stringMax: Long?,
  val value: String?,
)
