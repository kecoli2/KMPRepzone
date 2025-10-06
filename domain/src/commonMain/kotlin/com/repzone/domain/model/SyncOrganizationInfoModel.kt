package com.repzone.domain.model

data class SyncOrganizationInfoModel(
  val id: Long,
  val city: String?,
  val country: String?,
  val district: String?,
  val faxNumber: String?,
  val isEOrganization: Long?,
  val logoPath: String?,
  val name: String?,
  val phoneNumber: String?,
  val postalCode: String?,
  val street1: String?,
  val street2: String?,
  val title: String?,
)
