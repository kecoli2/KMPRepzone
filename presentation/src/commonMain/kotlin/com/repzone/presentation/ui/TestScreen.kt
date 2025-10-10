package com.repzone.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.repzone.core.interfaces.ILocationService
import com.repzone.core.model.GeoPoint
import com.repzone.core.printers.PrintOptions
import com.repzone.core.printers.Transport
import com.repzone.core.printers.ZebraPrinterManager
import com.repzone.core.printers.ZebraResult
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.util.PermissionStatus
import com.repzone.presentation.viewmodel.TestScreenViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun TestScreen() = ViewModelHost<TestScreenViewModel> { vm ->
    var showContent by remember { mutableStateOf(false) }
    var showGpsContent by remember { mutableStateOf(false) }
    var gpsData by remember { mutableStateOf<GeoPoint?>(null) }
    val gpsService = koinInject<ILocationService>()
    val scope = rememberCoroutineScope()
    val latest by remember(vm.iFireBaseRealtimeDatabase) { vm.iFireBaseRealtimeDatabase.observe("Test") }
        .collectAsState(initial = "Henüz yok")

    Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer).safeContentPadding().fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally,    ) {
        Text("Son değer: $latest")
        Text("HELLO")
        Button(onClick = {
            showContent = !showContent
            scope.launch {
                vm.iFireBaseRealtimeDatabase.set(path = "Test", value = "dsadsda")
            }
        }) {
            Text("Click me! deneme22")
        }
        Button(onClick = {
            scope.launch {
                gpsData = gpsService.getCurrentLocation()
                gpsData?.let {
                    showGpsContent = true
                }
            }
        }){
            Text("GPS Al")
        }
        AnimatedVisibility(visible = showGpsContent) {
            Text("${gpsData?.latitude}, ${gpsData?.longitude}")
        }
        PermissionsSection()
        PrinterSection()
    }
}

@Composable
fun PrinterSection(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val pm = rememberPermissionManager()
    val zebra = remember { ZebraPrinterManager() }

    var transport by remember { mutableStateOf(Transport.TCP) }
    var host by remember { mutableStateOf("192.168.1.50") }
    var port by remember { mutableStateOf("9100") }
    var btMac by remember { mutableStateOf("00:22:58:AA:BB:CC") }

    var busy by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Zebra Yazıcı Test", style = MaterialTheme.typography.titleMedium)

        // Transport seçimi
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            FilterChip(
                selected = transport == Transport.TCP,
                onClick = { transport = Transport.TCP },
                label = { Text("TCP (Wi-Fi/Ethernet)") }
            )
            FilterChip(
                selected = transport == Transport.Bluetooth,
                onClick = { transport = Transport.Bluetooth },
                label = { Text("Bluetooth") }
            )
        }

        if (transport == Transport.TCP) {
            OutlinedTextField(
                value = host, onValueChange = { host = it },
                label = { Text("Yazıcı IP/Host") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = port, onValueChange = { port = it.filter { ch -> ch.isDigit() } },
                label = { Text("Port (9100)") }, singleLine = true, modifier = Modifier.width(160.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        } else {
            OutlinedTextField(
                value = btMac, onValueChange = { btMac = it.uppercase() },
                label = { Text("Bluetooth MAC (Android)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
        }

        val sampleZpl = remember {
            """
            ^XA
            ^CI28
            ^CF0,40
            ^FO50,40^FDMerhaba Zebra!^FS
            ^FO50,90^BCN,100,Y,N,N^FD1234567890^FS
            ^FO50,220^FD${'$'} Tarih: ^FS
            ^XZ
            """.trimIndent()
        }

        Button(
            enabled = !busy,
            onClick = {
                scope.launch {
                    busy = true
                    resultText = null
                    // 1) Gerekli izinleri iste
                    val permOk = when (transport) {
                        Transport.TCP -> true // TCP için runtime izin yok
                        Transport.Bluetooth -> {
                            when (pm.ensureBluetooth()) {
                                is PermissionStatus.Granted -> true
                                else -> false
                            }
                        }
                    }

                    if (!permOk) {
                        resultText = "İzin verilmedi (Bluetooth)."
                        busy = false
                        return@launch
                    }

                    // 2) PrintOptions hazırla
                    val opts = if (transport == Transport.TCP) {
                        val p = port.toIntOrNull() ?: 9100
                        PrintOptions(
                            transport = Transport.TCP,
                            host = host.ifBlank { null },
                            port = p
                        )
                    } else {
                        PrintOptions(
                            transport = Transport.Bluetooth,
                            btAddress = btMac.ifBlank { null }
                        )
                    }

                    // 3) Yazdır
                    val res = zebra.printZpl(sampleZpl, opts)
                    resultText = when (res) {
                        is ZebraResult.Success -> "Yazdırma başarılı ✅"
                        is ZebraResult.NotSupported -> "Desteklenmiyor: ${res.reason}"
                        is ZebraResult.Failure -> "Hata: ${res.message}"
                    }
                    busy = false
                }
            }
        ) {
            if (busy) CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            else Text("Yazdır")
        }

        if (!resultText.isNullOrBlank()) {
            Text(resultText!!, style = MaterialTheme.typography.bodyMedium)
        }
    }
}