package com.repzone.sync.factory.newversion

import com.repzone.core.interfaces.IUserSession
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.*
import com.repzone.network.dto.form.FormBaseDto
import com.repzone.sync.interfaces.IBulkInsertService
import com.repzone.sync.interfaces.ISyncApiService
import com.repzone.sync.interfaces.ISyncFactory
import com.repzone.sync.interfaces.ISyncJob
import com.repzone.sync.job.impl.CustomerGroupPriceParametersSyncJob
import com.repzone.sync.job.impl.CustomerGroupSyncJob
import com.repzone.sync.job.impl.*
import com.repzone.sync.model.SyncJobType

class SyncJobFactory(private val syncModuleRepository: ISyncModuleRepository): ISyncFactory {
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
        modulesUserSession: IUserSession,
        eventReasonsApi: ISyncApiService<List<EventReasonDto>>,
        eventReasonsRawBulkInsert: IBulkInsertService<List<EventReasonDto>>,
        documentMapApi: ISyncApiService<List<DocumentMapModelDto>>,
        documentMapBulkInsert: IBulkInsertService<List<DocumentMapModelDto>>,
        dynamicPageReportApi: ISyncApiService<List<DynamicPageReportDto>>,
        dynamicPageReportBulkInsert: IBulkInsertService<List<DynamicPageReportDto>>,
        documentMapOrganizationsApi: ISyncApiService<List<DocumentMapDocumentOrganizationDto>>,
        documentMapOrganizationsBulkInsert: IBulkInsertService<List<DocumentMapDocumentOrganizationDto>>,
        formDefinationsApi: ISyncApiService<List<FormBaseDto>>,
        formDefinationBulkInsert: IBulkInsertService<List<FormBaseDto>>,
        formMandatoryDataApi: ISyncApiService<List<SyncMandatoryFormDto>>,
        formMandatoryDataBulkInsert: IBulkInsertService<List<SyncMandatoryFormDto>>,
        productUnitApi: ISyncApiService<List<SyncUnitDto>>,
        productUnitBulkInsert: IBulkInsertService<List<SyncUnitDto>>
    ): Map<SyncJobType, ISyncJob> {
        return mapOf(
            SyncJobType.PRODUCTS to ProductSyncJob(productApi, productBulkInsert, syncModuleRepository),
            SyncJobType.PRODUCTS_GROUP to ProductGroupSyncJob(productGroupApi, productGroupBulkInsert, syncModuleRepository),
            SyncJobType.PRODUCT_UNIT to ProductUnitJob(productUnitApi, productUnitBulkInsert, syncModuleRepository),
            SyncJobType.ROUTE to RouteDataSyncJob(routeApi, routeBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS to CustomerSyncJob(customerApi, customerBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_GROUP to CustomerGroupSyncJob(customerGroupApi, customerGroupBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_EMAIL to CustomerEmailSyncJob(customerEmailApi, customerEmailBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_PRICE_PARAMETERS to CustomerPriceParametersSyncJob(customerPriceParametersApi, customerPriceParametersBulkInsert, syncModuleRepository),
            SyncJobType.CUSTOMERS_GROUP_PRICE to CustomerGroupPriceParametersSyncJob(customerGroupPriceParametersApi, customerGroupPriceParametersBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_MODULES to PakageCustomFieldSyncJob(apiModulesApi, modulesRawBulkInsert, syncModuleRepository, modulesUserSession),
            SyncJobType.COMMON_MODULES_REASONS to EventReasonsSyncJob(eventReasonsApi, eventReasonsRawBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_DOCUMENT_MAPS to DocumentMapsSyncJob(documentMapApi, documentMapBulkInsert, syncModuleRepository),
            SyncJobType.COMMON_DYNAMIC_PAGES to DynamicPageReportSyncJob(dynamicPageReportApi,dynamicPageReportBulkInsert, syncModuleRepository),
            SyncJobType.EXTRATABLE_REPLICATION_DOCUMENTORGANIZATIONS to DocumentOrganizationsSyncJob(documentMapOrganizationsApi, documentMapOrganizationsBulkInsert, syncModuleRepository),
            SyncJobType.FORM to FormDataFetchSyncJob(formDefinationsApi, formDefinationBulkInsert, syncModuleRepository),
            SyncJobType.FORM_MANDATORY to FormMandatoryDataSyncJob(formMandatoryDataApi, formMandatoryDataBulkInsert, syncModuleRepository)
        )
    }
    //endregion

}