package com.repzone.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object InstantSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        /*return Instant.parse(decoder.decodeString())*/
        val raw = decoder.decodeString().trim()

        // "2025-11-13 14:23:27Z" -> "2025-11-13T14:23:27Z"
        var s = raw.replace(' ', 'T')

        // Eğer ne 'Z' ne de +HH:mm / -HH:mm içeriyorsa, UTC varsay (isteğe bağlı)
        val hasZoneOrOffset = s.endsWith("Z", ignoreCase = true) ||
                Regex("[+-]\\d{2}:?\\d{2}$").containsMatchIn(s)
        if (!hasZoneOrOffset) s += "Z"

        return Instant.parse(s)
    }
}