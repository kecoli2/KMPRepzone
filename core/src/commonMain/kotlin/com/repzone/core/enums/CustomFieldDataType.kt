package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class CustomFieldDataType {
    EMPTY1,
    STRING,
    INTEGER,
    BOOLEAN,
    DECIMAL,
    DATETIME,
    SELECTIONLIST,
    EMPTY2;

    companion object {
        object Serializer : KSerializer<CustomFieldDataType> {
            override val descriptor = PrimitiveSerialDescriptor("CustomFieldDataType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: CustomFieldDataType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = CustomFieldDataType.entries[decoder.decodeInt()]
        }
    }
}