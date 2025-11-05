package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class NumberTemplateType {
        EMPTY,
        CODEORDOCNUMBER,
        SERIALNUMBER,
        ALTERNATIVE;

    companion object {
        object Serializer : KSerializer<NumberTemplateType> {
            override val descriptor = PrimitiveSerialDescriptor("NumberTemplateType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: NumberTemplateType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = NumberTemplateType.entries[decoder.decodeInt()]
        }
    }
}