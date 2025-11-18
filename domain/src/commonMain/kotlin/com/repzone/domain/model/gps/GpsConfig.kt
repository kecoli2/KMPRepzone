package com.repzone.domain.model.gps
import com.repzone.core.util.TimeUtils
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.common.Result
import kotlinx.datetime.DayOfWeek

/**
 * Gps Configuration sonrasında calisma anında degisebilir default
 */
data class GpsConfig(
    val isActive: Boolean = true,                          // Aktif (ON/OFF switch)
    val enableLiveTracking: Boolean = true,                // Canlı Takip (Evet/Hayır)
    val gpsIntervalMinutes: Int = 1,                       // Konum Gönderim Sıklığı (dakika)
    val startTimeHour: Int = 9,                            // Takip Başlangıç Saati
    val startTimeMinute: Int = 0,
    val endTimeHour: Int = 18,                             // Takip Bitiş Saati
    val endTimeMinute: Int = 0,
    val activeDays: List<DayOfWeek> = listOf(                // Takip Günleri
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    ),
    val enableBackgroundTracking: Boolean = true,          // Arkaplanlda İzleme (Evet/Hayır)

    // Mobil taraf parametreleri (backend'de yok, mobilde kullanılıyor) bunları da sonrasında ekleyeceğiz
    val minDistanceMeters: Float = 0f,                    // Minimum mesafe filtresi
    val accuracyThreshold: Float = 250f,                   // Doğruluk eşiği
    val batteryOptimizationEnabled: Boolean = false,       // Batarya optimizasyonu
    val serverSyncIntervalMinutes: Int = 15,               // Server sync aralığı
    val autoSyncOnGpsUpdate: Boolean = true                // Her GPS'te sync
){
    fun validate(): Result<Unit> {
        return when {
            gpsIntervalMinutes < 1 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_INTERVAL_TOO_SHORT))
            minDistanceMeters < 0 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_MIN_DISTANCE_NAGATIVE))
            accuracyThreshold < 0 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.GPS_ACCURACY_THRESHOLD_NEGATIVE))
            startTimeHour !in 0..23 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_START_HOUR_RANGE))
            startTimeMinute !in 0..59 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_START_MINUTE_RANGE))
            endTimeHour !in 0..23 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_END_HOUR_RANGE))
            endTimeMinute !in 0..59 -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_END_MINUTE_RANGE))
            activeDays.isEmpty() -> Result.Error(DomainException.BusinessRuleException(ErrorCode.ERROR_ACTIVE_DAYS_REQUIRED))
            else -> Result.Success(Unit)
        }
    }

    /**
     * Servis aktif mi ve schedule içinde mi?
     */
    fun isActiveAndWithinSchedule(currentTimeMillis: Long = TimeUtils.currentTimeMillis()): Boolean {
        if (!isActive) return false

        return isWithinSchedule(currentTimeMillis)
    }

    /**
     * Background tracking (Foreground Service + Firebase) çalışmalı mı? Burası tekrardan sorulacak TODO
     *
     * Koşullar:
     * 1. isActive = true olmalı
     * 2. enableBackgroundTracking = true olmalı (Arkaplanlda İzleme)
     * 3. Schedule içinde olmalı (09:00-18:00, aktif günler)
     *
     * @return true ise Foreground Service aktif + Firebase gönder
     *         false ise sadece Repository'e kaydet
     */
    fun shouldRunBackgroundService(currentTimeMillis: Long = TimeUtils.currentTimeMillis()): Boolean {
        if (!isActive) return false
        if (!enableBackgroundTracking) return false

        return isWithinSchedule(currentTimeMillis)
    }

    /**
     * Canlı takip aktif mi?
     * enableLiveTracking = true ise, Firebase'e real-time gönder
     */
    fun shouldSendToFirebase(): Boolean {
        return isActive && enableLiveTracking && enableBackgroundTracking
    }

    /**
     * Schedule içinde mi?
     */
    fun isWithinSchedule(currentTimeMillis: Long = TimeUtils.currentTimeMillis()): Boolean {
        // Günü kontrol et
        val currentDay = TimeUtils.getCurrentDayOfWeek(currentTimeMillis)
        if (currentDay !in activeDays) return false

        // Saati kontrol et
        val currentHour = TimeUtils.getCurrentHour(currentTimeMillis)
        val currentMinute = TimeUtils.getCurrentMinute(currentTimeMillis)
        val currentTimeInMinutes = currentHour * 60 + currentMinute
        val startTimeInMinutes = startTimeHour * 60 + startTimeMinute
        val endTimeInMinutes = endTimeHour * 60 + endTimeMinute
        return currentTimeInMinutes in startTimeInMinutes..endTimeInMinutes
    }

    /**
     * Bir sonraki çalışma zamanına kadar kaç dakika var?
     */
    fun getMinutesUntilNextScheduledTime(currentTimeMillis: Long = TimeUtils.currentTimeMillis()): Int {
        if (isWithinSchedule(currentTimeMillis)) return 0

        // Şu anki gün ve saat
        val currentDay = TimeUtils.getCurrentDayOfWeek(currentTimeMillis)
        val currentHour = TimeUtils.getCurrentHour(currentTimeMillis)
        val currentMinute = TimeUtils.getCurrentMinute(currentTimeMillis)
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
}