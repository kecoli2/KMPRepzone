package com.repzone.sync.di

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncProductModel
import com.repzone.sync.factory.SyncJobFactory
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.service.CustomerRawSqlBulkInsertService
import com.repzone.sync.service.ProductRawSqlBulkInsertService
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.dsl.module

val SyncModule = module {
    single { TransactionCoordinator(get(), get()) }

    single<IBulkInsertService<SyncProductModel>> {
        ProductRawSqlBulkInsertService(
            get(),
            get()
        )
    }

    single<IBulkInsertService<SyncCustomerModel>>{
        CustomerRawSqlBulkInsertService(
            get(),
            get())
    }

    single {
        get<SyncJobFactory>().createJobs(
            productApi = get(),
            customerApi = get(),
            productBulkInsert = get(), // ← Bulk service injection
            customerBulkInsert = get()
        )
    }
}