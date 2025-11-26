package com.repzone.core.printers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ZebraPrinterManager actual constructor() {

    actual suspend fun printZpl(zpl: String, options: PrintOptions): ZebraResult =
        withContext(Dispatchers.Default) {
            try {
                when (options.transport) {
                    Transport.TCP -> printOverTcp(zpl, options)
                    Transport.Bluetooth -> printOverBt(zpl, options)
                }
            } catch (t: Throwable) {
                ZebraResult.Failure("iOS Link-OS print failed: ${t.message}", t)
            }
        }

    //TODO: SDK EKLENINCE BAKILACAK
    private fun printOverTcp(zpl: String, options: PrintOptions): ZebraResult {
        /*val host = options.host ?: return ZebraResult.Failure("TCP host is required")
        val conn = TcpConnection(host, options.port)
        conn.open()
        conn.write(zpl.encodeToByteArray())
        conn.close()*/
        return ZebraResult.Success
    }

    // TODO: SDK EKLENINCE BAKILACAK
    private fun printOverBt(zpl: String, options: PrintOptions): ZebraResult {
        /*val mac = options.btAddress ?: return ZebraResult.Failure("Bluetooth address is required")
        val conn = MfiBtPrinterConnection(mac)
        conn.open()
        conn.write(zpl.encodeToByteArray())
        conn.close()*/
        return ZebraResult.Success
    }
}