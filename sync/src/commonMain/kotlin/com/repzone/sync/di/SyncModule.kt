package com.repzone.sync.di

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.network.dto.CustomerDto
import com.repzone.network.dto.MobileProductDto
import com.repzone.network.dto.MobileRouteDto
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.sync.factory.SyncJobFactory
import com.repzone.sync.impl.SyncApiCustomerImpl
import com.repzone.sync.impl.SyncApiProductGroupImpl
import com.repzone.sync.impl.SyncApiProductImpl
import com.repzone.sync.impl.SyncApiRouteDataImpl
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.manager.SyncManagerImpl
import com.repzone.sync.service.*
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SyncModule = module {
    single { SyncJobFactory(get()) }

    single { TransactionCoordinator(get(), get()) }

    //region ROUTEDATA
    single<IBulkInsertService<List<MobileRouteDto>>>(named("routeDataBulkInsert")) {
        RouteDataRawSqlBulkInsertService(get(named("SyncRouteAppointmentEntityDbMapper")),get())
    }
    single<ISyncApiService<List<MobileRouteDto>>>(named("routeDataSyncApi")){ SyncApiRouteDataImpl(get() ) }
    //endregion ROUTEDATA

    //region PRODUCT
    single<IBulkInsertService<List<MobileProductDto>>>(named("productBulkInsert")) { ProductRawSqlBulkInsertService(get(),get()) }
    single<ISyncApiService<List<MobileProductDto>>>(named("productSyncApi")){ SyncApiProductImpl(get()) }
    //endregion PRODUCT

    //region PRODUCT GROUP
    single<ISyncApiService<List<ServiceProductGroupDto>>>(named("productGroupSyncApi")){ SyncApiProductGroupImpl(get()) }
    single<IBulkInsertService<List<ServiceProductGroupDto>>>(named("productGroupBulkInsert")) {
        ProductGroupRawSqlBulkInsertService(
            get(),
            get()
        )
    }
    //endregion PRODUCT GROUP

    //region CUSTOMER
    single<ISyncApiService<List<CustomerDto>>>(named("customerGroupSyncApi")){ SyncApiCustomerImpl( get()) }
    single<IBulkInsertService<List<CustomerDto>>>(named("customerBulkInsert")) {
        CustomerRawSqlBulkInsertService(
            get(),
            get())
    }
    //endregion CUSTOMER

    //region GENERAL
    single<ISyncManager>{ SyncManagerImpl(get()) }
    //endregion GENERAL

    single {
        get<SyncJobFactory>().createJobs(
            productApi = get(named("productSyncApi")),
            productBulkInsert = get(named("productBulkInsert")),
            productGroupApi = get(named("productGroupSyncApi")),
            productGroupBulkInsert = get(named("productGroupBulkInsert")),
            routeApi = get(named("routeDataSyncApi")),
            routeBulkInsert = get(named("routeDataBulkInsert")),
            customerApi = get(named("customerGroupSyncApi")),
            customerBulkInsert = get(named("customerBulkInsert"))
        )
    }
}