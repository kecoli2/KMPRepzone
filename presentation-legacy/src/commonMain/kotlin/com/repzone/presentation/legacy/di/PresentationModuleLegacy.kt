package com.repzone.presentation.legacy.di

import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.presentation.legacy.theme.LegacyThemeConfig
import com.repzone.presentation.legacy.viewmodel.login.LoginScreenViewModel
import com.repzone.core.ui.viewmodel.splash.SplashScreenViewModel
import com.repzone.core.ui.viewmodel.sync.SyncViewModel
import com.repzone.domain.manager.visitmanager.IVisitManager
import com.repzone.domain.manager.visitmanager.VisitManager
import com.repzone.domain.usecase.visit.GetRouteInformationUseCase
import com.repzone.domain.usecase.visit.GetVisitMenuListUseCase
import com.repzone.presentation.legacy.navigation.NavigationSharedStateHolder
import com.repzone.presentation.legacy.viewmodel.visit.VisitViewModel
import com.repzone.presentation.legacy.viewmodel.customerlist.CustomerListViewModel
import com.repzone.presentation.legacy.viewmodel.gpstest.GpsTrackingViewModel
import com.repzone.presentation.legacy.viewmodel.sync.SyncTestViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val PresentationModuleLegacy = module {

    factory<IVisitManager> { VisitManager(get(), get(), get(), get()) }

    //region Single
    singleOf(::NavigationSharedStateHolder)
    singleOf<IPresentationConfig>(::LegacyThemeConfig)
    //endregion Single

    //region ViewModels
    factoryOf(::LoginScreenViewModel)
    factoryOf(::SyncTestViewModel)
    factoryOf(::SplashScreenViewModel)
    factoryOf(::SyncViewModel)
    factoryOf(::CustomerListViewModel)
    factoryOf(::VisitViewModel)
    factoryOf(::GpsTrackingViewModel)
    //endregion ViewModels

    //region Use Case
    factoryOf(::GetVisitMenuListUseCase)
    factoryOf(::GetRouteInformationUseCase)
    //endsregion Use Case

}
