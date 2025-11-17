package com.repzone.presentation.legacy.ui.gpstest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.core.util.extensions.now
import com.repzone.domain.model.SyncStatus
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.ServiceState
import com.repzone.domain.model.gps.TrackingStatistics
import com.repzone.domain.util.format
import com.repzone.presentation.legacy.viewmodel.gpstest.GpsTrackingUiState
import com.repzone.presentation.legacy.viewmodel.gpstest.GpsTrackingViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsTrackingScreen() {
    val viewModel: GpsTrackingViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPS Tracking") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            StatusCard(uiState, viewModel)

            // Last Location Card
            if (uiState.lastLocation != null) {
                LocationCard(uiState.lastLocation!!)
            }

            // Statistics Card
            if (uiState.statistics != null) {
                StatisticsCard(uiState.statistics!!)
            }

            // Sync Status Card
            SyncStatusCard(uiState)

            // Control Buttons
            ControlButtons(uiState, viewModel)

            // Error Message
            if (uiState.error != null) {
                ErrorCard(uiState.error!!, viewModel)
            }
        }
    }
}

@Composable
private fun StatusCard(
    uiState: GpsTrackingUiState,
    viewModel: GpsTrackingViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                uiState.isTracking && !uiState.isPaused -> Color(0xFF4CAF50)
                uiState.isPaused -> Color(0xFFFFC107)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when {
                    uiState.isLoading -> "⏳ Yükleniyor..."
                    uiState.isTracking && !uiState.isPaused -> "✅ GPS Tracking Aktif"
                    uiState.isPaused -> "⏸️ Duraklatıldı"
                    else -> "⭕ GPS Tracking Kapalı"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (uiState.isTracking) Color.White else MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = when (uiState.serviceState) {
                    is ServiceState.Starting -> "Başlatılıyor..."
                    is ServiceState.Running -> "Çalışıyor"
                    is ServiceState.Paused -> "Duraklatıldı"
                    is ServiceState.Stopping -> "Durduruluyor..."
                    is ServiceState.Error -> "Hata: ${(uiState.serviceState as ServiceState.Error).domainException.message}"
                    else -> "Hazır"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = if (uiState.isTracking) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LocationCard(location: GpsLocation) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📍 Son GPS",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider()

            InfoRow("Koordinat", "${location.latitude.format(6)}, ${location.longitude.format(6)}")
            InfoRow("Hassasiyet", "${location.accuracy.toInt()} metre")
            InfoRow("Yükseklik", "${location.altitude?.toInt() ?: "-"} metre")

            if (location.speed != null && location.speed ?: 0f > 0f) {
                InfoRow("Hız", "${(location.speed ?: 0f * 3.6).toInt()} km/s")
            }

            if (location.bearing != null) {
                InfoRow("Yön", getBearingDirection(location.bearing ?: 0f))
            }

            InfoRow("Zaman", formatTimestamp(location.timestamp))
        }
    }
}

@Composable
private fun StatisticsCard(stats: TrackingStatistics) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📊 İstatistikler",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider()

            InfoRow("Toplam Konum", "${stats.totalLocations} adet")
            InfoRow("Mesafe", stats.getFormattedDistance())
            InfoRow("Ortalama Hassasiyet", "${stats.averageAccuracy.toInt()} metre")

            /*if (stats.totalLocations != null) {
                InfoRow("Son GPS", formatTimestamp(stats.lastGpsTime))
            }*/
        }
    }
}

@Composable
private fun SyncStatusCard(uiState: GpsTrackingUiState) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "☁️ Senkronizasyon",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider()

            when (val status = uiState.syncStatus) {
                is SyncStatus.Idle -> {
                    InfoRow("Durum", "Beklemede")
                }
                is SyncStatus.Syncing -> {
                    InfoRow("Durum", "⏳ Senkronize ediliyor...")
                    if (uiState.isSyncing) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                is SyncStatus.Success -> {
                    InfoRow("Durum", "✅ Başarılı")
                    InfoRow("Gönderilen", "${status.syncedCount} konum")
                }
                is SyncStatus.Failed -> {
                    InfoRow("Durum", "❌ Hata")
                    Text(
                        text = status.error.message ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                else -> {}
            }

            if (uiState.statistics != null) {
                InfoRow(
                    "Bekleyen",
                    "${uiState.statistics.unsyncedCount} konum"
                )
            }
        }
    }
}

@Composable
private fun ControlButtons(
    uiState: GpsTrackingUiState,
    viewModel: GpsTrackingViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Start/Stop Button
        if (!uiState.isTracking) {
            Button(
                onClick = { viewModel.startTracking() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canStartTracking
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("▶️ Tracking Başlat")
            }
        } else {
            Button(
                onClick = { viewModel.stopTracking() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.canStopTracking,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("⏹️ Tracking Durdur")
            }
        }

        // Pause/Resume Button
        if (uiState.isTracking) {
            OutlinedButton(
                onClick = {
                    if (uiState.isPaused) {
                        viewModel.resumeTracking()
                    } else {
                        viewModel.pauseTracking()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isPaused) "▶️ Devam Et" else "⏸️ Duraklat")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Force GPS Button
            OutlinedButton(
                onClick = { viewModel.forceGpsUpdate() },
                modifier = Modifier.weight(1f),
                enabled = uiState.canForceGps
            ) {
                if (uiState.isForcingGps) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text("📍 GPS Al")
                }
            }

            // Sync Now Button
            OutlinedButton(
                onClick = { viewModel.syncNow() },
                modifier = Modifier.weight(1f),
                enabled = uiState.canSync
            ) {
                if (uiState.isSyncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text("☁️ Sync")
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(
    error: String,
    viewModel: GpsTrackingViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "⚠️ Hata",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            IconButton(onClick = { viewModel.clearError() }) {
                Text("✖️")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

// Helper Functions
private fun getBearingDirection(bearing: Float): String {
    return when {
        bearing >= 337.5 || bearing < 22.5 -> "Kuzey ↑"
        bearing >= 22.5 && bearing < 67.5 -> "Kuzeydoğu ↗"
        bearing >= 67.5 && bearing < 112.5 -> "Doğu →"
        bearing >= 112.5 && bearing < 157.5 -> "Güneydoğu ↘"
        bearing >= 157.5 && bearing < 202.5 -> "Güney ↓"
        bearing >= 202.5 && bearing < 247.5 -> "Güneybatı ↙"
        bearing >= 247.5 && bearing < 292.5 -> "Batı ←"
        else -> "Kuzeybatı ↖"
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = now()
    val diffMillis = now - timestamp
    val diffSeconds = diffMillis / 1000
    val diffMinutes = diffSeconds / 60

    return when {
        diffSeconds < 10 -> "Az önce"
        diffSeconds < 60 -> "$diffSeconds saniye önce"
        diffMinutes < 60 -> "$diffMinutes dakika önce"
        else -> {
            val diffHours = diffMinutes / 60
            "$diffHours saat önce"
        }
    }
}
