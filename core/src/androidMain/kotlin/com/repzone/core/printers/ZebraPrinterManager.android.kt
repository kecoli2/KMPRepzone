package com.repzone.core.printers

import com.zebra.sdk.comm.BluetoothConnection
import com.zebra.sdk.comm.Connection
import com.zebra.sdk.comm.TcpConnection
import com.zebra.sdk.printer.ZebraPrinterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ZebraPrinterManager actual constructor() {

    actual suspend fun printZpl(zpl: String, options: PrintOptions): ZebraResult =
        withContext(Dispatchers.IO) {
            try {
                when (options.transport) {
                    Transport.TCP -> printOverTcp(zpl, options)
                    Transport.Bluetooth -> printOverBt(zpl, options)
                }
            } catch (t: Throwable) {
                ZebraResult.Failure("Android Link-OS print failed: ${t.message}", t)
            }
        }

    private fun printOverTcp(zpl: String, options: PrintOptions): ZebraResult {
        val host = options.host ?: return ZebraResult.Failure("TCP host is required")
        val connections: Connection = TcpConnection(host, options.port)

        connections.open()
        val printer = ZebraPrinterFactory.getInstance(connections)
        printer.sendCommand(zpl)
        connections.close()

        return ZebraResult.Success
    }

    private fun printOverBt(zpl: String, options: PrintOptions): ZebraResult {
        val mac = options.btAddress ?: return ZebraResult.Failure("Bluetooth MAC is required")
        val connection = BluetoothConnection(mac)
        connection.open()
        val printer = ZebraPrinterFactory.getInstance(connection)
        printer.connection.write(zpl.toByteArray())
        printer.sendCommand(zpl)
        connection.close()
        return ZebraResult.Success
    }
}