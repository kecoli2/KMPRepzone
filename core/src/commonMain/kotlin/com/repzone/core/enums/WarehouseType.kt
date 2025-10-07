package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


enum class WarehouseType {
    MAIN,
    RETURN,
    DAMAGERETURN,
    EMPTY,
    EMPTY2;

    /*
    TODO("EMPTY2 LERI DUZENLE")
     */
    companion object {
        object Serializer : KSerializer<WarehouseType> {
            override val descriptor = PrimitiveSerialDescriptor("WarehouseType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: WarehouseType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = WarehouseType.entries[decoder.decodeInt()]
        }
    }
}