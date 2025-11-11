package com.repzone.domain.events.base

import com.repzone.domain.events.DomainEvent
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
    suspend fun emit(event: DomainEvent)

    /**
     * Event yayınla (non-blocking)
     */
    fun tryEmit(event: DomainEvent): Boolean
}