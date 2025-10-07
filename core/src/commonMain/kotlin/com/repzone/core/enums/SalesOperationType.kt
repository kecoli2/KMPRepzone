package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class SalesOperationType {
    DRAFT, //Taslak
    SALES, //satış
    SALESRETURN, //satış iade
    PURCHASE, //alış
    PURCHASERETURN,  //alış iade
    WAREHOUSERECEIPT,//değişebilir
    COLLECTIONDEBIT6, //tahsilat borç
    COLLECTIONCREDIT7, //tahsilat alacak
    SALESDAMAGEDRETURN8, // Satis  hasarli iade
    PURCHASEDAMAGEDRETURN; // Alis  hasarli iade

    companion object {
        object Serializer : KSerializer<SalesOperationType> {
            override val descriptor = PrimitiveSerialDescriptor("SalesOperationType", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: SalesOperationType) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = SalesOperationType.entries[decoder.decodeInt()]
        }
    }
}