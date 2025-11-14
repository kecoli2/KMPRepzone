package com.repzone.domain.model.gps

import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.core.util.extensions.now
import com.repzone.domain.util.TimeUtils

data class SyncStatistics(
    val totalLocations: Int,
    val syncedLocations: Int,
    val unsyncedLocations: Int,
    val lastSyncTime: Long?,
    val isSyncing: Boolean
) {
    val syncPercentage: Float
        get() = if (totalLocations > 0) {
            (syncedLocations.toFloat() / totalLocations) * 100f
        } else 0f

    fun getFormattedLastSync(): UiText {
        return lastSyncTime?.let {
            TimeUtils.formatDuration(now() - it)
        } ?: UiText.resource(StringResource.SYNC_GPS_NEVER)

    }
}