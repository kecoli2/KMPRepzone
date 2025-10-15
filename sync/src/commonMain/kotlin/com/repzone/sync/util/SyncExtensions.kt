package com.repzone.sync.util

import androidx.compose.runtime.Composable
import com.repzone.sync.model.SyncJobStatus
import com.repzone.core.enums.UserRole

fun UserRole.getDisplayName(): String = when (this) {
    UserRole.SALES_REP -> "Satış Temsilcisi"
    UserRole.DISTRIBUTION -> "Dağıtım Elemanı"
}

fun SyncJobStatus.getProgressPercentage(): Int = when (this) {
    is SyncJobStatus.Idle -> 0
    is SyncJobStatus.Running -> 50
    is SyncJobStatus.Progress -> ((current.toDouble() / total) * 100).toInt()
    is SyncJobStatus.Success -> 100
    is SyncJobStatus.Failed -> 0
}

@Composable
fun SyncJobStatus.getDisplayMessage(): String = when (this) {
    is SyncJobStatus.Idle -> "Bekliyor"
    is SyncJobStatus.Running -> "Çalışıyor..."
    is SyncJobStatus.Progress -> resourceUi?.getMessage() ?: "$current/$total işlendi"
    is SyncJobStatus.Success -> "$recordCount kayıt işlendi (${duration}ms)"
    is SyncJobStatus.Failed -> "Hata: $error"
}