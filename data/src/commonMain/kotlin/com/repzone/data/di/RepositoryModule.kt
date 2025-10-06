package com.repzone.data.di

import com.repzone.data.mapper.*
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.ProductRepositoryImpl
import com.repzone.data.repository.imp.SyncModuleRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.*
import com.repzone.domain.model.*
import com.repzone.domain.repository.*
import com.repzone.network.dto.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RepositoryModule = module {


    //region Customer
    single<MapperDto<SyncCustomerEntity, SyncCustomerModel, CustomerDto>>(named("CustomerEntityDbMapperInterface")) { CustomerEntityDbMapper() }
    single<ICustomerRepository> { CustomerRepositoryImpl(get(named("CustomerEntityDbMapperInterface")), get()) }
    single{ CustomerEntityDbMapper() }
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
    single<MapperDto<SyncRouteAppointmentEntity, SyncRouteAppointmentModel, RouteDto>>(named("SyncRouteAppointmentEntityDbMapper")) { SyncRouteAppointmentEntityDbMapper() }
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
    single<Mapper<SyncAppListEntity, SyncAppListModel>>(named("SyncAppListEntityDbMapper")) { SyncAppListEntityDbMapper() }
    //endregion

    //region AppSetting
    single<Mapper<AppSettingEntity, AppSettingModel>>(named("AppSettingEntityDbMapper")) { AppSettingEntityDbMapper() }
    //endregion

    //region CampaignCustomerImplementations
    single<Mapper<SyncCampaignCustomerImplementationsEntity, SyncCampaignCustomerImplementationsModel>>(named("SyncCampaignCustomerImplementationsEntityDbMapper")) { SyncCampaignCustomerImplementationsEntityDbMapper() }
    //endregion

    //region CampaignDynamicListOrganizations
    single<Mapper<SyncCampaignDynamicListOrganizationsEntity, SyncCampaignDynamicListOrganizationsModel>>(named("SyncCampaignDynamicListOrganizationsEntityDbMapper")) { SyncCampaignDynamicListOrganizationsEntityDbMapper() }
    //endregion

    //region CampaignMaster
    single<Mapper<SyncCampaignMasterEntity, SyncCampaignMasterModel>>(named("SyncCampaignMasterEntityDbMapper")) { SyncCampaignMasterEntityDbMapper() }
    //endregion

    //region CampaignMasterResult
    single<Mapper<SyncCampaignMasterResultEntity, SyncCampaignMasterResultModel>>(named("SyncCampaignMasterResultEntityDbMapper")) { SyncCampaignMasterResultEntityDbMapper() }
    //endregion

    //region CampaignMasterResultRequiredProduct
    single<Mapper<CampaignMasterResultRequiredProductEntity, CampaignMasterResultRequiredProductModel>>(named("CampaignMasterResultRequiredProductEntityDbMapper")) { CampaignMasterResultRequiredProductEntityDbMapper() }
    //endregion

    //region CampaignMasterRule
    single<Mapper<SyncCampaignMasterRuleEntity, SyncCampaignMasterRuleModel>>(named("SyncCampaignMasterRuleEntityDbMapper")) { SyncCampaignMasterRuleEntityDbMapper() }
    //endregion

    //region CampaignOrganization
    single<Mapper<SyncCampaignOrganizationEntity, SyncCampaignOrganizationModel>>(named("SyncCampaignOrganizationEntityDbMapper")) { SyncCampaignOrganizationEntityDbMapper() }
    //endregion

    //region CampaignResultRule
    single<Mapper<SyncCampaignResultRuleEntity, SyncCampaignResultRuleModel>>(named("SyncCampaignResultRuleEntityDbMapper")) { SyncCampaignResultRuleEntityDbMapper() }
    //endregion

    //region CollectionLogInformation
    single<Mapper<CollectionLogInformationEntity, CollectionLogInformationModel>>(named("CollectionLogInformationEntityDbMapper")) { CollectionLogInformationEntityDbMapper() }
    //endregion

    //region CrashLogInformation
    single<Mapper<CrashLogInformationEntity, CrashLogInformationModel>>(named("CrashLogInformationEntityDbMapper")) { CrashLogInformationEntityDbMapper() }
    //endregion

    //region CrmPriceListParameter
    single<MapperDto<SyncCrmPriceListParameterEntity, SyncCrmPriceListParameterModel, CrmPriceListParameterDto>>(named("SyncCrmPriceListParameterEntityDbMapper")) { SyncCrmPriceListParameterEntityDbMapper() }
    //endregion

    //region CustomDataDetail
    single<Mapper<CustomDataDetailEntity, CustomDataDetailModel>>(named("CustomDataDetailEntityDbMapper")) { CustomDataDetailEntityDbMapper() }
    //endregion

    //region CustomDataHeader
    single<Mapper<CustomDataHeaderEntity, CustomDataHeaderModel>>(named("CustomDataHeaderEntityDbMapper")) { CustomDataHeaderEntityDbMapper() }
    //endregion

    //region CustomerCampaignImplementationLogInformation
    single<Mapper<CustomerCampaignImplementationLogInformationEntity, CustomerCampaignImplementationLogInformationModel>>(named("CustomerCampaignImplementationLogInformationEntityDbMapper")) { CustomerCampaignImplementationLogInformationEntityDbMapper() }
    //endregion

    //region CustomerCategoryClassification
    single<Mapper<SyncCustomerCategoryClassificationEntity, SyncCustomerCategoryClassificationModel>>(named("SyncCustomerCategoryClassificationEntityDbMapper")) { SyncCustomerCategoryClassificationEntityDbMapper() }
    //endregion

    //region CustomerChannelClassification
    single<Mapper<SyncCustomerChannelClassificationEntity, SyncCustomerChannelClassificationModel>>(named("SyncCustomerChannelClassificationEntityDbMapper")) { SyncCustomerChannelClassificationEntityDbMapper() }
    //endregion

    //region CustomerClassClassification
    single<Mapper<SyncCustomerClassClassificationEntity, SyncCustomerClassClassificationModel>>(named("SyncCustomerClassClassificationEntityDbMapper")) { SyncCustomerClassClassificationEntityDbMapper() }
    //endregion

    //region CustomerCustomField
    single<Mapper<SyncCustomerCustomFieldEntity, SyncCustomerCustomFieldModel>>(named("SyncCustomerCustomFieldEntityDbMapper")) { SyncCustomerCustomFieldEntityDbMapper() }
    //endregion

    //region CustomerEmail
    single<MapperDto<SyncCustomerEmailEntity, SyncCustomerEmailModel, CustomerEmailDto>>(named("SyncCustomerEmailEntityDbMapper")) { SyncCustomerEmailEntityDbMapper() }
    //endregion

    //region CustomerGroup
    single<MapperDto<SyncCustomerGroupEntity, SyncCustomerGroupModel, CustomerGroupDto>>(named("SyncCustomerGroupEntityDbMapper")) { SyncCustomerGroupEntityDbMapper() }
    //endregion

    //region CustomerGroupProductDistribution
    single<Mapper<SyncCustomerGroupProductDistributionEntity, SyncCustomerGroupProductDistributionModel>>(named("SyncCustomerGroupProductDistributionEntityDbMapper")) { SyncCustomerGroupProductDistributionEntityDbMapper() }
    //endregion

    //region CustomerNoteSp
    single<Mapper<CustomerNoteSpEntity, CustomerNoteSpModel>>(named("CustomerNoteSpEntityDbMapper")) { CustomerNoteSpEntityDbMapper() }
    //endregion

    //region CustomerOrganizationScope
    single<Mapper<SyncCustomerOrganizationScopeEntity, SyncCustomerOrganizationScopeModel>>(named("SyncCustomerOrganizationScopeEntityDbMapper")) { SyncCustomerOrganizationScopeEntityDbMapper() }
    //endregion

    //region CustomerProductDistribution
    single<Mapper<SyncCustomerProductDistributionEntity, SyncCustomerProductDistributionModel>>(named("SyncCustomerProductDistributionEntityDbMapper")) { SyncCustomerProductDistributionEntityDbMapper() }
    //endregion

    //region CustomerSegmentClassification
    single<Mapper<SyncCustomerSegmentClassificationEntity, SyncCustomerSegmentClassificationModel>>(named("SyncCustomerSegmentClassificationEntityDbMapper")) { SyncCustomerSegmentClassificationEntityDbMapper() }
    //endregion

    //region DailyOperationLogInformation
    single<Mapper<DailyOperationLogInformationEntity, DailyOperationLogInformationModel>>(named("DailyOperationLogInformationEntityDbMapper")) { DailyOperationLogInformationEntityDbMapper() }
    //endregion

    //region DocumentMap
    single<Mapper<SyncDocumentMapEntity, SyncDocumentMapModel>>(named("SyncDocumentMapEntityDbMapper")) { SyncDocumentMapEntityDbMapper() }
    //endregion

    //region DocumentMapDocNumberInformation
    single<Mapper<DocumentMapDocNumberInformationEntity, DocumentMapDocNumberInformationModel>>(named("DocumentMapDocNumberInformationEntityDbMapper")) { DocumentMapDocNumberInformationEntityDbMapper() }
    //endregion

    //region DocumentMapProcess
    single<Mapper<SyncDocumentMapProcessEntity, SyncDocumentMapProcessModel>>(named("SyncDocumentMapProcessEntityDbMapper")) { SyncDocumentMapProcessEntityDbMapper() }
    //endregion

    //region DocumentMapProcessStep
    single<Mapper<SyncDocumentMapProcessStepEntity, SyncDocumentMapProcessStepModel>>(named("SyncDocumentMapProcessStepEntityDbMapper")) { SyncDocumentMapProcessStepEntityDbMapper() }
    //endregion

    //region DocumentOrganization
    single<Mapper<SyncDocumentOrganizationEntity, SyncDocumentOrganizationModel>>(named("SyncDocumentOrganizationEntityDbMapper")) { SyncDocumentOrganizationEntityDbMapper() }
    //endregion

    //region DownedDriveItem
    single<Mapper<DownedDriveItemEntity, DownedDriveItemModel>>(named("DownedDriveItemEntityDbMapper")) { DownedDriveItemEntityDbMapper() }
    //endregion

    //region DynamicDataRow
    single<Mapper<DynamicDataRowEntity, DynamicDataRowModel>>(named("DynamicDataRowEntityDbMapper")) { DynamicDataRowEntityDbMapper() }
    //endregion

    //region DynamicDataRowModelv2
    single<Mapper<DynamicDataRowModelv2Entity, DynamicDataRowModelv2Model>>(named("DynamicDataRowModelv2EntityDbMapper")) { DynamicDataRowModelv2EntityDbMapper() }
    //endregion

    //region DynamicListHeaders
    single<Mapper<DynamicListHeadersEntity, DynamicListHeadersModel>>(named("DynamicListHeadersEntityDbMapper")) { DynamicListHeadersEntityDbMapper() }
    //endregion

    //region DynamicListItems
    single<Mapper<DynamicListItemsEntity, DynamicListItemsModel>>(named("DynamicListItemsEntityDbMapper")) { DynamicListItemsEntityDbMapper() }
    //endregion

    //region DynamicListOrganization
    single<Mapper<DynamicListOrganizationEntity, DynamicListOrganizationModel>>(named("DynamicListOrganizationEntityDbMapper")) { DynamicListOrganizationEntityDbMapper() }
    //endregion

    //region DynamicPageReport
    single<Mapper<SyncDynamicPageReportEntity, SyncDynamicPageReportModel>>(named("SyncDynamicPageReportEntityDbMapper")) { SyncDynamicPageReportEntityDbMapper() }
    //endregion

    //region EagleEyeLogInformation
    single<Mapper<EagleEyeLogInformationEntity, EagleEyeLogInformationModel>>(named("EagleEyeLogInformationEntityDbMapper")) { EagleEyeLogInformationEntityDbMapper() }
    //endregion

    //region EventReason
    single<MapperDto<SyncEventReasonEntity, SyncEventReasonModel, EventReasonDto>>(named("SyncEventReasonEntityDbMapper")) { SyncEventReasonEntityDbMapper() }
    //endregion

    //region FeedbackCategory
    single<Mapper<SyncFeedbackCategoryEntity, SyncFeedbackCategoryModel>>(named("SyncFeedbackCategoryEntityDbMapper")) { SyncFeedbackCategoryEntityDbMapper() }
    //endregion

    //region FeedbackList
    single<Mapper<SyncFeedbackListEntity, SyncFeedbackListModel>>(named("SyncFeedbackListEntityDbMapper")) { SyncFeedbackListEntityDbMapper() }
    //endregion

    //region FormBase
    single<Mapper<SyncFormBaseEntity, SyncFormBaseModel>>(named("SyncFormBaseEntityDbMapper")) { SyncFormBaseEntityDbMapper() }
    //endregion

    //region FormHotSaleDailyOperationInformation
    single<Mapper<FormHotSaleDailyOperationInformationEntity, FormHotSaleDailyOperationInformationModel>>(named("FormHotSaleDailyOperationInformationEntityDbMapper")) { FormHotSaleDailyOperationInformationEntityDbMapper() }
    //endregion

    //region FormLogInformation
    single<Mapper<FormLogInformationEntity, FormLogInformationModel>>(named("FormLogInformationEntityDbMapper")) { FormLogInformationEntityDbMapper() }
    //endregion

    //region Game
    single<Mapper<SyncGameEntity, SyncGameModel>>(named("SyncGameEntityDbMapper")) { SyncGameEntityDbMapper() }
    //endregion

    //region GeoLocation
    single<Mapper<GeoLocationEntity, GeoLocationModel>>(named("GeoLocationEntityDbMapper")) { GeoLocationEntityDbMapper() }
    //endregion

    //region HotSaleDailyInformation
    single<Mapper<HotSaleDailyInformationEntity, HotSaleDailyInformationModel>>(named("HotSaleDailyInformationEntityDbMapper")) { HotSaleDailyInformationEntityDbMapper() }
    //endregion

    //region InventoryCountLogInformation
    single<Mapper<InventoryCountLogInformationEntity, InventoryCountLogInformationModel>>(named("InventoryCountLogInformationEntityDbMapper")) { InventoryCountLogInformationEntityDbMapper() }
    //endregion

    //region InvoicePrintContentLogInformation
    single<Mapper<InvoicePrintContentLogInformationEntity, InvoicePrintContentLogInformationModel>>(named("InvoicePrintContentLogInformationEntityDbMapper")) { InvoicePrintContentLogInformationEntityDbMapper() }
    //endregion

    //region ManufacturerCustomers
    single<Mapper<SyncManufacturerCustomersEntity, SyncManufacturerCustomersModel>>(named("SyncManufacturerCustomersEntityDbMapper")) { SyncManufacturerCustomersEntityDbMapper() }
    //endregion

    //region ManufacturerParameter
    single<Mapper<SyncManufacturerParameterEntity, SyncManufacturerParameterModel>>(named("SyncManufacturerParameterEntityDbMapper")) { SyncManufacturerParameterEntityDbMapper() }
    //endregion

    //region ManufacturerRepresentatives
    single<Mapper<SyncManufacturerRepresentativesEntity, SyncManufacturerRepresentativesModel>>(named("SyncManufacturerRepresentativesEntityDbMapper")) { SyncManufacturerRepresentativesEntityDbMapper() }
    //endregion

    //region Manufacturers
    single<Mapper<SyncManufacturersEntity, SyncManufacturersModel>>(named("SyncManufacturersEntityDbMapper")) { SyncManufacturersEntityDbMapper() }
    //endregion

    //region MessageUser
    single<Mapper<SyncMessageUserEntity, SyncMessageUserModel>>(named("SyncMessageUserEntityDbMapper")) { SyncMessageUserEntityDbMapper() }
    //endregion

    //region Module
    single<Mapper<SyncModuleEntity, SyncModuleModel>>(named("SyncModuleEntityDbMapper")) { SyncModuleEntityDbMapper() }
    //endregion

    //region NotificationLogInformation
    single<Mapper<NotificationLogInformationEntity, NotificationLogInformationModel>>(named("NotificationLogInformationEntityDbMapper")) { NotificationLogInformationEntityDbMapper() }
    //endregion

    //region OrderLogInformation
    single<Mapper<OrderLogInformationEntity, OrderLogInformationModel>>(named("OrderLogInformationEntityDbMapper")) { OrderLogInformationEntityDbMapper() }
    //endregion

    //region Organization
    single<Mapper<SyncOrganizationEntity, SyncOrganizationModel>>(named("SyncOrganizationEntityDbMapper")) { SyncOrganizationEntityDbMapper() }
    //endregion

    //region OrganizationInfo
    single<Mapper<SyncOrganizationInfoEntity, SyncOrganizationInfoModel>>(named("SyncOrganizationInfoEntityDbMapper")) { SyncOrganizationInfoEntityDbMapper() }
    //endregion

    //region PackageCustomField
    single<MapperDto<SyncPackageCustomFieldEntity, SyncPackageCustomFieldModel, PackageCustomFieldDto>>(named("SyncPackageCustomFieldEntityDbMapper")) { SyncPackageCustomFieldEntityDbMapper() }
    //endregion

    //region PackageCustomFieldProduct
    single<MapperDto<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel, PackageCustomFieldProductDto>>(named("SyncPackageCustomFieldProductEntityDbMapper")) { SyncPackageCustomFieldProductEntityDbMapper() }
    //endregion

    //region PaymentPlan
    single<Mapper<SyncPaymentPlanEntity, SyncPaymentPlanModel>>(named("SyncPaymentPlanEntityDbMapper")) { SyncPaymentPlanEntityDbMapper() }
    //endregion

    //region PaymentTransactionLogInformation
    single<Mapper<PaymentTransactionLogInformationEntity, PaymentTransactionLogInformationModel>>(named("PaymentTransactionLogInformationEntityDbMapper")) { PaymentTransactionLogInformationEntityDbMapper() }
    //endregion

    //region PendingReport
    single<Mapper<PendingReportEntity, PendingReportModel>>(named("PendingReportEntityDbMapper")) { PendingReportEntityDbMapper() }
    //endregion

    //region PrinterDocumentRelationInformation
    single<Mapper<PrinterDocumentRelationInformationEntity, PrinterDocumentRelationInformationModel>>(named("PrinterDocumentRelationInformationEntityDbMapper")) { PrinterDocumentRelationInformationEntityDbMapper() }
    //endregion

    //region ProductAllocation
    single<Mapper<SyncProductAllocationEntity, SyncProductAllocationModel>>(named("SyncProductAllocationEntityDbMapper")) { SyncProductAllocationEntityDbMapper() }
    //endregion

    //region ProductCustomField
    single<Mapper<SyncProductCustomFieldEntity, SyncProductCustomFieldModel>>(named("SyncProductCustomFieldEntityDbMapper")) { SyncProductCustomFieldEntityDbMapper() }
    //endregion

    //region ProductDistribution
    single<Mapper<SyncProductDistributionEntity, SyncProductDistributionModel>>(named("SyncProductDistributionEntityDbMapper")) { SyncProductDistributionEntityDbMapper() }
    //endregion

    //region ProductDistributionLine
    single<Mapper<SyncProductDistributionLineEntity, SyncProductDistributionLineModel>>(named("SyncProductDistributionLineEntityDbMapper")) { SyncProductDistributionLineEntityDbMapper() }
    //endregion

    //region ProductGroup
    single<Mapper<SyncProductGroupEntity, SyncProductGroupModel>>(named("SyncProductGroupEntityDbMapper")) { SyncProductGroupEntityDbMapper() }
    //endregion

    //region ProductImages
    single<Mapper<SyncProductImagesEntity, SyncProductImagesModel>>(named("SyncProductImagesEntityDbMapper")) { SyncProductImagesEntityDbMapper() }
    //endregion

    //region ProductParameter
    single<Mapper<ProductParameterEntity, ProductParameterModel>>(named("ProductParameterEntityDbMapper")) { ProductParameterEntityDbMapper() }
    //endregion

    //region ProductParameterv4
    single<Mapper<ProductParameterv4Entity, ProductParameterv4Model>>(named("ProductParameterv4EntityDbMapper")) { ProductParameterv4EntityDbMapper() }
    //endregion

    //region ProductPriceLines
    single<Mapper<SyncProductPriceLinesEntity, SyncProductPriceLinesModel>>(named("SyncProductPriceLinesEntityDbMapper")) { SyncProductPriceLinesEntityDbMapper() }
    //endregion

    //region ProductPrices
    single<Mapper<SyncProductPricesEntity, SyncProductPricesModel>>(named("SyncProductPricesEntityDbMapper")) { SyncProductPricesEntityDbMapper() }
    //endregion

    //region ProductUnit
    single<Mapper<SyncProductUnitEntity, SyncProductUnitModel>>(named("SyncProductUnitEntityDbMapper")) { SyncProductUnitEntityDbMapper() }
    //endregion

    //region ProductUnitParameter
    single<Mapper<SyncProductUnitParameterEntity, SyncProductUnitParameterModel>>(named("SyncProductUnitParameterEntityDbMapper")) { SyncProductUnitParameterEntityDbMapper() }
    //endregion

    //region RepresentativeAllocation
    single<Mapper<SyncRepresentativeAllocationEntity, SyncRepresentativeAllocationModel>>(named("SyncRepresentativeAllocationEntityDbMapper")) { SyncRepresentativeAllocationEntityDbMapper() }
    //endregion

    //region RepresentativeCustomField
    single<Mapper<SyncRepresentativeCustomFieldEntity, SyncRepresentativeCustomFieldModel>>(named("SyncRepresentativeCustomFieldEntityDbMapper")) { SyncRepresentativeCustomFieldEntityDbMapper() }
    //endregion

    //region RepresentativeProductDistribution
    single<Mapper<SyncRepresentativeProductDistributionEntity, SyncRepresentativeProductDistributionModel>>(named("SyncRepresentativeProductDistributionEntityDbMapper")) { SyncRepresentativeProductDistributionEntityDbMapper() }
    //endregion

    //region ReservedStock
    single<Mapper<SyncReservedStockEntity, SyncReservedStockModel>>(named("SyncReservedStockEntityDbMapper")) { SyncReservedStockEntityDbMapper() }
    //endregion

    //region RestServiceTask
    single<Mapper<RestServiceTaskEntity, RestServiceTaskModel>>(named("RestServiceTaskEntityDbMapper")) { RestServiceTaskEntityDbMapper() }
    //endregion

    //region RiskDueDay
    single<Mapper<SyncRiskDueDayEntity, SyncRiskDueDayModel>>(named("SyncRiskDueDayEntityDbMapper")) { SyncRiskDueDayEntityDbMapper() }
    //endregion

    //region SMSValidationLogInformation
    single<Mapper<SMSValidationLogInformationEntity, SMSValidationLogInformationModel>>(named("SMSValidationLogInformationEntityDbMapper")) { SMSValidationLogInformationEntityDbMapper() }
    //endregion

    //region ShipmentActionLogInformation
    single<Mapper<ShipmentActionLogInformationEntity, ShipmentActionLogInformationModel>>(named("ShipmentActionLogInformationEntityDbMapper")) { ShipmentActionLogInformationEntityDbMapper() }
    //endregion

    //region StepAttribute
    single<Mapper<SyncStepAttributeEntity, SyncStepAttributeModel>>(named("SyncStepAttributeEntityDbMapper")) { SyncStepAttributeEntityDbMapper() }
    //endregion

    //region Stock
    single<Mapper<SyncStockEntity, SyncStockModel>>(named("SyncStockEntityDbMapper")) { SyncStockEntityDbMapper() }
    //endregion

    //region StockApprovalLogInformation
    single<Mapper<SyncStockApprovalLogInformationEntity, SyncStockApprovalLogInformationModel>>(named("SyncStockApprovalLogInformationEntityDbMapper")) { SyncStockApprovalLogInformationEntityDbMapper() }
    //endregion

    //region StockLoadingDemandLogInformation
    single<Mapper<StockLoadingDemandLogInformationEntity, StockLoadingDemandLogInformationModel>>(named("StockLoadingDemandLogInformationEntityDbMapper")) { StockLoadingDemandLogInformationEntityDbMapper() }
    //endregion

    //region StockTransactionLogInformation
    single<Mapper<StockTransactionLogInformationEntity, StockTransactionLogInformationModel>>(named("StockTransactionLogInformationEntityDbMapper")) { StockTransactionLogInformationEntityDbMapper() }
    //endregion

    //region SystemLogInformation
    single<Mapper<SystemLogInformationEntity, SystemLogInformationModel>>(named("SystemLogInformationEntityDbMapper")) { SystemLogInformationEntityDbMapper() }
    //endregion

    //region Task
    single<Mapper<SyncTaskEntity, SyncTaskModel>>(named("SyncTaskEntityDbMapper")) { SyncTaskEntityDbMapper() }
    //endregion

    //region TaskLogInformation
    single<Mapper<TaskLogInformationEntity, TaskLogInformationModel>>(named("TaskLogInformationEntityDbMapper")) { TaskLogInformationEntityDbMapper() }
    //endregion

    //region TaskModelAddress
    single<Mapper<SyncTaskModelAddressEntity, SyncTaskModelAddressModel>>(named("SyncTaskModelAddressEntityDbMapper")) { SyncTaskModelAddressEntityDbMapper() }
    //endregion

    //region TaskStep
    single<Mapper<SyncTaskStepEntity, SyncTaskStepModel>>(named("SyncTaskStepEntityDbMapper")) { SyncTaskStepEntityDbMapper() }
    //endregion

    //region TaskVisitLogInformation
    single<Mapper<TaskVisitLogInformationEntity, TaskVisitLogInformationModel>>(named("TaskVisitLogInformationEntityDbMapper")) { TaskVisitLogInformationEntityDbMapper() }
    //endregion

    //region TenantDiscount
    single<Mapper<SyncTenantDiscountEntity, SyncTenantDiscountModel>>(named("SyncTenantDiscountEntityDbMapper")) { SyncTenantDiscountEntityDbMapper() }
    //endregion

    //region TransitStock
    single<Mapper<SyncTransitStockEntity, SyncTransitStockModel>>(named("SyncTransitStockEntityDbMapper")) { SyncTransitStockEntityDbMapper() }
    //endregion

    //region Unit
    single<Mapper<SyncUnitEntity, SyncUnitModel>>(named("SyncUnitEntityDbMapper")) { SyncUnitEntityDbMapper() }
    //endregion

    //region UploadFileTask
    single<Mapper<UploadFileTaskEntity, UploadFileTaskModel>>(named("UploadFileTaskEntityDbMapper")) { UploadFileTaskEntityDbMapper() }
    //endregion

    //region Visit
    single<Mapper<VisitEntity, VisitModel>>(named("VisitEntityDbMapper")) { VisitEntityDbMapper() }
    //endregion

    //region VisitActivityDefinition
    single<Mapper<SyncVisitActivityDefinitionEntity, SyncVisitActivityDefinitionModel>>(named("SyncVisitActivityDefinitionEntityDbMapper")) { SyncVisitActivityDefinitionEntityDbMapper() }
    //endregion

    //region VisitActivityLogInformation
    single<Mapper<VisitActivityLogInformationEntity, VisitActivityLogInformationModel>>(named("VisitActivityLogInformationEntityDbMapper")) { VisitActivityLogInformationEntityDbMapper() }
    //endregion

    //region VisitLogInformation
    single<Mapper<VisitLogInformationEntity, VisitLogInformationModel>>(named("VisitLogInformationEntityDbMapper")) { VisitLogInformationEntityDbMapper() }
    //endregion

    //region Warehouse
    single<Mapper<SyncWarehouseEntity, SyncWarehouseModel>>(named("SyncWarehouseEntityDbMapper")) { SyncWarehouseEntityDbMapper() }
    //endregion

    //region WarehouseTotal
    single<Mapper<SyncWarehouseTotalEntity, SyncWarehouseTotalModel>>(named("SyncWarehouseTotalEntityDbMapper")) { SyncWarehouseTotalEntityDbMapper() }
    //endregion

}