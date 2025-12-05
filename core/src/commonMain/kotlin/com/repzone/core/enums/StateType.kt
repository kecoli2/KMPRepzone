package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class StateType {
    DRAFT,
    ACTIVE,
    EMPTY,
    PASSIVE,
    DELETED;

    companion object {
        object Serializer : KSerializer<StateType> {
            override val descriptor = PrimitiveSerialDescriptor("StateType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: StateType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = StateType.entries[decoder.decodeInt()]
        }
    }
}