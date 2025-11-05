package com.repzone.data.di

import com.repzone.data.mapper.*
import com.repzone.data.repository.imp.VisitRepositoryImpl
import com.repzone.data.repository.imp.EventReasonRepositoryImpl
import com.repzone.data.repository.imp.CustomerListRepositoryImpl
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.DocumentMapRepositoryImpl
import com.repzone.data.repository.imp.MobileModuleParameterRepositoryImpl
import com.repzone.data.repository.imp.RepresentativeRepositoryImpl
import com.repzone.data.repository.imp.RouteAppointmentRepositoryImpl
import com.repzone.data.repository.imp.SyncModuleRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.*
import com.repzone.domain.model.*
import com.repzone.domain.repository.*
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.network.dto.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RepositoryModule = module {

    //region REPOSITORY
    single<ICustomerRepository> { CustomerRepositoryImpl(get(), get()) }
    single<ICustomerListRepository> {
        CustomerListRepositoryImpl(get(),
            get(),
            get(),
            get(),
            get()) }
    single<ISyncModuleRepository> {
        SyncModuleRepositoryImpl(
            get(),
            get()) }

    single<IMobileModuleParameterRepository>{
        MobileModuleParameterRepositoryImpl(
            get(),
            get(),
            get()) }
    single<IRouteAppointmentRepository> { RouteAppointmentRepositoryImpl(get(), get()) }
    single<IEventReasonRepository> { EventReasonRepositoryImpl(get()) }
    factory<IRepresentativeRepository>{ RepresentativeRepositoryImpl(get(), get()) }
    factory<IVisitRepository>{ VisitRepositoryImpl(get(), get()) }
    factory<IDocumentMapRepository>{ DocumentMapRepositoryImpl(get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get())
    }
    //endregion REPOSITORY

    //region DBMAPPERS
    //region Customer
    single { CustomerEntityDbMapper() }
    single { CustomerEntityDbMapper() }
    single { CustomerItemViewEntityDbMapper() }
    //endregion

    //region Product
    single { ProductEntityDbMapper() }
    //endregion

    //region SyncModule
    single { SyncModuleEntityDbMapper() }
    //endregion

    //region MobileRoute
    single { SyncRouteAppointmentEntityDbMapper() }
    single { RouteInformationViewEntityDbMapper() }
    //endregion MobileRoute

    //region Adress
    //endregion Adress

    //region ProductParameters
    single { ProductEntityDtoDbMapper() }
    //endregion

    //region ProductGroup
    single { SyncProductGroupEntityDbMapper() }
    //endregion

    //region AppList
    single { SyncAppListEntityDbMapper() }
    //endregion

    //region AppSetting
    single { AppSettingEntityDbMapper() }
    //endregion

    //region CampaignCustomerImplementations
    single { SyncCampaignCustomerImplementationsEntityDbMapper() }
    //endregion

    //region CampaignDynamicListOrganizations
    single { SyncCampaignDynamicListOrganizationsEntityDbMapper() }
    //endregion

    //region CampaignMaster
    single { SyncCampaignMasterEntityDbMapper() }
    //endregion

    //region CampaignMasterResult
    single { SyncCampaignMasterResultEntityDbMapper() }
    //endregion

    //region CampaignMasterResultRequiredProduct
    single { CampaignMasterResultRequiredProductEntityDbMapper() }
    //endregion

    //region CampaignMasterRule
    single { SyncCampaignMasterRuleEntityDbMapper() }
    //endregion

    //region CampaignOrganization
    single { SyncCampaignOrganizationEntityDbMapper() }
    //endregion

    //region CampaignResultRule
    single { SyncCampaignResultRuleEntityDbMapper() }
    //endregion

    //region CollectionLogInformation
    single { CollectionLogInformationEntityDbMapper() }
    //endregion

    //region CrashLogInformation
    single { CrashLogInformationEntityDbMapper() }
    //endregion

    //region CrmPriceListParameter
    single { SyncCrmPriceListParameterEntityDbMapper() }
    //endregion

    //region CustomDataDetail
    single { CustomDataDetailEntityDbMapper() }
    //endregion

    //region CustomDataHeader
    single { CustomDataHeaderEntityDbMapper() }
    //endregion

    //region CustomerCampaignImplementationLogInformation
    single { CustomerCampaignImplementationLogInformationEntityDbMapper() }
    //endregion

    //region CustomerCategoryClassification
    single { SyncCustomerCategoryClassificationEntityDbMapper() }
    //endregion

    //region CustomerChannelClassification
    single { SyncCustomerChannelClassificationEntityDbMapper() }
    //endregion

    //region CustomerClassClassification
    single { SyncCustomerClassClassificationEntityDbMapper() }
    //endregion

    //region CustomerCustomField
    single { SyncCustomerCustomFieldEntityDbMapper() }
    //endregion

    //region CustomerEmail
    single { SyncCustomerEmailEntityDbMapper() }
    //endregion

    //region CustomerGroup
    single { SyncCustomerGroupEntityDbMapper() }
    //endregion

    //region CustomerGroupProductDistribution
    single { SyncCustomerGroupProductDistributionEntityDbMapper() }
    //endregion

    //region CustomerNoteSp
    single { CustomerNoteSpEntityDbMapper() }
    //endregion

    //region CustomerOrganizationScope
    single { SyncCustomerOrganizationScopeEntityDbMapper() }
    //endregion

    //region CustomerProductDistribution
    single { SyncCustomerProductDistributionEntityDbMapper() }
    //endregion

    //region CustomerSegmentClassification
    single { SyncCustomerSegmentClassificationEntityDbMapper() }
    //endregion

    //region DailyOperationLogInformation
    single { DailyOperationLogInformationEntityDbMapper() }
    //endregion

    //region DocumentMap
    single { SyncDocumentMapEntityDbMapper() }
    //endregion

    //region DocumentMapDocNumberInformation
    single { DocumentMapDocNumberInformationEntityDbMapper() }
    //endregion

    //region DocumentMapProcess
    single { SyncDocumentMapProcessEntityDbMapper() }
    //endregion

    //region DocumentMapProcessStep
    single { SyncDocumentMapProcessStepEntityDbMapper() }
    //endregion

    //region DocumentOrganization
    single { SyncDocumentOrganizationEntityDbMapper() }
    //endregion

    //region DownedDriveItem
    single { DownedDriveItemEntityDbMapper() }
    //endregion

    //region DynamicDataRow
    single { DynamicDataRowEntityDbMapper() }
    //endregion

    //region DynamicDataRowModelv2
    single { DynamicDataRowModelv2EntityDbMapper() }
    //endregion

    //region DynamicListHeaders
    single { DynamicListHeadersEntityDbMapper() }
    //endregion

    //region DynamicListItems
    single { DynamicListItemsEntityDbMapper() }
    //endregion

    //region DynamicListOrganization
    single { DynamicListOrganizationEntityDbMapper() }
    //endregion

    //region DynamicPageReport
    single { SyncDynamicPageReportEntityDbMapper() }
    //endregion

    //region EagleEyeLogInformation
    single { EagleEyeLogInformationEntityDbMapper() }
    //endregion

    //region EventReason
    single { SyncEventReasonEntityDbMapper() }
    //endregion

    //region FeedbackCategory
    single { SyncFeedbackCategoryEntityDbMapper() }
    //endregion

    //region FeedbackList
    single { SyncFeedbackListEntityDbMapper() }
    //endregion

    //region FormBase
    single { SyncFormBaseEntityDbMapper() }
    //endregion

    //region FormHotSaleDailyOperationInformation
    single { FormHotSaleDailyOperationInformationEntityDbMapper() }
    //endregion

    //region FormLogInformation
    single { FormLogInformationEntityDbMapper() }
    //endregion

    //region Game
    single { SyncGameEntityDbMapper() }
    //endregion

    //region GeoLocation
    single { GeoLocationEntityDbMapper() }
    //endregion

    //region HotSaleDailyInformation
    single { HotSaleDailyInformationEntityDbMapper() }
    //endregion

    //region InventoryCountLogInformation
    single { InventoryCountLogInformationEntityDbMapper() }
    //endregion

    //region InvoicePrintContentLogInformation
    single { InvoicePrintContentLogInformationEntityDbMapper() }
    //endregion

    //region ManufacturerCustomers
    single { SyncManufacturerCustomersEntityDbMapper() }
    //endregion

    //region ManufacturerParameter
    single { SyncManufacturerParameterEntityDbMapper() }
    //endregion

    //region ManufacturerRepresentatives
    single { SyncManufacturerRepresentativesEntityDbMapper() }
    //endregion

    //region Manufacturers
    single { SyncManufacturersEntityDbMapper() }
    //endregion

    //region MessageUser
    single { SyncMessageUserEntityDbMapper() }
    //endregion

    //region Module
    single { SyncModuleEntityDbMapper() }
    //endregion

    //region NotificationLogInformation
    single { NotificationLogInformationEntityDbMapper() }
    //endregion

    //region OrderLogInformation
    single { OrderLogInformationEntityDbMapper() }
    //endregion

    //region Organization
    single { SyncOrganizationEntityDbMapper() }
    //endregion

    //region OrganizationInfo
    single { SyncOrganizationInfoEntityDbMapper() }
    //endregion

    //region PackageCustomField
    single { SyncPackageCustomFieldEntityDbMapper() }
    //endregion

    //region PackageCustomFieldProduct
    single { SyncPackageCustomFieldProductEntityDbMapper() }
    //endregion

    //region PaymentPlan
    single { SyncPaymentPlanEntityDbMapper() }
    //endregion

    //region PaymentTransactionLogInformation
    single { PaymentTransactionLogInformationEntityDbMapper() }
    //endregion

    //region PendingReport
    single { PendingReportEntityDbMapper() }
    //endregion

    //region PrinterDocumentRelationInformation
    single { PrinterDocumentRelationInformationEntityDbMapper() }
    //endregion

    //region ProductAllocation
    single { SyncProductAllocationEntityDbMapper() }
    //endregion

    //region ProductCustomField
    single { SyncProductCustomFieldEntityDbMapper() }
    //endregion

    //region ProductDistribution
    single { SyncProductDistributionEntityDbMapper() }
    //endregion

    //region ProductDistributionLine
    single { SyncProductDistributionLineEntityDbMapper() }
    //endregion

    //region ProductGroup
    single { SyncProductGroupEntityDbMapper() }
    //endregion

    //region ProductImages
    single { SyncProductImagesEntityDbMapper() }
    //endregion

    //region ProductParameter
    single { ProductParameterEntityDbMapper() }
    //endregion

    //region ProductParameterv4
    single { ProductParameterv4EntityDbMapper() }
    //endregion

    //region ProductPriceLines
    single { SyncProductPriceLinesEntityDbMapper() }
    //endregion

    //region ProductPrices
    single { SyncProductPricesEntityDbMapper() }
    //endregion

    //region ProductUnit
    single { SyncProductUnitEntityDbMapper() }
    //endregion

    //region ProductUnitParameter
    single { SyncProductUnitParameterEntityDbMapper() }
    //endregion

    //region RepresentativeAllocation
    single { SyncRepresentativeAllocationEntityDbMapper() }
    //endregion

    //region RepresentativeCustomField
    single { SyncRepresentativeCustomFieldEntityDbMapper() }
    //endregion

    //region RepresentativeProductDistribution
    single { SyncRepresentativeProductDistributionEntityDbMapper() }
    //endregion

    //region ReservedStock
    single  { SyncReservedStockEntityDbMapper() }
    //endregion

    //region RestServiceTask
    single  { RestServiceTaskEntityDbMapper() }
    //endregion

    //region RiskDueDay
    single { SyncRiskDueDayEntityDbMapper() }
    //endregion

    //region SMSValidationLogInformation
    single { SMSValidationLogInformationEntityDbMapper() }
    //endregion

    //region ShipmentActionLogInformation
    single { ShipmentActionLogInformationEntityDbMapper() }
    //endregion

    //region StepAttribute
    single { SyncStepAttributeEntityDbMapper() }
    //endregion

    //region Stock
    single { SyncStockEntityDbMapper() }
    //endregion

    //region StockApprovalLogInformation
    single { SyncStockApprovalLogInformationEntityDbMapper() }
    //endregion

    //region StockLoadingDemandLogInformation
    single { StockLoadingDemandLogInformationEntityDbMapper() }
    //endregion

    //region StockTransactionLogInformation
    single { StockTransactionLogInformationEntityDbMapper() }
    //endregion

    //region SystemLogInformation
    single { SystemLogInformationEntityDbMapper() }
    //endregion

    //region Task
    single { SyncTaskEntityDbMapper() }
    //endregion

    //region TaskLogInformation
    single { TaskLogInformationEntityDbMapper() }
    //endregion

    //region TaskModelAddress
    single { SyncTaskModelAddressEntityDbMapper() }
    //endregion

    //region TaskStep
    single { SyncTaskStepEntityDbMapper() }
    //endregion

    //region TaskVisitLogInformation
    single { TaskVisitLogInformationEntityDbMapper() }
    //endregion

    //region TenantDiscount
    single { SyncTenantDiscountEntityDbMapper() }
    //endregion

    //region TransitStock
    single { SyncTransitStockEntityDbMapper() }
    //endregion

    //region Unit
    single { SyncUnitEntityDbMapper() }
    //endregion

    //region UploadFileTask
    single { UploadFileTaskEntityDbMapper() }
    //endregion

    //region Visit
    single { VisitEntityDbMapper() }
    //endregion

    //region VisitActivityDefinition
    single { SyncVisitActivityDefinitionEntityDbMapper() }
    //endregion

    //region VisitActivityLogInformation
    single { VisitActivityLogInformationEntityDbMapper() }
    //endregion

    //region VisitLogInformation
    single { VisitLogInformationEntityDbMapper() }
    //endregion

    //region Warehouse
    single { SyncWarehouseEntityDbMapper() }
    //endregion

    //region WarehouseTotal
    single { SyncWarehouseTotalEntityDbMapper() }
    //endregion
    //endregion DBMAPPERS
}