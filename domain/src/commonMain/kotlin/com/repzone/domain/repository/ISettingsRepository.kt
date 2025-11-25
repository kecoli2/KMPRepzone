package com.repzone.domain.repository

import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.GeneralSettings
import com.repzone.domain.document.model.StockSettings

interface ISettingsRepository {
    /**
     * Genel ayarları getirir
     */
    suspend fun getGeneralSettings(): GeneralSettings

    /**
     * Stok ayarlarını getirir
     */
    suspend fun getStockSettings(): StockSettings

    /**
     * Slot konfigürasyonlarını getirir
     */
    suspend fun getSlotConfigs(): List<DiscountSlotConfig>

    /**
     * Ayarları günceller
     */
    suspend fun updateGeneralSettings(settings: GeneralSettings)

    suspend fun updateStockSettings(settings: StockSettings)

    suspend fun updateSlotConfig(config: DiscountSlotConfig)
}