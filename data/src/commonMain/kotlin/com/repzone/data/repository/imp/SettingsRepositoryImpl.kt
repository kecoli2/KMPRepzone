package com.repzone.data.repository.imp

import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.document.model.ConflictResolution
import com.repzone.domain.document.model.DiscountCalculationMode
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.GeneralSettings
import com.repzone.domain.document.model.StockSettings
import com.repzone.domain.repository.ISettingsRepository

class SettingsRepositoryImpl(private val iDatabaseManager: IDatabaseManager): ISettingsRepository {
    //region Properties
    private var generalSettings: GeneralSettings? = null
    private var stockSettings: StockSettings? = null
    private var listOfSlotConfigs: List<DiscountSlotConfig>? = null
    //endregion

    //region Public Method
    override suspend fun getGeneralSettings(): GeneralSettings {
        if (generalSettings != null) {
            return generalSettings!!
        }
        generalSettings = GeneralSettings(
            defaultCalculationMode = DiscountCalculationMode.CASCADING,
            defaultConflictResolution = ConflictResolution.ASK_USER
        )
        return generalSettings!!
    }

    override suspend fun getStockSettings(): StockSettings {
        return if(stockSettings != null){
                stockSettings!!
            }else {
            stockSettings = StockSettings(
                orderStockBehavior = com.repzone.domain.document.model.StockBehavior.BLOCK
            )
            stockSettings!!
        }
    }

    override suspend fun getSlotConfigs(): List<DiscountSlotConfig> {
        if(listOfSlotConfigs != null){
            return listOfSlotConfigs!!
        }
        listOfSlotConfigs = listOf(
            DiscountSlotConfig(
                slotNumber = 1,
                name = "Indirim 1",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(2, "Indirim 2",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(3, "Indirim 3",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(4, "Indirim 4",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(5, "Indirim 5",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(6, "Indirim 6",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(7, "Indirim 7",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),
            DiscountSlotConfig(8, "Indirim 8",
                allowManualEntry = true,
                allowAutomatic = true,
                maxPercentage = null
            ),

        )
        return listOfSlotConfigs!!
    }

    override suspend fun updateGeneralSettings(settings: GeneralSettings) {
        TODO("Not yet implemented")
    }

    override suspend fun updateStockSettings(settings: StockSettings) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSlotConfig(config: DiscountSlotConfig) {
        TODO("Not yet implemented")
    }
    //endregion

}