package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncAppListModel(
  val id: Long,
  val applicationId: Long?,
  val clientKey: String?,
  val clientSecretKey: String?,
  val code: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val organizationId: Long?,
  val recordDateUtc: Long?,
  val state: StateType,
  val tenantId: Long?,
  val url: String?,
)
