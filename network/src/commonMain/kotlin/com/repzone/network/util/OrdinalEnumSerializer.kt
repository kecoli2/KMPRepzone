package com.repzone.network.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class OrdinalEnumSerializer<T : Enum<T>>(private val enumValues: Array<T>) : KSerializer<T> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("enum", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): T {
        val value = decoder.decodeInt()
        return enumValues[value]
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeInt(value.ordinal)
    }
}