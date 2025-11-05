package com.repzone.domain.model

import com.repzone.core.enums.PrinterDeviceType

data class PrinterDocumentRelationInformationModel(
  val documentMapId: Long,
  val paperWidth: Long?,
  val printerAddress: String?,
  val printerName: String?,
  val printerType: PrinterDeviceType,
  val state: Long?,
)
