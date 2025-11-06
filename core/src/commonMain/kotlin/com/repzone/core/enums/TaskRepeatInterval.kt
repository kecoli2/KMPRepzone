package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class TaskRepeatInterval {
    NONE,
    ONE_TIME,
    ATVISITSTART,
    WEEK,
    TWO_WEEK,
    MONTH,
    EVERY_VISIT;

    companion object {
        object Serializer : KSerializer<TaskRepeatInterval> {
            override val descriptor = PrimitiveSerialDescriptor("TaskRepeatInterval", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: TaskRepeatInterval) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = TaskRepeatInterval.entries[decoder.decodeInt()]
        }
    }
}