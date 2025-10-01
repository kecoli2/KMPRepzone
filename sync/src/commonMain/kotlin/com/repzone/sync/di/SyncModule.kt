package com.repzone.sync.di

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.network.dto.MobileProductDto
import com.repzone.sync.factory.SyncJobFactory
import com.repzone.sync.impl.SyncApiCustomerImpl
import com.repzone.sync.impl.SyncApiProductImpl
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.manager.SyncManagerImpl
import com.repzone.sync.service.CustomerRawSqlBulkInsertService
import com.repzone.sync.service.ProductRawSqlBulkInsertService
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SyncModule = module {
    single { SyncJobFactory(get()) }

    single { TransactionCoordinator(get(), get()) }

    //PRODUCT
    single<IBulkInsertService<MobileProductDto>>(named("productBulkInsert")) { ProductRawSqlBulkInsertService(get(),get()) }
    single<ISyncApiService<MobileProductDto>>(named("productSyncApi")){ SyncApiProductImpl(get()) }

    //CUSTOMER
    single<IBulkInsertService<SyncCustomerModel>>(named("customerBulkInsert")){ CustomerRawSqlBulkInsertService(get(named("CustomerEntityDbMapperInterface")), get()) }
    single<ISyncApiService<SyncCustomerModel>>(named("customerSyncApi")){ SyncApiCustomerImpl(get()) }

    //GENERAL
    single<ISyncManager>{ SyncManagerImpl(get()) }

    single {
        get<SyncJobFactory>().createJobs(
            productApi = get(named("productSyncApi")),
            customerApi = get(named("customerSyncApi")),
            productBulkInsert = get(named("productBulkInsert")),
            customerBulkInsert = get(named("customerBulkInsert"))
        )
    }
}