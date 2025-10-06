package com.repzone.domain.model

data class SyncDocumentOrganizationModel(
  val id: Long,
  val documentHeader: String?,
  val documentTypeId: Long?,
  val lang: String?,
  val logoPathUrl: String?,
  val logoSelection: Long?,
  val minMaxControl: Long?,
  val modificationDateUtc: Long?,
  val note: String?,
  val organizationId: Long?,
  val printerTemplateAbsoulutePath: String?,
  val printerTemplatePath: String?,
  val printQrCode: Long?,
  val recordDateUtc: Long?,
  val state: Long?,
  val uniqueIdCaption: String?,
  val useFinancialLogo: Long?,
)
