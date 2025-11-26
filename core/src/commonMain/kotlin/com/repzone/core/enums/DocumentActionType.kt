package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class DocumentActionType {
    START_VISIT,
    ORDER,
    INVOICE,
    WAYBILL,
    WAREHOUSERECEIPT,
    COLLECTION,
    OTHER,
    FORM,
    END_VISIT,
    EMPTY;

    companion object {
        object Serializer : KSerializer<DocumentActionType> {
            override val descriptor = PrimitiveSerialDescriptor("DocumentTypeGroup", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: DocumentActionType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = DocumentActionType.entries[decoder.decodeInt()]
        }
    }
}