package com.repzone.sync.di

import org.koin.dsl.module

val SyncModule = module {

    // OrderSyncJob: HttpClient ve CoroutineScope parametre ile gelecek
   /* factory { (client: HttpClient, scope: CoroutineScope) ->
        val api = get<IOrderApi> { parametersOf(client) } // NetworkModule, client parametresi ister
        OrderSyncJob(
            outbox = get<OrderOutboxService>(),          // data:repository Koin modülünden gelir
            api = api,
            scope = scope
        )
    }*/
}