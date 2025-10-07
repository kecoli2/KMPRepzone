package com.repzone.presentation.viewmodel.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.presentation.base.ViewModelHost
import com.repzone.sync.model.SyncJobResult
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import com.repzone.sync.model.SyncJobType.*
import com.repzone.sync.model.SyncProgress
import com.repzone.sync.model.UserRole
import com.repzone.sync.transaction.TransactionStats

/**
 * Sync Test Screen - ViewModelHost pattern ile
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncTestScreen(
    modifier: Modifier = Modifier
) {
    ViewModelHost<SyncTestViewModel> { viewModel ->
        val state by viewModel.state.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }

        // Error ve success message handling
        LaunchedEffect(state.errorMessage) {
            state.errorMessage?.let { error ->
                snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = "Tamam",
                    duration = SnackbarDuration.Short
                )
                viewModel.onEvent(SyncTestViewModel.Event.ClearError)
            }
        }

        LaunchedEffect(state.successMessage) {
            state.successMessage?.let { success ->
                snackbarHostState.showSnackbar(
                    message = success,
                    duration = SnackbarDuration.Short
                )
                viewModel.onEvent(SyncTestViewModel.Event.ClearSuccess)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("🚀 Sync Test Center")
                            Text(
                                text = "Rol: ${state.userRole.getDisplayName()}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.onEvent(SyncTestViewModel.Event.RefreshStats)
                        }) {
                            Icon(Icons.Default.Refresh, "Yenile")
                        }
                    }
                )
            }
        ) { paddingValues ->

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Quick action buttons
                item {
                    QuickActionsCard(
                        onStartFullSync = {
                            viewModel.onEvent(SyncTestViewModel.Event.StartFullSync)
                        },
                        onStartProducts = {
                            viewModel.onEvent(SyncTestViewModel.Event.StartSpecificJob(PRODUCTS))
                        },
                        onStartCustomers = {
                            viewModel.onEvent(SyncTestViewModel.Event.StartSpecificJob(CUSTOMERS))
                        },
                        isLoading = state.uiFrame.isLoading
                    )
                }

                // Overall progress
                item {
                    OverallProgressTestCard(
                        progress = state.overallProgress,
                        onPauseAll = { viewModel.onEvent(SyncTestViewModel.Event.PauseAll) },
                        onResumeAll = { viewModel.onEvent(SyncTestViewModel.Event.ResumeAll) },
                        onCancelAll = { viewModel.onEvent(SyncTestViewModel.Event.CancelAll) }
                    )
                }

                // Individual jobs
                item {
                    Text(
                        text = "📋 Job Status'ları",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(state.allJobsStatusList) { (jobType, status) ->
                    JobTestCard(
                        jobType = jobType,
                        status = status,
                        userRole = state.userRole,
                        onTrigger = {
                            viewModel.onEvent(SyncTestViewModel.Event.StartSpecificJob(jobType))
                        }
                    )
                }

                // Performance stats
                item {
                    PerformanceTestCard(
                        stats = state.coordinatorStats,
                        jobHistory = state.jobHistory
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionsCard(
    onStartFullSync: () -> Unit,
    onStartProducts: () -> Unit,
    onStartCustomers: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "⚡ Hızlı İşlemler",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Full sync button
            Button(
                onClick = onStartFullSync,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.PlayArrow, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tüm Sync İşlemlerini Başlat")
            }

            // Individual job buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onStartProducts,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("📦")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ürünler")
                }

                OutlinedButton(
                    onClick = onStartCustomers,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("👥")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Müşteriler")
                }
            }
        }
    }
}

@Composable
private fun OverallProgressTestCard(
    progress: SyncProgress,
    onPauseAll: () -> Unit,
    onResumeAll: () -> Unit,
    onCancelAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (progress.isRunning)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Genel Durum",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (progress.isRunning) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatusBadge("Toplam", progress.totalJobs, Color.Gray)
                StatusBadge("✅", progress.completedJobs, Color(0xFF4CAF50))
                StatusBadge("⚡", progress.runningJobs, Color(0xFF2196F3))
                StatusBadge("❌", progress.failedJobs, Color(0xFFF44336))
            }

            if (progress.isRunning) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onPauseAll,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Pause, null, modifier = Modifier.size(16.dp))
                        Text("Duraklat")
                    }

                    OutlinedButton(
                        onClick = onCancelAll,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, null, modifier = Modifier.size(16.dp))
                        Text("İptal")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, count: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = color.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun JobTestCard(
    jobType: SyncJobType,
    status: SyncJobStatus,
    userRole: UserRole,
    onTrigger: () -> Unit
) {
    val isApplicable = isJobApplicableForRole(jobType, userRole)
    val progressPercentage = status.getProgressPercentage()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                !isApplicable -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                status is SyncJobStatus.Success -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                status is SyncJobStatus.Failed -> Color(0xFFF44336).copy(alpha = 0.1f)
                status is SyncJobStatus.Running || status is SyncJobStatus.Progress ->
                    Color(0xFF2196F3).copy(alpha = 0.1f)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${jobType.getIcon()} ${jobType.getDisplayName()}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                StatusIndicator(status)
            }

            if (status is SyncJobStatus.Progress) {
                LinearProgressIndicator(
                    progress = progressPercentage / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "${status.current}/${status.total} - ${progressPercentage}%",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = status.getDisplayMessage(isApplicable, userRole),
                style = MaterialTheme.typography.bodySmall
            )

            if (isApplicable && status !is SyncJobStatus.Running && status !is SyncJobStatus.Progress) {
                TextButton(onClick = onTrigger) {
                    Text("▶️ Başlat")
                }
            }
        }
    }
}

@Composable
private fun StatusIndicator(status: SyncJobStatus) {
    when (status) {
        is SyncJobStatus.Running, is SyncJobStatus.Progress -> {
            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
        }
        is SyncJobStatus.Success -> {
            Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
        }
        is SyncJobStatus.Failed -> {
            Icon(Icons.Default.Error, null, tint = Color(0xFFF44336), modifier = Modifier.size(16.dp))
        }
        else -> {
            Icon(Icons.Default.Schedule, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun PerformanceTestCard(
    stats: TransactionStats,
    jobHistory: List<SyncJobResult>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📈 Performans İstatistikleri",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatColumn("Toplam", stats.totalOperations.toString())
                StatColumn("✅ Başarılı", stats.successfulOperations.toString())
                StatColumn("❌ Başarısız", stats.failedOperations.toString())
                StatColumn("Oran", "${if(stats.totalOperations > 0) (stats.successfulOperations * 100 / stats.totalOperations) else 0}%")
            }

            if (jobHistory.isNotEmpty()) {
                Divider()
                Text("Son 5 İşlem:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                jobHistory.takeLast(5).forEach { result ->
                    Text(
                        text = "• ${result.jobType.getDisplayName()}: ${result.recordsProcessed} kayıt",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun StatColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

// Helper functions
private fun SyncJobType.getIcon(): String = when (this) {
    PRODUCTS -> "📦"
    CUSTOMERS -> "👥"
    PRODUCTS_GROUP -> "📦"
    ROUTE -> "📦"
    CUSTOMERS_GROUP -> "📦"
    TABLE_REPLICATION -> "📦"
    COMMON_MODULES -> "📦"
    FORM -> "📦"
    EXTRA_TABLE -> "📦"
    STOCK -> "📦"
    else -> "📦"
}

private fun SyncJobType.getDisplayName(): String = when (this) {
    PRODUCTS -> "Ürünler"
    CUSTOMERS -> "Müşteriler"
    PRODUCTS_GROUP -> "Ürün Grupları"
    ROUTE -> "Rotalar"
    CUSTOMERS_GROUP -> "Müşteri Grupları"
    TABLE_REPLICATION -> "Replikasyon"
    COMMON_MODULES -> "Custom Field"
    FORM -> "Formlar"
    EXTRA_TABLE -> "Extarnal"
    STOCK -> "Stoklar"
    CUSTOMERS_EMAIL -> "Müşteri Email"
    CUSTOMERS_PRICE_PARAMETERS -> "Müşteri Fiyat"
    CUSTOMERS_GROUP_PRICE -> "Müşteri Grubu Fiyat"
    COMMON_MODULES_REASONS -> "Custom Field Reasons"
    COMMON_DOCUMENT_MAPS -> "Document Maps"
}

private fun UserRole.getDisplayName(): String = when (this) {
    UserRole.SALES_REP -> "Satış Temsilcisi"
    UserRole.MERGE_STAFF -> "Merge Elemanı"
    UserRole.MANAGER -> "Müdür"
    UserRole.ADMIN -> "Sistem Yöneticisi"
}

private fun isJobApplicableForRole(jobType: SyncJobType, userRole: UserRole): Boolean {
    return when (jobType) {
        PRODUCTS -> userRole in setOf(UserRole.MERGE_STAFF, UserRole.MANAGER, UserRole.ADMIN)
        CUSTOMERS -> userRole in setOf(UserRole.SALES_REP, UserRole.MANAGER, UserRole.ADMIN)
        PRODUCTS_GROUP -> userRole in setOf(UserRole.MANAGER, UserRole.ADMIN, UserRole.SALES_REP)
        ROUTE -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        CUSTOMERS_GROUP_PRICE -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        CUSTOMERS_GROUP -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        TABLE_REPLICATION -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        COMMON_MODULES -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        FORM -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        EXTRA_TABLE -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        STOCK -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        CUSTOMERS_EMAIL -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        CUSTOMERS_PRICE_PARAMETERS -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        COMMON_MODULES_REASONS -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
        COMMON_DOCUMENT_MAPS -> userRole in setOf(UserRole.ADMIN, UserRole.SALES_REP)
    }
}

private fun SyncJobStatus.getProgressPercentage(): Int = when (this) {
    is SyncJobStatus.Idle -> 0
    is SyncJobStatus.Running -> 50
    is SyncJobStatus.Progress -> if (total > 0) ((current.toDouble() / total) * 100).toInt() else 0
    is SyncJobStatus.Success -> 100
    is SyncJobStatus.Failed -> 0
}

private fun SyncJobStatus.getDisplayMessage(isApplicable: Boolean, userRole: UserRole): String {
    if (!isApplicable) {
        return "❌ ${userRole.getDisplayName()} rolü için geçerli değil"
    }

    return when (this) {
        is SyncJobStatus.Idle -> "⏳ Bekliyor"
        is SyncJobStatus.Running -> "⚡ Çalışıyor..."
        is SyncJobStatus.Progress -> "📊 ${message ?: "$current/$total işlendi"}"
        is SyncJobStatus.Success -> "✅ $recordCount kayıt işlendi (${duration}ms)"
        is SyncJobStatus.Failed -> "❌ Hata: $error"
    }
}