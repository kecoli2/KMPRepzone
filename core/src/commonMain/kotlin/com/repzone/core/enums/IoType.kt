package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class IoType {
    EMPTY,
    INPUT,
    OUTPUT;
    companion object {
        object Serializer : KSerializer<IoType> {
            override val descriptor = PrimitiveSerialDescriptor("IoType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: IoType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = IoType.entries[decoder.decodeInt()]
        }
    }
}