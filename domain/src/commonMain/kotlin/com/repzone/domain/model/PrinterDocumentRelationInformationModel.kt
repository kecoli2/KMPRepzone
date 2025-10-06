package com.repzone.domain.model

data class PrinterDocumentRelationInformationModel(
  val documentMapId: Long,
  val paperWidth: Long?,
  val printerAddress: String?,
  val printerName: String?,
  val printerType: Long?,
  val state: Long?,
)
