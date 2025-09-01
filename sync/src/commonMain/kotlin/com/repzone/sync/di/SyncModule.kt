package com.repzone.sync.di

import com.repzone.data.outbox.OrderOutboxService
import com.repzone.network.api.IOrderApi
import com.repzone.sync.job.OrderSyncJob
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val SyncModule = module {

    // OrderSyncJob: HttpClient ve CoroutineScope parametre ile gelecek
    factory { (client: HttpClient, scope: CoroutineScope) ->
        val api = get<IOrderApi> { parametersOf(client) } // NetworkModule, client parametresi ister
        OrderSyncJob(
            outbox = get<OrderOutboxService>(),          // data:repository Koin modülünden gelir
            api = api,
            scope = scope
        )
    }
}