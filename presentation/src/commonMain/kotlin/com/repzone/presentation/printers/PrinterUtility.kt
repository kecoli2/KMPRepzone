package com.repzone.presentation.printers

enum class Transport { TCP, Bluetooth }

sealed class ZebraResult {
    data object Success : ZebraResult()
    data class Failure(val message: String, val cause: Throwable? = null) : ZebraResult()
    data class NotSupported(val reason: String) : ZebraResult()
}

data class PrintOptions(
    val transport: Transport,
    val host: String? = null,
    val port: Int = 9100,
    val btAddress: String? = null,
    val timeoutMs: Int = 5000
)

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ZebraPrinterManager() {
    suspend fun printZpl(zpl: String, options: PrintOptions): ZebraResult
}