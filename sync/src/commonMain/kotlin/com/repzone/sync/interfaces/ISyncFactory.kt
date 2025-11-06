package com.repzone.sync.interfaces

import com.repzone.core.interfaces.IUserSession
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.network.dto.CustomerDto
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.dto.DocumentMapDocumentOrganizationDto
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.network.dto.DynamicPageReportDto
import com.repzone.network.dto.EventReasonDto
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.dto.ProductDto
import com.repzone.network.dto.ProductGroupDto
import com.repzone.network.dto.RouteDto
import com.repzone.sync.model.SyncJobType

interface ISyncFactory {
    fun createJobs(productApi: ISyncApiService<List<ProductDto>>,
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
                   documentMapOrganizationsBulkInsert: IBulkInsertService<List<DocumentMapDocumentOrganizationDto>>,):Map<SyncJobType, ISyncJob>
}