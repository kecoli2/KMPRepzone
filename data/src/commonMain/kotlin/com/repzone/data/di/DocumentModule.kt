package com.repzone.data.di

import com.repzone.data.repository.imp.PromotionRuleRepository
import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.ProductListValidator
import com.repzone.domain.document.service.DocumentSession
import com.repzone.domain.document.service.LineDiscountCalculator
import com.repzone.domain.document.service.PromotionEngine
import com.repzone.domain.document.service.StockCalculator
import com.repzone.domain.document.service.StockValidator
import com.repzone.domain.repository.IPromotionRuleRepository
import com.repzone.domain.repository.ISettingsRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

var DocumentModule = module {
    singleOf(::StockCalculator)
    single {
        var settings = runBlocking {
            get<ISettingsRepository>().getStockSettings()
        }
        StockValidator(get(), settings)
    }

    single {
        var generalSettings = runBlocking {
            get<ISettingsRepository>().getGeneralSettings()
        }
        var slotConfigs = runBlocking {
            get<ISettingsRepository>().getSlotConfigs()
        }

        LineDiscountCalculator(slotConfigs, generalSettings)
    }

    factoryOf(::ProductListValidator)

    singleOf(::PromotionRuleRepository) { bind<IPromotionRuleRepository>() }
    singleOf(::PromotionEngine) {bind<IPromotionEngine>()}
    singleOf(::DocumentSession) {bind<IDocumentSession>()}
}