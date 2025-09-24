package com.repzone.network.http

import io.ktor.client.engine.HttpClientEngine

interface HttpEngineProvider {
    fun get(): HttpClientEngine
}