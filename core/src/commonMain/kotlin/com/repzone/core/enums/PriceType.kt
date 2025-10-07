package com.repzone.core.enums

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
enum class PriceType {
    DEFAULT,
    SALES,
    RETURN,
    DAMAGERETURN;
    companion object {
        object Serializer : KSerializer<PriceType> {
            override val descriptor = PrimitiveSerialDescriptor("PriceType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: PriceType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = PriceType.entries[decoder.decodeInt()]
        }
    }
}