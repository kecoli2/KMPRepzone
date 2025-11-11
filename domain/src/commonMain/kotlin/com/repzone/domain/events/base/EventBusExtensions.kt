package com.repzone.domain.events.base

import com.repzone.domain.events.CustomerBalanceUpdated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull


fun IEventBus.subscribeToBalanceUpdates(): Flow<CustomerBalanceUpdated> {
    return events.mapNotNull { event ->
        when (event) {
            is CustomerBalanceUpdated -> event
        }
    }
}