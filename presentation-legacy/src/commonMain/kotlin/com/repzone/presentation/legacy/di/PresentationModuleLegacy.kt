package com.repzone.presentation.legacy.di

import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.database.interfaces.IDatabaseManager
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
import com.repzone.presentation.legacy.viewmodel.sync.SyncTestViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val PresentationModuleLegacy = module {
    single { NavigationSharedStateHolder() }
    single<IPresentationConfig> { LegacyThemeConfig(get()) }
    factory { LoginScreenViewModel(get(), get(),get<IDatabaseManager>(), get()) }
    factory { SyncTestViewModel(get(), get(), get()) }
    factory { SplashScreenViewModel(get(), get()) }
    factory { SyncViewModel(get(),get(),get()) }
    factory { CustomerListViewModel(get(), get(), get(), get(), get(), get()) }
    factory { VisitViewModel(get(), get(), get()) }
    factory<IVisitManager> { VisitManager(get(), get(), get(), get()) }

    //region Use Case
    factoryOf(::GetVisitMenuListUseCase)
    factoryOf(::GetRouteInformationUseCase)
    //endsregion Use Case

}
