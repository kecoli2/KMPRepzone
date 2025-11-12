package com.repzone.domain.events.base

import com.repzone.domain.events.base.events.DecisionEvents
import com.repzone.domain.events.base.events.DomainEvent
import com.repzone.domain.events.base.events.PipelineEvents
import com.repzone.domain.events.base.events.ScreenEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

fun IEventBus.subscribeToBalanceUpdates(): Flow<DomainEvent.CustomerBalanceUpdated> {
    return events
        .mapNotNull { event ->
            when (event) {
                is DomainEvent.CustomerBalanceUpdated -> event
                else -> {
                    null
                }
            }
        }
}

fun IEventBus.decisionRequired(): Flow<DecisionEvents.DecisionRequired> {
    return events
        .mapNotNull { event ->
            when (event) {
                is DecisionEvents.DecisionRequired -> event
                else -> null
            }
        }
}

fun IEventBus.subscribeToEvents(): Flow<DomainEvent> {
    return events
        .mapNotNull { event ->
            event
        }
}