package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class VideoQuality {
    LOW,
    MEDIUM,
    HIGH;

    companion object {
        object Serializer : KSerializer<VideoQuality> {
            override val descriptor = PrimitiveSerialDescriptor("VideoQuality", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: VideoQuality) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = VideoQuality.entries[decoder.decodeInt()]
        }
    }
}