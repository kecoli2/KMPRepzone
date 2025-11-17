package com.repzone.data.di

import com.repzone.data.repository.imp.GpsConfigManagerImpl
import com.repzone.data.repository.imp.LocationRepositoryImpl
import com.repzone.data.service.GpsDataSyncServiceImpl
import com.repzone.data.service.GpsTrackingManagerImpl
import com.repzone.data.service.LocationServiceImpl
import com.repzone.domain.manager.gps.IGpsTrackingManager
import com.repzone.domain.repository.ILocationRepository
import com.repzone.domain.service.IGpsConfigManager
import com.repzone.domain.service.IGpsDataSyncService
import com.repzone.domain.service.ILocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val GpsModule = module {

    // Coroutine Scope
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    singleOf(::GpsConfigManagerImpl) { bind<IGpsConfigManager>() }
    singleOf(::LocationRepositoryImpl) { bind<ILocationRepository>() }
    singleOf(::LocationServiceImpl) { bind<ILocationService>() }
    singleOf(::GpsDataSyncServiceImpl) { bind<IGpsDataSyncService>() }
    singleOf(::GpsTrackingManagerImpl) { bind<IGpsTrackingManager>()}

}