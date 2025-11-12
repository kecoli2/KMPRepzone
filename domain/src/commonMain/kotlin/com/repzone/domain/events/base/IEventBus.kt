package com.repzone.domain.events.base

import com.repzone.domain.events.base.events.DomainEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IEventBus {
    /**
     * Tüm domain event'lerini dinle
     */
    val events: Flow<DomainEvent>

    /**
     * Aktif subscriber sayısı
     */
    val subscriptionCount: StateFlow<Int>

    /**
     * Event yayınla (suspend)
     */
    suspend fun publish(event: DomainEvent)

    /**
     * Event yayınla (non-blocking)
     */
    fun tryPublish(event: DomainEvent): Boolean
}