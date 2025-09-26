package com.repzone.sync.model

data class SyncProgress(
    val totalJobs: Int,
    val completedJobs: Int,
    val runningJobs: Int,
    val failedJobs: Int,
    val isRunning: Boolean
)