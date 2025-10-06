package com.repzone.sync.di

import com.repzone.network.dto.CustomerDto
import com.repzone.network.dto.ProductDto
import com.repzone.network.dto.RouteDto
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.dto.ProductGroupDto
import com.repzone.sync.factory.SyncJobFactory
import com.repzone.sync.service.api.impl.SyncApiCustomerImpl
import com.repzone.sync.service.api.impl.SyncApiProductGroupImpl
import com.repzone.sync.service.api.impl.SyncApiProductImpl
import com.repzone.sync.service.api.impl.SyncApiRouteDataImpl
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.manager.SyncManagerImpl
import com.repzone.sync.service.api.impl.SyncApiCustomerGroupImpl
import com.repzone.sync.service.bulk.impl.CustomerGroupRawSqlBulkInsertService
import com.repzone.sync.service.bulk.impl.CustomerRawSqlBulkInsertService
import com.repzone.sync.service.bulk.impl.ProductGroupRawSqlBulkInsertService
import com.repzone.sync.service.bulk.impl.ProductRawSqlBulkInsertService
import com.repzone.sync.service.bulk.impl.RouteDataRawSqlBulkInsertService
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SyncModule = module {
    single { SyncJobFactory(get()) }

    single { TransactionCoordinator(get(), get()) }

    //region ROUTEDATA
    single<IBulkInsertService<List<RouteDto>>>(named("routeDataBulkInsert")) {
        RouteDataRawSqlBulkInsertService(get(named("SyncRouteAppointmentEntityDbMapper")), get())
    }
    single<ISyncApiService<List<RouteDto>>>(named("routeDataSyncApi")){ SyncApiRouteDataImpl(get() ) }
    //endregion ROUTEDATA

    //region PRODUCT
    single<IBulkInsertService<List<ProductDto>>>(named("productBulkInsert")) {
        ProductRawSqlBulkInsertService(
            get(),
            get()
        )
    }
    single<ISyncApiService<List<ProductDto>>>(named("productSyncApi")){ SyncApiProductImpl(get()) }
    //endregion PRODUCT

    //region PRODUCT GROUP
    single<ISyncApiService<List<ProductGroupDto>>>(named("productGroupSyncApi")){ SyncApiProductGroupImpl(get()) }
    single<IBulkInsertService<List<ProductGroupDto>>>(named("productGroupBulkInsert")) {
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
            get()
        )
    }
    //endregion CUSTOMER

    //region CUSTOMER GROUP
    single<ISyncApiService<List<CustomerGroupDto>>>(named("customerGroupImpl")){ SyncApiCustomerGroupImpl( get()) }
    single<IBulkInsertService<List<CustomerGroupDto>>>(named("customerGrouplBulkInsert")){
        CustomerGroupRawSqlBulkInsertService(
            get(named("SyncCustomerGroupEntityDbMapper")),
            get())
    }
    //endregion CUSTOMER GROUP

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
            customerBulkInsert = get(named("customerBulkInsert")),
            customerGroupApi = get(named("customerGroupImpl")),
            customerGroupBulkInsert = get(named("customerGrouplBulkInsert"))
        )
    }
}