package com.repzone.domain.platform

actual interface BackgroundTaskScheduler {
    actual fun scheduleGpsCollection(
        intervalMinutes: Int,
        flexIntervalMinutes: Int,
    ): Result<Unit>

    actual fun scheduleDataSync(
        intervalMinutes: Int,
        requiresNetwork: Boolean,
    ): Result<Unit>

    actual fun cancelGpsCollection()
    actual fun cancelDataSync()
    actual fun cancelAllTasks()
    actual fun isTaskScheduled(taskId: String): Boolean
    actual fun scheduleOneTimeTask(
        taskId: String,
        delayMinutes: Int,
        callback: suspend () -> Unit,
    ): Result<Unit>
}