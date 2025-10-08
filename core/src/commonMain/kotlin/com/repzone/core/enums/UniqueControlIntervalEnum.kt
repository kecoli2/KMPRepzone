package com.repzone.core.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = UniqueControlIntervalEnumSerializer::class)
enum class UniqueControlIntervalEnum(val value: Int) {
    None(0),
    Daily(1),
    Weekly(2),
    Monthly(3),
    Quarter(4),
    Yearly(5),
    LifeTime(6),
    OneDay(11),
    TwoDays(12),
    ThreeDays(13),
    FourDays(14),
    FiveDays(15),
    SixDays(16),
    SevenDays(17),
    OneMonth(21),
    TwoMonths(22),
    ThreeMonths(23),
    FourMonths(24),
    FiveMonths(25),
    SixMonths(26),
    SevenMonths(27),
    EightMonths(28),
    NineMonths(29),
    TenMonths(30),
    ElevenMonths(31),
    TwelveMonths(32);

    companion object {
        fun fromValue(value: Int): UniqueControlIntervalEnum? {
            return entries.find { it.value == value }
        }
    }
}

object UniqueControlIntervalEnumSerializer : KSerializer<UniqueControlIntervalEnum> {
    override val descriptor = PrimitiveSerialDescriptor("UniqueControlIntervalEnum", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: UniqueControlIntervalEnum) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): UniqueControlIntervalEnum {
        val intValue = decoder.decodeInt()
        return UniqueControlIntervalEnum.fromValue(intValue)
            ?: throw SerializationException("Unknown UniqueControlIntervalEnum value: $intValue")
    }
}