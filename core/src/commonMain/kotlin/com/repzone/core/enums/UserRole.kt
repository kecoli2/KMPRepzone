package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class UserRole {
    SALES_REP,
    DISTRIBUTION;

    companion object {
        object Serializer : KSerializer<UserRole> {
            override val descriptor = PrimitiveSerialDescriptor("UserRole", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: UserRole) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = UserRole.entries[decoder.decodeInt()]
        }
    }
}