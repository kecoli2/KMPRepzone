package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class FormOperationType {
    DEFAULT,
    VIRTUAL;

    companion object {
        object Serializer : KSerializer<FormOperationType> {
            override val descriptor = PrimitiveSerialDescriptor("FormOperationType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: FormOperationType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = FormOperationType.entries[decoder.decodeInt()]
        }
    }
}