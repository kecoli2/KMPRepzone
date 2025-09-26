package com.repzone.sync.util

import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.UserRole

fun UserRole.getDisplayName(): String = when (this) {
    UserRole.SALES_REP -> "Satış Temsilcisi"
    UserRole.MERGE_STAFF -> "Merge Elemanı"
    UserRole.MANAGER -> "Müdür"
    UserRole.ADMIN -> "Sistem Yöneticisi"
}

fun SyncJobStatus.getProgressPercentage(): Int = when (this) {
    is SyncJobStatus.Idle -> 0
    is SyncJobStatus.Running -> 50
    is SyncJobStatus.Progress -> ((current.toDouble() / total) * 100).toInt()
    is SyncJobStatus.Success -> 100
    is SyncJobStatus.Failed -> 0
}

fun SyncJobStatus.getDisplayMessage(): String = when (this) {
    is SyncJobStatus.Idle -> "Bekliyor"
    is SyncJobStatus.Running -> "Çalışıyor..."
    is SyncJobStatus.Progress -> message ?: "$current/$total işlendi"
    is SyncJobStatus.Success -> "$recordCount kayıt işlendi (${duration}ms)"
    is SyncJobStatus.Failed -> "Hata: $error"
}