package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class ProductParameterv4Model(
  val id: Long,
  val color: String?,
  val ediCode: String?,
  val isVisible: Long?,
  val modificationDateUtc: Long?,
  val order: Long?,
  val organizationId: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
