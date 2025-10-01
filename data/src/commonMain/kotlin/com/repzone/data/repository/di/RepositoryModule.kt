package com.repzone.data.repository.di

import com.repzone.data.mapper.*
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.ProductRepositoryImpl
import com.repzone.data.repository.imp.SyncModuleRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.database.ProductParameterEntity
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.SyncModuleEntity
import com.repzone.database.SyncProductEntity
import com.repzone.domain.model.ProductParameterModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.repository.*
import org.koin.dsl.module

val RepositoryModule = module {


    //region Customer
    single { CustomerEntityDbMapper() }
    single<Mapper<SyncCustomerEntity, SyncCustomerModel>> { CustomerEntityDbMapper() }
    single<ICustomerRepository> { CustomerRepositoryImpl(get(), get()) }
    //endregion

    //region Product
    single { ProductEntityDbMapper() }
    single<Mapper<SyncProductEntity, SyncProductModel>> { ProductEntityDbMapper() }
    single<IProductRepository> { ProductRepositoryImpl(get(), get()) }
    //endregion

    //region SyncModule
    single { SyncModuleEntityDbMapper() }
    single<Mapper<SyncModuleEntity, SyncModuleModel>> { SyncModuleEntityDbMapper() }
    single<ISyncModuleRepository> { SyncModuleRepositoryImpl(get(), get()) }
    //endregion

    //region ProductParameters
    single { ProductParameterEntityDbMapper() }
    single<Mapper<ProductParameterEntity, ProductParameterModel>> { ProductParameterEntityDbMapper() }
    //endregion

}