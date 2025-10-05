package com.repzone.core.enums

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
enum class AddressType {
    MAIL,
    EMPTY1,
    BILL,
    EMPTY2,
    SHIPPING;

    companion object {
        object Serializer : KSerializer<AddressType> {
            override val descriptor = PrimitiveSerialDescriptor("AddressType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: AddressType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = entries[decoder.decodeInt()]
        }
    }
}