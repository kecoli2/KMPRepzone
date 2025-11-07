package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class DocumentTypeGroup {
    EMPTY,
    ORDER,
    INVOICE,
    DISPATCH,
    WAREHOUSERECEIPT,
    COLLECTION,
    OTHER,
    FORM;

    companion object {
        object Serializer : KSerializer<DocumentTypeGroup> {
            override val descriptor = PrimitiveSerialDescriptor("DocumentTypeGroup", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: DocumentTypeGroup) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = DocumentTypeGroup.entries[decoder.decodeInt()]
        }
    }
}