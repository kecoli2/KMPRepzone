package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class FormVisibleOption {
    NONE ,
    VISITS,
    DAILYOPERATIONSSTARTDAY,
    DAILYOPERATIONSENDDAY;

    companion object {
        object Serializer : KSerializer<FormVisibleOption> {
            override val descriptor = PrimitiveSerialDescriptor("FormVisibleOption", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: FormVisibleOption) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = FormVisibleOption.entries[decoder.decodeInt()]
        }
    }
}