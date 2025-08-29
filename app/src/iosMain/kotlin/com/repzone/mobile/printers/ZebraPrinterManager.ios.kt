package com.repzone.mobile.printers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.dataUsingEncoding
import platform.Foundation.NSUTF8StringEncoding


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