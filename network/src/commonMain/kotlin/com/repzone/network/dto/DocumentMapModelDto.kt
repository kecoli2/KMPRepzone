package com.repzone.network.dto

import com.repzone.core.enums.*
import com.repzone.core.util.InstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class DocumentMapModelDto(
    val id : Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = IoType.Companion.Serializer::class)
    val ioType: IoType? = null,
    val isElectronicDocument: Boolean,
    @Serializable(with = DocumentTypeGroup.Companion.Serializer::class)
    val documentTypeGroup: DocumentTypeGroup? = null,
    @Serializable(with = WarehouseType.Companion.Serializer::class)
    val warehouseType: WarehouseType? = null,
    val minMaxControl: Boolean,
    @Serializable(with = SalesOperationType.Companion.Serializer::class)
    val operationType: SalesOperationType? = null,
    val process: List<DocumentMapProcessDto>? = null
)
