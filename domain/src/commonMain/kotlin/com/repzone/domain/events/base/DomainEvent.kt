package com.repzone.domain.events

/**
 * Tüm domain event'lerin base interface'i
 * Sealed interface olduğu için type-safe
 */
sealed interface DomainEvent {
    /**
     * Event'in oluşturulma zamanı (milliseconds)
     */
    val timestamp: Long
}