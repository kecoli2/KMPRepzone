package com.repzone.domain.model

import com.repzone.core.enums.DocProcessType

data class SyncDocumentMapProcessModel(
  val id: Long,
  val docProcessType: DocProcessType,
  val documentMapName: String?,
  val name: String?,
)
