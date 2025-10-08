package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class EntityModelType {
    NONE ,
    CUSTOMER,
    CUSTOMERADDRESS,
    CUSTOMERGROUP,
    PRODUCT,
    PRODUCTGROUP,
    PRODUCTUNIT,
    UNIT,
    BRAND,
    REPRESENTATIVE,
    REPRESENTATIVEGROUP,
    ROUTEAPPOINTMENT,
    ROUTEDEFINITION,
    ROUTESPRINT,
    ORDER,
    VISIT,
    FEED,
    FORMS,
    USER,
    TENANT,
    CUSTOMDATA;

    companion object {
        object Serializer : KSerializer<EntityModelType> {
            override val descriptor = PrimitiveSerialDescriptor("EntityModelType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: EntityModelType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = EntityModelType.entries[decoder.decodeInt()]
        }
    }
}