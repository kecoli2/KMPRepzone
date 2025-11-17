package com.repzone.domain.model.gps
import com.repzone.core.util.extensions.getCurrentDayOfWeek
import com.repzone.core.util.extensions.getCurrentHour
import com.repzone.core.util.extensions.getCurrentMinute
import com.repzone.core.util.extensions.now
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.common.Result
import kotlinx.datetime.DayOfWeek

/**
 * Gps Configuration sonrasında calisma anında degisebilir default
 */
data class GpsConfig(
    val gpsIntervalMinutes: Int = 5, // GPS toplama aralığı
    val serverSyncIntervalMinutes: Int = 15, // Sync aralığı
    val minDistanceMeters: Float = 10f,  // Min hareket mesafesi
    val accuracyThreshold: Float = 50f, // Max GPS hatası
    val batteryOptimizationEnabled: Boolean = true,
    val enableBackgroundTracking: Boolean = true,
    val autoSyncOnGpsUpdate: Boolean = true, // GPS güncellemelerinde otomatik olarak sync yap

    val enableSchedule: Boolean = true,  // Schedule aktif mi?
    val startTimeHour: Int = 9,          // Başlangıç saati (0-23)
    val startTimeMinute: Int = 0,        // Başlangıç dakikası (0-59)
    val endTimeHour: Int = 18,           // Bitiş saati (0-23)
    val endTimeMinute: Int = 0,          // Bitiş dakikası (0-59)
    val activeDays: List<DayOfWeek> = listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY
    )
){
    fun validate(): Result<Unit> {
        return when {
            gpsIntervalMinutes < 1 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_INTERVAL_TOO_SHORT))
            serverSyncIntervalMinutes < 1 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.SYNC_INTERVAL_TOO_SHORT))
            minDistanceMeters < 0 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_MIN_DISTANCE_NAGATIVE))
            accuracyThreshold < 0 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_ACCURACY_THRESHOLD_NEGATIVE))
            startTimeHour !in 0..23 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_START_HOUR_RANGE))
            startTimeMinute !in 0..59 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_START_MINUTE_RANGE))
            endTimeHour !in 0..23 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_END_HOUR_RANGE))
            endTimeMinute !in 0..59 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_END_MINUTE_RANGE))
            enableSchedule && activeDays.isEmpty() -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_ACTIVE_DAYS_REQUIRED))
            else -> Result.Success(Unit)
        }
    }

    fun isWithinSchedule(currentTimeMillis: Long = now()): Boolean {
        if (!enableSchedule) return true  // Schedule kapalıysa her zaman çalış

        // Günü kontrol et
        val currentDay = currentTimeMillis.getCurrentDayOfWeek()
        if (currentDay !in activeDays) return false

        // Saati kontrol et
        val currentHour = currentTimeMillis.getCurrentHour()
        val currentMinute = currentTimeMillis.getCurrentMinute()
        val currentTimeInMinutes = currentHour * 60 + currentMinute
        val startTimeInMinutes = startTimeHour * 60 + startTimeMinute
        val endTimeInMinutes = endTimeHour * 60 + endTimeMinute

        return currentTimeInMinutes in startTimeInMinutes..endTimeInMinutes
    }

    fun getMinutesUntilNextScheduledTime(currentTimeMillis: Long = now()): Int {
        if (!enableSchedule) return 0
        if (isWithinSchedule(currentTimeMillis)) return 0

        // Şu anki gün ve saat
        val currentDay = currentTimeMillis.getCurrentDayOfWeek()
        val currentHour = currentTimeMillis.getCurrentHour()
        val currentMinute = currentTimeMillis.getCurrentMinute()
        val currentTimeInMinutes = currentHour * 60 + currentMinute
        val startTimeInMinutes = startTimeHour * 60 + startTimeMinute

        // Bugün aktif bir gün mü?
        if (currentDay in activeDays) {
            // Henüz başlangıç saati gelmemişse
            if (currentTimeInMinutes < startTimeInMinutes) {
                return startTimeInMinutes - currentTimeInMinutes
            }
        }

        // Bir sonraki aktif günü bul
        val daysUntilNext = findDaysUntilNextActiveDay(currentDay)
        val minutesUntilMidnight = (24 * 60) - currentTimeInMinutes
        val minutesFromMidnightToStart = startTimeInMinutes

        return minutesUntilMidnight + (daysUntilNext - 1) * 24 * 60 + minutesFromMidnightToStart
    }

    private fun findDaysUntilNextActiveDay(currentDay: DayOfWeek): Int {
        if (activeDays.isEmpty()) return 0

        val allDays = DayOfWeek.entries
        val currentIndex = allDays.indexOf(currentDay)

        // Sonraki 7 günü kontrol et
        for (i in 1..7) {
            val nextDayIndex = (currentIndex + i) % allDays.size
            val nextDay = allDays[nextDayIndex]
            if (nextDay in activeDays) {
                return i
            }
        }
        return 1  // Fallback
    }

    fun getScheduleDescription(): String {
        if (!enableSchedule) return "Her zaman aktif"

        val startTime = "${startTimeHour.toString().padStart(2, '0')}:${startTimeMinute.toString().padStart(2, '0')}"
        val endTime = "${endTimeHour.toString().padStart(2, '0')}:${endTimeMinute.toString().padStart(2, '0')}"
        val days = activeDays.sortedBy { it.ordinal }.joinToString(", ") { it.name }

        return "$startTime - $endTime | $days"
    }
}