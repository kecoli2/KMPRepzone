package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class FormDocumentType {
    NONE,
    FORM,
    SURVEY,
    VISIT,
    ORDER;

    companion object {
        object Serializer : KSerializer<FormDocumentType> {
            override val descriptor = PrimitiveSerialDescriptor("FormDocumentType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: FormDocumentType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = FormDocumentType.entries[decoder.decodeInt()]
        }
    }
}