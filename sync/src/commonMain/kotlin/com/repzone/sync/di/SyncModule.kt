package com.repzone.sync.di

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncProductModel
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
import org.koin.dsl.module

val SyncModule = module {
    single { SyncJobFactory(get()) }

    single { TransactionCoordinator(get(), get()) }

    //PRODUCT
    single<IBulkInsertService<SyncProductModel>> { ProductRawSqlBulkInsertService(get(),get()) }
    single<ISyncApiService<SyncProductModel>>{ SyncApiProductImpl(get()) }

    //CUSTOMER
    single<IBulkInsertService<SyncCustomerModel>>{ CustomerRawSqlBulkInsertService(get(), get()) }
    single<ISyncApiService<SyncCustomerModel>>{ SyncApiCustomerImpl(get()) }

    //GENERAL
    single<ISyncManager>{ SyncManagerImpl(get()) }

    single {
        get<SyncJobFactory>().createJobs(
            productApi = get(),
            customerApi = get(),
            productBulkInsert = get(),
            customerBulkInsert = get()
        )
    }
}