package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class DocProcessType {
    EMPTY,
    INSERT,
    UPDATE,
    DELETE,
    CANCEL,
    COPY,
    REVERSE,
    COPYCANCEL;

    companion object {
        object Serializer : KSerializer<DocProcessType> {
            override val descriptor = PrimitiveSerialDescriptor("DocProcessType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: DocProcessType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = DocProcessType.entries[decoder.decodeInt()]
        }
    }
}