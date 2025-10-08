package com.repzone.presentationlegacy.ui

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.repzone.core.util.PermissionStatus
import com.repzone.presentationlegacy.manager.PermissionManager
import kotlinx.coroutines.launch

@Composable
expect fun rememberPermissionManager(): PermissionManager

// 🔧 UI’den çağırmak için küçük bir yardımcı composable (örnek)
@Composable
fun PermissionsSection() {
    val pm = rememberPermissionManager()
    val scope = rememberCoroutineScope()

    var btStatus by remember { mutableStateOf<PermissionStatus?>(null) }
    var notifStatus by remember { mutableStateOf<PermissionStatus?>(null) }
    var gpsStatus by remember { mutableStateOf<PermissionStatus?>(null) }

    // Buraya kendi buton/Metin tasarımını koy
    Button(onClick = {
        scope.launch { btStatus = pm.ensureBluetooth() }
    }) { Text("Bluetooth izni iste") }

    Button(onClick = {
        scope.launch { notifStatus = pm.ensureNotifications() }
    }) { Text("Bildirim izni iste") }

    Button(onClick = {
        scope.launch {
            gpsStatus = pm.ensureLocation()
        }
    }) { Text("GPS izni iste") }

    // Basit durum çıktıları
    if (btStatus != null) {
        Text("Bluetooth: $btStatus")
    }
    if (notifStatus != null) {
        Text("Notifications: $notifStatus")
    }
    if(gpsStatus != null){
        Text("GPS: $gpsStatus")
    }
}