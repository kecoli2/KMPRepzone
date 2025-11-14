package com.repzone.domain.service

import com.repzone.domain.model.gps.GpsConfig
import kotlinx.coroutines.flow.Flow

interface GpsConfigManager {
    fun getConfig(): GpsConfig
    fun updateConfig(config: GpsConfig)
    fun observeConfig(): Flow<GpsConfig>
}