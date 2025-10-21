package com.repzone.sync.di

import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.network.dto.CustomerDto
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.network.dto.DynamicPageReportDto
import com.repzone.network.dto.EventReasonDto
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.dto.ProductDto
import com.repzone.network.dto.ProductGroupDto
import com.repzone.network.dto.RouteDto
import com.repzone.sync.factory.newversion.SyncJobFactory
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncFactory
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.manager.SyncManagerImpl
import com.repzone.sync.service.api.impl.SyncApiCustomerEmailImpl
import com.repzone.sync.service.api.impl.*
import com.repzone.sync.service.bulk.impl.CustomerEmailRawSqlBulkInsertService
import com.repzone.sync.service.bulk.impl.CustomerGroupPriceParameterslRawSqlBulkInsertService
import com.repzone.sync.service.bulk.impl.*
import com.repzone.sync.transaction.TransactionCoordinator
import org.koin.core.qualifier.named
import org.koin.dsl.module

val SyncModule = module {
    single<ISyncFactory> { SyncJobFactory(get()) }

    single { TransactionCoordinator(get(), get()) }

    //region GENERAL
    single<ISyncManager>{ SyncManagerImpl(get()) }
    //endregion GENERAL


    //region ROUTEDATA
    single<IBulkInsertService<List<RouteDto>>>(named("routeDataBulkInsert")) {
        RouteDataRawSqlBulkInsertService(get(named("SyncRouteAppointmentEntityDbMapper")), get())
    }
    single<ISyncApiService<List<RouteDto>>>(named("routeDataSyncApi")){ SyncApiRouteDataImpl(get()) }
    //endregion ROUTEDATA

    //region PRODUCT
    single<IBulkInsertService<List<ProductDto>>>(named("productBulkInsert")) { ProductRawSqlBulkInsertService(get(), get()) }
    single<ISyncApiService<List<ProductDto>>>(named("productSyncApi")){ SyncApiProductImpl(get()) }
    //endregion PRODUCT

    //region PRODUCT GROUP
    single<ISyncApiService<List<ProductGroupDto>>>(named("productGroupSyncApi")){ SyncApiProductGroupImpl(get()) }
    single<IBulkInsertService<List<ProductGroupDto>>>(named("productGroupBulkInsert")) { ProductGroupRawSqlBulkInsertService(get(), get()) }
    //endregion PRODUCT GROUP

    //region CUSTOMER
    single<ISyncApiService<List<CustomerDto>>>(named("customerGroupSyncApi")){ SyncApiCustomerImpl( get()) }
    single<IBulkInsertService<List<CustomerDto>>>(named("customerBulkInsert")) { CustomerRawSqlBulkInsertService(get(), get()) }
    //endregion CUSTOMER

    //region CUSTOMER GROUP
    single<ISyncApiService<List<CustomerGroupDto>>>(named("customerGroupImpl")){ SyncApiCustomerGroupImpl( get()) }
    single<IBulkInsertService<List<CustomerGroupDto>>>(named("customerGrouplBulkInsert")){ CustomerGroupRawSqlBulkInsertService(
        get(named("SyncCustomerGroupEntityDbMapper")),
        get()) }
    //endregion CUSTOMER GROUP

    //region CUSTOMER EMAIL
    single<ISyncApiService<List<CustomerEmailDto>>>(named("customerEmailImpl")){ SyncApiCustomerEmailImpl(get()) }
    single<IBulkInsertService<List<CustomerEmailDto>>>(named("customerEmailBulkInsert")){ CustomerEmailRawSqlBulkInsertService(
        get(named("SyncCustomerEmailEntityDbMapper")),
        get()) }
    //endregion CUSTOMER EMAIL

    //region CUSTOMER PRICE PARAMETERS
    single<ISyncApiService<List<CrmPriceListParameterDto>>>(named("customerPriceParametersImpl")){ SyncApiCustomerPriceParametersImpl(get()) }
    single<IBulkInsertService<List<CrmPriceListParameterDto>>>(named("customerEmailBulkInsert")){
        CustomerPriceParameterslRawSqlBulkInsertService(get(named("SyncCrmPriceListParameterEntityDbMapper")), get()) }
    //endregion CUSTOMER PRICE PARAMETERS

    //region CUSTOMER GROUP PRICE PARAMETERS
    single<ISyncApiService<List<CrmPriceListParameterDto>>>(named("customerGroupPriceParametersImpl")){ SyncApiCustomerGroupPriceParametersImpl(get()) }
    single<IBulkInsertService<List<CrmPriceListParameterDto>>>(named("customerGroupEmailBulkInsert")){ CustomerGroupPriceParameterslRawSqlBulkInsertService(
        get(named("SyncCrmPriceListParameterEntityDbMapper")),
        get()) }
    //endregion CUSTOMER GROUP PRICE PARAMETERS

    //region COMMON MODULES
    single<ISyncApiService<List<PackageCustomFieldDto>>>(named("syncModulesImpl")){ SyncApiModulesImpl(get()) }
    single<IBulkInsertService<List<PackageCustomFieldDto>>>(named("syncModulesBulkInsert")){ ModulesRawSqlBulkInsertService(
        get(named("SyncPackageCustomFieldEntityDbMapper")),
        get(named("SyncPackageCustomFieldProductEntityDbMapper")),
        get()) }
    //endregion COMMON MODULES

    //region COMMON REASONS
    single<ISyncApiService<List<EventReasonDto>>>(named("syncApiEventReasonsImpl")){ SyncApiEventReasonsImpl(get()) }
    single<IBulkInsertService<List<EventReasonDto>>>(named("syncEventReasonBulkInsert")){ EventReasonsRawSqlBulkInsertService(
        get(named("SyncEventReasonEntityDbMapper")),
        get()) }
    //endregion COMMON REASONS

    //region COMMON DOCUMENT
    single<ISyncApiService<List<DocumentMapModelDto>>>(named("syncApiDocumentMapsImpl")) { SyncApiDocumentMapsImpl(get()) }
    single<IBulkInsertService<List<DocumentMapModelDto>>>(named("syncDocumentMapsRawSqlBulkInsertService")){ DocumentMapsRawSqlBulkInsertService(
        get(named("SyncDocumentMapEntityDbMapper")),
        get(),
        get(),
        get()) }
    //endregion COMMON DOCUMENT

    //region COMMON DYNAMIC PAGES
    single<ISyncApiService<List<DynamicPageReportDto>>>(named("syncApiDynamicPageReportImpl")) { SyncApiDynamicPageReportImpl(get()) }
    single<IBulkInsertService<List<DynamicPageReportDto>>>(named("syncDynamicPageReportRawSqlBulkInsertService")) { DynamicPageReportRawSqlBulkInsertService(
        get(named("SyncDynamicPageReportEntityDbMapper")),
        get()) }
    //endregion COMMON DYNAMIC PAGES

    single {
        get<ISyncFactory>().createJobs(
            productApi = get(named("productSyncApi")),
            productBulkInsert = get(named("productBulkInsert")),
            productGroupApi = get(named("productGroupSyncApi")),
            productGroupBulkInsert = get(named("productGroupBulkInsert")),
            routeApi = get(named("routeDataSyncApi")),
            routeBulkInsert = get(named("routeDataBulkInsert")),
            customerApi = get(named("customerGroupSyncApi")),
            customerBulkInsert = get(named("customerBulkInsert")),
            customerGroupApi = get(named("customerGroupImpl")),
            customerGroupBulkInsert = get(named("customerGrouplBulkInsert")),
            customerEmailApi = get(named("customerEmailImpl")),
            customerEmailBulkInsert = get(named("customerEmailBulkInsert")),
            customerPriceParametersApi = get(named("customerPriceParametersImpl")),
            customerPriceParametersBulkInsert = get(named("customerEmailBulkInsert")),
            customerGroupPriceParametersApi = get(named("customerGroupPriceParametersImpl")),
            customerGroupPriceParametersBulkInsert = get(named("customerGroupEmailBulkInsert")),
            apiModulesApi = get(named("syncModulesImpl")),
            modulesRawBulkInsert = get(named("syncModulesBulkInsert")),
            modulesUserSession = get(),
            eventReasonsApi = get(named("syncApiEventReasonsImpl")),
            eventReasonsRawBulkInsert = get(named("syncEventReasonBulkInsert")),
            documentMapApi = get(named("syncApiDocumentMapsImpl")),
            documentMapBulkInsert = get(named("syncDocumentMapsRawSqlBulkInsertService")),
            dynamicPageReportApi = get(named("syncApiDynamicPageReportImpl")),
            dynamicPageReportBulkInsert = get(named("syncDynamicPageReportRawSqlBulkInsertService")))
    }
}