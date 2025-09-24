package com.repzone.data.repository.di

import com.repzone.data.mapper.SyncCustomerEntityDbMapper
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ICustomerRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single<Mapper<SyncCustomerEntity, SyncCustomerModel>> { SyncCustomerEntityDbMapper() }
    single<ICustomerRepository> { CustomerRepositoryImpl(get(), get(), get()) }
}