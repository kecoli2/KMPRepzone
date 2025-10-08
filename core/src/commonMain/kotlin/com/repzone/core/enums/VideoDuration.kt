package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = VideoDurationSerializer::class)
enum class VideoDuration(val value: Int) {
    Default(10),
    Max(30);

    companion object {
        fun fromValue(value: Int): VideoDuration? {
            return entries.find { it.value == value }
        }
    }
}

object VideoDurationSerializer : KSerializer<VideoDuration> {
    override val descriptor = PrimitiveSerialDescriptor("VideoDuration", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: VideoDuration) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): VideoDuration {
        val intValue = decoder.decodeInt()
        return VideoDuration.fromValue(intValue)
            ?: throw SerializationException("Unknown VideoDuration value: $intValue")
    }
}