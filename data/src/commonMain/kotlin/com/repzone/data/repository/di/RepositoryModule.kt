package com.repzone.data.repository.di

import com.repzone.data.mapper.ProductEntityDbMapper
import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.ProductRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.SyncProductEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.repository.ICustomerRepository
import com.repzone.domain.repository.IProductRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single { CustomerEntityDbMapper() }
    single<Mapper<SyncCustomerEntity, SyncCustomerModel>> { CustomerEntityDbMapper() }
    single<ICustomerRepository> { CustomerRepositoryImpl(get(), get(), get()) }

    single { ProductEntityDbMapper() }
    single<Mapper<SyncProductEntity, SyncProductModel>> { ProductEntityDbMapper() }
    single<IProductRepository> { ProductRepositoryImpl(get(), get(), get()) }


}