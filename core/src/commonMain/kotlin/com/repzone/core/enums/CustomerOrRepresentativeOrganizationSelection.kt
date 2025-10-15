package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

enum class CustomerOrRepresentativeOrganizationSelection {
    CUSTOMERORGANIZATION,
    REPRESENTATIVEORGANIZATION;

    companion object {
        object Serializer : KSerializer<CustomerOrRepresentativeOrganizationSelection> {
            override val descriptor = PrimitiveSerialDescriptor("CustomerOrRepresentativeOrganizationSelection", PrimitiveKind.INT)
            override fun serialize(encoder: Encoder, value: CustomerOrRepresentativeOrganizationSelection) = encoder.encodeInt(value.ordinal)
            override fun deserialize(decoder: Decoder) = CustomerOrRepresentativeOrganizationSelection.entries[decoder.decodeInt()]
        }
    }
}