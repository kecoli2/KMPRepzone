package com.repzone.sync.factory.newversion

import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.*
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncFactory
import com.repzone.sync.interfaces.ISyncJob
import com.repzone.sync.job.impl.newversion.*
import com.repzone.sync.model.SyncJobType

class SyncJobFactory(private val syncModuleRepository: ISyncModuleRepository): ISyncFactory {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun createJobs(
        productApi: ISyncApiService<List<ProductDto>>,
        productBulkInsert: IBulkInsertService<List<ProductDto>>,
        productGroupApi: ISyncApiService<List<ProductGroupDto>>,
        productGroupBulkInsert: IBulkInsertService<List<ProductGroupDto>>,
        routeApi: ISyncApiService<List<RouteDto>>,
        routeBulkInsert: IBulkInsertService<List<RouteDto>>,
        customerApi: ISyncApiService<List<CustomerDto>>,
        customerBulkInsert: IBulkInsertService<List<CustomerDto>>,
        customerGroupApi: ISyncApiService<List<CustomerGroupDto>>,
        customerGroupBulkInsert: IBulkInsertService<List<CustomerGroupDto>>,
        customerEmailApi: ISyncApiService<List<CustomerEmailDto>>,
        customerEmailBulkInsert: IBulkInsertService<List<CustomerEmailDto>>,
        customerPriceParametersApi: ISyncApiService<List<CrmPriceListParameterDto>>,
        customerPriceParametersBulkInsert: IBulkInsertService<List<CrmPriceListParameterDto>>,
        customerGroupPriceParametersApi: ISyncApiService<List<CrmPriceListParameterDto>>,
        customerGroupPriceParametersBulkInsert: IBulkInsertService<List<CrmPriceListParameterDto>>,
        apiModulesApi: ISyncApiService<List<PackageCustomFieldDto>>,
        modulesRawBulkInsert: IBulkInsertService<List<PackageCustomFieldDto>>,
        eventReasonsApi: ISyncApiService<List<EventReasonDto>>,
        eventReasonsRawBulkInsert: IBulkInsertService<List<EventReasonDto>>,
        documentMapApi: ISyncApiService<List<DocumentMapModelDto>>,
        documentMapBulkInsert: IBulkInsertService<List<DocumentMapModelDto>>,
        dynamicPageReportApi: ISyncApiService<List<DynamicPageReportDto>>,
        dynamicPageReportBulkInsert: IBulkInsertService<List<DynamicPageReportDto>>
    ): Map<SyncJobType, ISyncJob> {
        return mapOf(
            SyncJobType.PRODUCTS to ProductSyncJob(
                productApi,
                productBulkInsert,
                syncModuleRepository
            ),
            SyncJobType.PRODUCTS_GROUP to ProductGroupSyncJob(productGroupApi, productGroupBulkInsert, syncModuleRepository),
            SyncJobType.ROUTE to RouteDataSyncJob(routeApi, routeBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS to CustomerSyncJob(customerApi, customerBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_GROUP to CustomerGroupSyncJob(customerGroupApi, customerGroupBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_EMAIL to CustomerEmailSyncJob(customerEmailApi, customerEmailBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_PRICE_PARAMETERS to CustomerPriceParametersSyncJob(customerPriceParametersApi, customerPriceParametersBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_GROUP_PRICE to CustomerGroupPriceParametersSyncJob(customerGroupPriceParametersApi, customerGroupPriceParametersBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_MODULES to PakageCustomFieldSyncJob(apiModulesApi, modulesRawBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_MODULES_REASONS to EventReasonsSyncJob(eventReasonsApi, eventReasonsRawBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_DOCUMENT_MAPS to DocumentMapsSyncJob(documentMapApi, documentMapBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_DYNAMIC_PAGES to DynamicPageReportSyncJob(dynamicPageReportApi,dynamicPageReportBulkInsert, syncModuleRepository))
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}