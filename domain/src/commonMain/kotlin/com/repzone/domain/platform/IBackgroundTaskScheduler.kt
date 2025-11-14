package com.repzone.domain.platform
import com.repzone.domain.common.Result

/**
 * Background Task Scheduler Interface
 * Platform-specific arka plan görev zamanlayıcı (expect/actual)
 */
expect interface IBackgroundTaskScheduler {
    /**
     * Periyodik GPS toplama görevini planlar
     * @param intervalMinutes Dakika cinsinden aralık
     * @param flexIntervalMinutes Esneklik aralığı (battery optimization için)
     */
    fun scheduleGpsCollection(
        intervalMinutes: Int,
        flexIntervalMinutes: Int = 5
    ): Result<Unit>

    /**
     * Periyodik veri senkronizasyon görevini planlar
     * @param intervalMinutes Dakika cinsinden aralık
     * @param requiresNetwork Network gereksinimi
     */
    fun scheduleDataSync(
        intervalMinutes: Int,
        requiresNetwork: Boolean = true
    ): Result<Unit>

    /**
     * GPS toplama görevini iptal eder
     */
    fun cancelGpsCollection()

    /**
     * Veri senkronizasyon görevini iptal eder
     */
    fun cancelDataSync()

    /**
     * Tüm planlanmış görevleri iptal eder
     */
    fun cancelAllTasks()

    /**
     * Görevin çalışıp çalışmadığını kontrol eder
     */
    fun isTaskScheduled(taskId: String): Boolean

    /**
     * Tek seferlik görev planlar (immediate execution)
     */
    fun scheduleOneTimeTask(
        taskId: String,
        delayMinutes: Int = 0,
        callback: suspend () -> Unit
    ): Result<Unit>
}

/**
 * Task IDs
 * Arka plan görevleri için sabit ID'ler
 */
object BackgroundTaskIds {
    const val GPS_COLLECTION = "gps_collection_task"
    const val DATA_SYNC = "data_sync_task"
    const val FORCE_GPS_UPDATE = "force_gps_update_task"
}