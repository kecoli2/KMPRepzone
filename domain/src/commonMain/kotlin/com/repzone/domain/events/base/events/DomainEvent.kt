package com.repzone.domain.events.base.events

import com.repzone.core.util.extensions.now

/**
 * Tüm domain event'lerin base interface'i
 * Sealed interface olduğu için type-safe
 */
sealed interface DomainEvent {
    /**
     * Event'in oluşturulma zamanı (milliseconds)
     */
    val timestamp: Long

    /**
     * Customer Balance Events
     */
    data class CustomerBalanceUpdated(val customerId: Long,
                                      val balance: Double,
                                      override val timestamp: Long = now()
    ):DomainEvent

    data class VisitStartEvent(val visitId: Long, val customerId: Long, val appointmentId: Long, override val timestamp: Long = now()):DomainEvent
    data class VisitStoptEvent(val visitId: Long, override val timestamp: Long = now()):DomainEvent

}