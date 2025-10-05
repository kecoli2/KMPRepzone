package com.repzone.core.enums

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
enum class MonitoringActionType {
    DONOTHING,
    STOPACTION,
    GIVEWARNING;

    companion object {
        object Serializer : KSerializer<MonitoringActionType> {
            override val descriptor = PrimitiveSerialDescriptor("MonitoringActionType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: MonitoringActionType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = MonitoringActionType.entries[decoder.decodeInt()]
        }
    }
}