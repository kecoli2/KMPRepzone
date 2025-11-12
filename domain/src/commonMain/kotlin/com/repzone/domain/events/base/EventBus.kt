package com.repzone.domain.events.base

import com.repzone.domain.events.base.events.DomainEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn

class EventBus(
    private val logger: IEventLogger? = null
) : IEventBus {

    //region Field
    private val _events = MutableSharedFlow<DomainEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val events: Flow<DomainEvent> = _events.asSharedFlow()
    //endregion Field

    //region Public Method
    override val subscriptionCount: StateFlow<Int> = _events.subscriptionCount
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    override suspend fun publish(event: DomainEvent) {
        logger?.logEmit(event, subscriptionCount.value)
        _events.emit(event)
    }

    override fun tryPublish(event: DomainEvent): Boolean {
        logger?.logEmit(event, subscriptionCount.value)
        return _events.tryEmit(event)
    }
    //endregion Public Method
}