package com.repzone.domain.model

data class SyncMessageUserModel(
  val id: Long,
  val cellPhone: String?,
  val email: String?,
  val firstName: String?,
  val lastName: String?,
  val modificationDateUtc: Long?,
  val organizationId: Long?,
  val organizationName: String?,
  val photoPath: String?,
  val recordDateUtc: Long?,
  val roleId: Long?,
  val roleName: String?,
  val state: Long?,
)
