package com.repzone.data.di

import com.repzone.data.mapper.AppSettingEntityDbMapper
import com.repzone.data.mapper.CampaignMasterResultRequiredProductEntityDbMapper
import com.repzone.data.mapper.CollectionLogInformationEntityDbMapper
import com.repzone.data.mapper.CrashLogInformationEntityDbMapper
import com.repzone.data.mapper.CustomDataDetailEntityDbMapper
import com.repzone.data.mapper.CustomDataHeaderEntityDbMapper
import com.repzone.data.mapper.CustomerCampaignImplementationLogInformationEntityDbMapper
import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.data.mapper.CustomerItemViewEntityDbMapper
import com.repzone.data.mapper.CustomerNoteSpEntityDbMapper
import com.repzone.data.mapper.DailyOperationLogInformationEntityDbMapper
import com.repzone.data.mapper.DocumentMapDocNumberInformationEntityDbMapper
import com.repzone.data.mapper.DownedDriveItemEntityDbMapper
import com.repzone.data.mapper.DynamicDataRowEntityDbMapper
import com.repzone.data.mapper.DynamicDataRowModelv2EntityDbMapper
import com.repzone.data.mapper.DynamicListHeadersEntityDbMapper
import com.repzone.data.mapper.DynamicListItemsEntityDbMapper
import com.repzone.data.mapper.DynamicListOrganizationEntityDbMapper
import com.repzone.data.mapper.EagleEyeLogInformationEntityDbMapper
import com.repzone.data.mapper.FormHotSaleDailyOperationInformationEntityDbMapper
import com.repzone.data.mapper.FormLogInformationEntityDbMapper
import com.repzone.data.mapper.GeoLocationEntityDbMapper
import com.repzone.data.mapper.HotSaleDailyInformationEntityDbMapper
import com.repzone.data.mapper.InventoryCountLogInformationEntityDbMapper
import com.repzone.data.mapper.InvoicePrintContentLogInformationEntityDbMapper
import com.repzone.data.mapper.NotificationLogInformationEntityDbMapper
import com.repzone.data.mapper.OrderLogInformationEntityDbMapper
import com.repzone.data.mapper.PaymentTransactionLogInformationEntityDbMapper
import com.repzone.data.mapper.PendingReportEntityDbMapper
import com.repzone.data.mapper.PrinterDocumentRelationInformationEntityDbMapper
import com.repzone.data.mapper.ProductEntityDbMapper
import com.repzone.data.mapper.ProductEntityDtoDbMapper
import com.repzone.data.mapper.ProductFlatViewEntityDbMapper
import com.repzone.data.mapper.ProductParameterEntityDbMapper
import com.repzone.data.mapper.ProductParameterv4EntityDbMapper
import com.repzone.data.mapper.RestServiceTaskEntityDbMapper
import com.repzone.data.mapper.RouteInformationViewEntityDbMapper
import com.repzone.data.mapper.SMSValidationLogInformationEntityDbMapper
import com.repzone.data.mapper.ShipmentActionLogInformationEntityDbMapper
import com.repzone.data.mapper.StockLoadingDemandLogInformationEntityDbMapper
import com.repzone.data.mapper.StockTransactionLogInformationEntityDbMapper
import com.repzone.data.mapper.SyncAppListEntityDbMapper
import com.repzone.data.mapper.SyncCampaignCustomerImplementationsEntityDbMapper
import com.repzone.data.mapper.SyncCampaignDynamicListOrganizationsEntityDbMapper
import com.repzone.data.mapper.SyncCampaignMasterEntityDbMapper
import com.repzone.data.mapper.SyncCampaignMasterResultEntityDbMapper
import com.repzone.data.mapper.SyncCampaignMasterRuleEntityDbMapper
import com.repzone.data.mapper.SyncCampaignOrganizationEntityDbMapper
import com.repzone.data.mapper.SyncCampaignResultRuleEntityDbMapper
import com.repzone.data.mapper.SyncCrmPriceListParameterEntityDbMapper
import com.repzone.data.mapper.SyncCustomerCategoryClassificationEntityDbMapper
import com.repzone.data.mapper.SyncCustomerChannelClassificationEntityDbMapper
import com.repzone.data.mapper.SyncCustomerClassClassificationEntityDbMapper
import com.repzone.data.mapper.SyncCustomerCustomFieldEntityDbMapper
import com.repzone.data.mapper.SyncCustomerEmailEntityDbMapper
import com.repzone.data.mapper.SyncCustomerEntityDbMapper
import com.repzone.data.mapper.SyncCustomerGroupEntityDbMapper
import com.repzone.data.mapper.SyncCustomerGroupProductDistributionEntityDbMapper
import com.repzone.data.mapper.SyncCustomerOrganizationScopeEntityDbMapper
import com.repzone.data.mapper.SyncCustomerProductDistributionEntityDbMapper
import com.repzone.data.mapper.SyncCustomerSegmentClassificationEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessStepEntityDbMapper
import com.repzone.data.mapper.SyncDocumentOrganizationEntityDbMapper
import com.repzone.data.mapper.SyncDynamicPageReportEntityDbMapper
import com.repzone.data.mapper.SyncEventReasonEntityDbMapper
import com.repzone.data.mapper.SyncFeedbackCategoryEntityDbMapper
import com.repzone.data.mapper.SyncFeedbackListEntityDbMapper
import com.repzone.data.mapper.SyncFormBaseEntityDbMapper
import com.repzone.data.mapper.SyncGameEntityDbMapper
import com.repzone.data.mapper.SyncManufacturerCustomersEntityDbMapper
import com.repzone.data.mapper.SyncManufacturerParameterEntityDbMapper
import com.repzone.data.mapper.SyncManufacturerRepresentativesEntityDbMapper
import com.repzone.data.mapper.SyncManufacturersEntityDbMapper
import com.repzone.data.mapper.SyncMessageUserEntityDbMapper
import com.repzone.data.mapper.SyncModuleEntityDbMapper
import com.repzone.data.mapper.SyncOrganizationEntityDbMapper
import com.repzone.data.mapper.SyncOrganizationInfoEntityDbMapper
import com.repzone.data.mapper.SyncPackageCustomFieldEntityDbMapper
import com.repzone.data.mapper.SyncPackageCustomFieldProductEntityDbMapper
import com.repzone.data.mapper.SyncPaymentPlanEntityDbMapper
import com.repzone.data.mapper.SyncProductAllocationEntityDbMapper
import com.repzone.data.mapper.SyncProductCustomFieldEntityDbMapper
import com.repzone.data.mapper.SyncProductDistributionEntityDbMapper
import com.repzone.data.mapper.SyncProductDistributionLineEntityDbMapper
import com.repzone.data.mapper.SyncProductGroupEntityDbMapper
import com.repzone.data.mapper.SyncProductImagesEntityDbMapper
import com.repzone.data.mapper.SyncProductPriceLinesEntityDbMapper
import com.repzone.data.mapper.SyncProductPricesEntityDbMapper
import com.repzone.data.mapper.SyncProductUnitEntityDbMapper
import com.repzone.data.mapper.SyncProductUnitParameterEntityDbMapper
import com.repzone.data.mapper.SyncRepresentativeAllocationEntityDbMapper
import com.repzone.data.mapper.SyncRepresentativeCustomFieldEntityDbMapper
import com.repzone.data.mapper.SyncRepresentativeProductDistributionEntityDbMapper
import com.repzone.data.mapper.SyncReservedStockEntityDbMapper
import com.repzone.data.mapper.SyncRiskDueDayEntityDbMapper
import com.repzone.data.mapper.SyncRouteAppointmentEntityDbMapper
import com.repzone.data.mapper.SyncStepAttributeEntityDbMapper
import com.repzone.data.mapper.SyncStockApprovalLogInformationEntityDbMapper
import com.repzone.data.mapper.SyncStockEntityDbMapper
import com.repzone.data.mapper.SyncTaskEntityDbMapper
import com.repzone.data.mapper.SyncTaskModelAddressEntityDbMapper
import com.repzone.data.mapper.SyncTaskStepEntityDbMapper
import com.repzone.data.mapper.SyncTenantDiscountEntityDbMapper
import com.repzone.data.mapper.SyncTransitStockEntityDbMapper
import com.repzone.data.mapper.SyncUnitEntityDbMapper
import com.repzone.data.mapper.SyncVisitActivityDefinitionEntityDbMapper
import com.repzone.data.mapper.SyncWarehouseEntityDbMapper
import com.repzone.data.mapper.SyncWarehouseTotalEntityDbMapper
import com.repzone.data.mapper.SystemLogInformationEntityDbMapper
import com.repzone.data.mapper.TaskLogInformationEntityDbMapper
import com.repzone.data.mapper.TaskVisitLogInformationEntityDbMapper
import com.repzone.data.mapper.UploadFileTaskEntityDbMapper
import com.repzone.data.mapper.VisitActivityLogInformationEntityDbMapper
import com.repzone.data.mapper.VisitEntityDbMapper
import com.repzone.data.mapper.VisitLogInformationEntityDbMapper
import com.repzone.data.repository.imp.CustomerListRepositoryImpl
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.DailyOparationRepository
import com.repzone.data.repository.imp.DistributionRepositoryPreviewImpl
import com.repzone.data.repository.imp.DocumentMapRepositoryImpl
import com.repzone.data.repository.imp.DynamicFormRepositoryImpl
import com.repzone.data.repository.imp.EventReasonRepositoryImpl
import com.repzone.data.repository.imp.MobileModuleParameterRepositoryImplPreview
import com.repzone.data.repository.imp.PaymentInformationRepositoryImpl
import com.repzone.data.repository.imp.PriceRepositoryImpl
import com.repzone.data.repository.imp.ProductRepository
import com.repzone.data.repository.imp.PromotionRuleRepository
import com.repzone.data.repository.imp.RepresentativeRepositoryImpl
import com.repzone.data.repository.imp.RouteAppointmentRepositoryImpl
import com.repzone.data.repository.imp.SettingsRepositoryImpl
import com.repzone.data.repository.imp.SyncModuleRepositoryImpl
import com.repzone.data.repository.imp.VisitRepositoryImpl
import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.model.ProductListValidator
import com.repzone.domain.document.service.DocumentSessionPreview
import com.repzone.domain.document.service.LineDiscountCalculator
import com.repzone.domain.document.service.PromotionEngine
import com.repzone.domain.document.service.StockCalculator
import com.repzone.domain.document.service.StockValidator
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.ICustomerRepository
import com.repzone.domain.repository.IDailyOparationRepository
import com.repzone.domain.repository.IDistributionRepository
import com.repzone.domain.repository.IDocumentMapRepository
import com.repzone.domain.repository.IDynamicFormRepository
import com.repzone.domain.repository.IEventReasonRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IPaymentInformationRepository
import com.repzone.domain.repository.IPriceRepository
import com.repzone.domain.repository.IProductRepository
import com.repzone.domain.repository.IPromotionRuleRepository
import com.repzone.domain.repository.IRepresentativeRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.ISettingsRepository
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.domain.repository.IVisitRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val RepositoryModulePreview = module {

    //region REPOSITORY
    singleOf(::CustomerRepositoryImpl) { bind<ICustomerRepository>() }
    singleOf(::CustomerListRepositoryImpl) { bind<ICustomerListRepository>() }
    singleOf(::SyncModuleRepositoryImpl) { bind<ISyncModuleRepository>() }
    singleOf(::MobileModuleParameterRepositoryImplPreview) { bind<IMobileModuleParameterRepository>() }
    singleOf(::RouteAppointmentRepositoryImpl) { bind<IRouteAppointmentRepository>() }
    singleOf(::EventReasonRepositoryImpl) { bind<IEventReasonRepository>() }

    factoryOf(::RepresentativeRepositoryImpl) { bind<IRepresentativeRepository>() }
    factoryOf(::VisitRepositoryImpl) { bind<IVisitRepository>() }
    factoryOf(::DocumentMapRepositoryImpl) { bind<IDocumentMapRepository>() }
    factoryOf(::DynamicFormRepositoryImpl) { bind<IDynamicFormRepository>() }
    factoryOf(::DailyOparationRepository) { bind<IDailyOparationRepository>() }

    factoryOf(::SettingsRepositoryImpl) { bind<ISettingsRepository>() }
    factoryOf(::ProductRepository) { bind<IProductRepository>() }

    factoryOf(::DistributionRepositoryPreviewImpl) { bind<IDistributionRepository>() }
    factoryOf(::PriceRepositoryImpl) { bind<IPriceRepository>() }
    factoryOf(::PaymentInformationRepositoryImpl) { bind<IPaymentInformationRepository>() }
    //endregion REPOSITORY

    //region Document Module
    singleOf(::StockCalculator)
    single {
        var settings = runBlocking {
            get<ISettingsRepository>().getStockSettings()
        }
        StockValidator(get(), settings)
    }

    single {
        var generalSettings = runBlocking {
            get<ISettingsRepository>().getGeneralSettings()
        }
        var slotConfigs = runBlocking {
            get<ISettingsRepository>().getSlotConfigs()
        }

        LineDiscountCalculator(slotConfigs, generalSettings)
    }

    factoryOf(::ProductListValidator)

    singleOf(::PromotionRuleRepository) { bind<IPromotionRuleRepository>() }
    singleOf(::PromotionEngine) {bind<IPromotionEngine>()}
    singleOf(::DocumentSessionPreview) {bind<IDocumentSession>()}
    //endregion Document Module

    //region DBMAPPERS
    //region Customer
    single { CustomerEntityDbMapper() }
    singleOf(::SyncCustomerEntityDbMapper)
    single { CustomerItemViewEntityDbMapper() }
    //endregion

    //region Product
    single { ProductEntityDbMapper() }
    single { ProductFlatViewEntityDbMapper() }
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

    //region OTHER MODULE
    includes(
        EventBusModule,
        PublineModule,
        GpsModule
    )
    //endregion OTHER MODULE
}