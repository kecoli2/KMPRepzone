package com.repzone.domain.model

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.IoType
import com.repzone.core.enums.SalesOperationType
import com.repzone.core.enums.StateType
import com.repzone.core.enums.WarehouseType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class SyncDocumentMapModel(
    val id: Long,
    val description: String?,
    val documentHeader: String?,
    val documentTypeGroup: DocumentActionType,
    val ioType: IoType,
    val isElectronicDocument: Boolean,
    val isFulfillment: Boolean,
    val lang: String?,
    val logoPathUrl: String?,
    val logoSelection: Int?,
    val minMaxControl: Boolean,
    val modificationDateUtc: Instant?,
    val name: String?,
    val note: String?,
    val operationType: SalesOperationType,
    val printerTemplatePath: String?,
    val printQrCode: Boolean,
    val recordDateUtc: Instant,
    val state: StateType,
    val uniqueIdCaption: String?,
    val useFinancialLogo: Boolean,
    val warehouseType: WarehouseType,
)
