package com.repzone.data.di

import com.repzone.data.mapper.*
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.ProductRepositoryImpl
import com.repzone.data.repository.imp.SyncModuleRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.SyncModuleEntity
import com.repzone.database.SyncProductEntity
import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.model.SyncRouteAppointmentModel
import com.repzone.domain.repository.*
import com.repzone.network.dto.MobileRouteDto
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RepositoryModule = module {


    //region Customer
    single<Mapper<SyncCustomerEntity, SyncCustomerModel>>(named("CustomerEntityDbMapperInterface")) { CustomerEntityDbMapper() }
    single<ICustomerRepository> { CustomerRepositoryImpl(get(named("CustomerEntityDbMapperInterface")), get()) }
    //endregion

    //region Product
    single<Mapper<SyncProductEntity, SyncProductModel>>(named("ProductEntityDbMapperInterface")) { ProductEntityDbMapper() }
    single<IProductRepository> { ProductRepositoryImpl(get(named("ProductEntityDbMapperInterface")), get()) }
    //endregion

    //region SyncModule
    single<Mapper<SyncModuleEntity, SyncModuleModel>>(named("SyncModuleEntityDbMapper")) { SyncModuleEntityDbMapper() }
    single<ISyncModuleRepository> { SyncModuleRepositoryImpl(get(named("SyncModuleEntityDbMapper")), get()) }
    //endregion

    //region MobileRoute
    single<MapperDto<SyncRouteAppointmentEntity, SyncRouteAppointmentModel, MobileRouteDto>>(named("SyncRouteAppointmentEntityDbMapper")) { SyncRouteAppointmentEntityDbMapper() }
    //endregion MobileRoute

    //region Adress
    //endregion Adress


    //region ProductParameters
    single { ProductEntityDtoDbMapper() }
    //endregion

    //region ProductGroup
    single { SyncProductGroupEntityDbMapper() }
    //endregion

}