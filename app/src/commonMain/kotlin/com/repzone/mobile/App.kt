package com.repzone.mobile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import com.repzone.core.interfaces.IFirebaseCrashService
import com.repzone.core.interfaces.ILocationService
import com.repzone.core.model.GeoPoint
import kotlinx.coroutines.launch
import okio.FileSystem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import repzonemobile.app.generated.resources.Res
import repzonemobile.app.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {

    setSingletonImageLoaderFactory { context ->

        getAsyncImageLoader(context)

    }
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val iFirebaseCrashlytics = koinInject<IFirebaseCrashService>()
        var showGpsContent by remember { mutableStateOf(false) }
        var gpsData by remember { mutableStateOf<GeoPoint?>(null) }
        val gpsService = koinInject<ILocationService>()
        val iFireBaseRealtimeDatabase = koinInject<IFireBaseRealtimeDatabase>()
        val scope = rememberCoroutineScope()
        val latest by remember(iFireBaseRealtimeDatabase) { iFireBaseRealtimeDatabase.observe("Test") }
            .collectAsState(initial = "Henüz yok")

        Column(modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Son değer: $latest")
            Text("trsdt")
            Button(onClick = {
                showContent = !showContent
                scope.launch {
                    iFireBaseRealtimeDatabase.set(path = "Test", value = "dsadsda")
                }
            }) {
                Text("Click me! deneme22")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
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
/*            PermissionsSection()
            PrinterSection()*/
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024) // 512MB
        .build()
}

/*
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
                        is ZebraResult.Success -> "Yazdırma başarılı"
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
}*/
