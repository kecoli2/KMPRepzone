package com.repzone.core.model

import repzonemobile.core.generated.resources.Res

data class PrinterListItem(val printerAdress: String,
                           val printerName: String,
                            val availableDocumentTypes: Res.string)
