package com.repzone.network.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

class AndroidHttpEngineProvider : HttpEngineProvider {
    override fun get(): HttpClientEngine = OkHttp.create()
}