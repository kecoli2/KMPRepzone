package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncProductImagesModel(
  val id: Long,
  val fileId: Long?,
  val imageUrl: String?,
  val modificationDateUtc: Long?,
  val productId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
)
