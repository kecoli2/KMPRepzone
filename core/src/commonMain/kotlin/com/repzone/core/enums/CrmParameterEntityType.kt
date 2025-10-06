package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class CrmParameterEntityType {
    CUSTOMER,
    CUSTOMER_GROUP;

    companion object {
        object Serializer : KSerializer<CrmParameterEntityType> {
            override val descriptor = PrimitiveSerialDescriptor("CrmParameterEntityType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: CrmParameterEntityType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = CrmParameterEntityType.entries[decoder.decodeInt()]
        }
    }
}