package com.repzone.network.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

class IosHttpEngineProvider : HttpEngineProvider {
    override fun get(): HttpClientEngine = Darwin.create()
}