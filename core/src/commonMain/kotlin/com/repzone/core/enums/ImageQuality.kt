package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ImageQualitySerializer::class)
enum class ImageQuality(val value: Int) {
    XSmall(10),
    Small(25),
    Medium(50),
    Large(75),
    Original(100);

    companion object {
        fun fromValue(value: Int): ImageQuality? {
            return entries.find { it.value == value }
        }
    }
}

object ImageQualitySerializer : KSerializer<ImageQuality> {
    override val descriptor = PrimitiveSerialDescriptor("ImageQuality", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: ImageQuality) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): ImageQuality {
        val intValue = decoder.decodeInt()
        return ImageQuality.fromValue(intValue)
            ?: throw SerializationException("Unknown ImageQuality value: $intValue")
    }
}