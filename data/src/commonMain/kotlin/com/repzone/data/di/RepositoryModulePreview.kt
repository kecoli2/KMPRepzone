package com.repzone.data.di

import com.repzone.data.mapper.AppSettingEntityDbMapper
import com.repzone.data.mapper.CampaignMasterResultRequiredProductEntityDbMapper
import com.repzone.data.mapper.CollectionLogInformationEntityDbMapper
import com.repzone.data.mapper.CrashLogInformationEntityDbMapper
import com.repzone.data.mapper.CustomDataDetailEntityDbMapper
import com.repzone.data.mapper.CustomDataHeaderEntityDbMapper
import com.repzone.data.mapper.CustomerCampaignImplementationLogInformationEntityDbMapper
import com.repzone.data.mapper.CustomerEntityDbMapper
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
import com.repzone.data.mapper.ProductParameterEntityDbMapper
import com.repzone.data.mapper.ProductParameterv4EntityDbMapper
import com.repzone.data.mapper.RestServiceTaskEntityDbMapper
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
import com.repzone.data.repository.imp.MobileModuleParameterRepositoryImpl
import com.repzone.data.repository.imp.MobileModuleParameterRepositoryImplPreview
import com.repzone.data.repository.imp.ProductRepositoryImpl
import com.repzone.data.repository.imp.SyncModuleRepositoryImpl
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.AppSettingEntity
import com.repzone.database.CampaignMasterResultRequiredProductEntity
import com.repzone.database.CollectionLogInformationEntity
import com.repzone.database.CrashLogInformationEntity
import com.repzone.database.CustomDataDetailEntity
import com.repzone.database.CustomDataHeaderEntity
import com.repzone.database.CustomerCampaignImplementationLogInformationEntity
import com.repzone.database.CustomerNoteSpEntity
import com.repzone.database.DailyOperationLogInformationEntity
import com.repzone.database.DocumentMapDocNumberInformationEntity
import com.repzone.database.DownedDriveItemEntity
import com.repzone.database.DynamicDataRowEntity
import com.repzone.database.DynamicDataRowModelv2Entity
import com.repzone.database.DynamicListHeadersEntity
import com.repzone.database.DynamicListItemsEntity
import com.repzone.database.DynamicListOrganizationEntity
import com.repzone.database.EagleEyeLogInformationEntity
import com.repzone.database.FormHotSaleDailyOperationInformationEntity
import com.repzone.database.FormLogInformationEntity
import com.repzone.database.GeoLocationEntity
import com.repzone.database.HotSaleDailyInformationEntity
import com.repzone.database.InventoryCountLogInformationEntity
import com.repzone.database.InvoicePrintContentLogInformationEntity
import com.repzone.database.NotificationLogInformationEntity
import com.repzone.database.OrderLogInformationEntity
import com.repzone.database.PaymentTransactionLogInformationEntity
import com.repzone.database.PendingReportEntity
import com.repzone.database.PrinterDocumentRelationInformationEntity
import com.repzone.database.ProductParameterEntity
import com.repzone.database.ProductParameterv4Entity
import com.repzone.database.RestServiceTaskEntity
import com.repzone.database.SMSValidationLogInformationEntity
import com.repzone.database.ShipmentActionLogInformationEntity
import com.repzone.database.StockLoadingDemandLogInformationEntity
import com.repzone.database.StockTransactionLogInformationEntity
import com.repzone.database.SyncAppListEntity
import com.repzone.database.SyncCampaignCustomerImplementationsEntity
import com.repzone.database.SyncCampaignDynamicListOrganizationsEntity
import com.repzone.database.SyncCampaignMasterEntity
import com.repzone.database.SyncCampaignMasterResultEntity
import com.repzone.database.SyncCampaignMasterRuleEntity
import com.repzone.database.SyncCampaignOrganizationEntity
import com.repzone.database.SyncCampaignResultRuleEntity
import com.repzone.database.SyncCrmPriceListParameterEntity
import com.repzone.database.SyncCustomerCategoryClassificationEntity
import com.repzone.database.SyncCustomerChannelClassificationEntity
import com.repzone.database.SyncCustomerClassClassificationEntity
import com.repzone.database.SyncCustomerCustomFieldEntity
import com.repzone.database.SyncCustomerEmailEntity
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.SyncCustomerGroupEntity
import com.repzone.database.SyncCustomerGroupProductDistributionEntity
import com.repzone.database.SyncCustomerOrganizationScopeEntity
import com.repzone.database.SyncCustomerProductDistributionEntity
import com.repzone.database.SyncCustomerSegmentClassificationEntity
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.database.SyncDocumentMapProcessEntity
import com.repzone.database.SyncDocumentMapProcessStepEntity
import com.repzone.database.SyncDocumentOrganizationEntity
import com.repzone.database.SyncDynamicPageReportEntity
import com.repzone.database.SyncEventReasonEntity
import com.repzone.database.SyncFeedbackCategoryEntity
import com.repzone.database.SyncFeedbackListEntity
import com.repzone.database.SyncFormBaseEntity
import com.repzone.database.SyncGameEntity
import com.repzone.database.SyncManufacturerCustomersEntity
import com.repzone.database.SyncManufacturerParameterEntity
import com.repzone.database.SyncManufacturerRepresentativesEntity
import com.repzone.database.SyncManufacturersEntity
import com.repzone.database.SyncMessageUserEntity
import com.repzone.database.SyncModuleEntity
import com.repzone.database.SyncOrganizationEntity
import com.repzone.database.SyncOrganizationInfoEntity
import com.repzone.database.SyncPackageCustomFieldEntity
import com.repzone.database.SyncPackageCustomFieldProductEntity
import com.repzone.database.SyncPaymentPlanEntity
import com.repzone.database.SyncProductAllocationEntity
import com.repzone.database.SyncProductCustomFieldEntity
import com.repzone.database.SyncProductDistributionEntity
import com.repzone.database.SyncProductDistributionLineEntity
import com.repzone.database.SyncProductEntity
import com.repzone.database.SyncProductGroupEntity
import com.repzone.database.SyncProductImagesEntity
import com.repzone.database.SyncProductPriceLinesEntity
import com.repzone.database.SyncProductPricesEntity
import com.repzone.database.SyncProductUnitEntity
import com.repzone.database.SyncProductUnitParameterEntity
import com.repzone.database.SyncRepresentativeAllocationEntity
import com.repzone.database.SyncRepresentativeCustomFieldEntity
import com.repzone.database.SyncRepresentativeProductDistributionEntity
import com.repzone.database.SyncReservedStockEntity
import com.repzone.database.SyncRiskDueDayEntity
import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.database.SyncStepAttributeEntity
import com.repzone.database.SyncStockApprovalLogInformationEntity
import com.repzone.database.SyncStockEntity
import com.repzone.database.SyncTaskEntity
import com.repzone.database.SyncTaskModelAddressEntity
import com.repzone.database.SyncTaskStepEntity
import com.repzone.database.SyncTenantDiscountEntity
import com.repzone.database.SyncTransitStockEntity
import com.repzone.database.SyncUnitEntity
import com.repzone.database.SyncVisitActivityDefinitionEntity
import com.repzone.database.SyncWarehouseEntity
import com.repzone.database.SyncWarehouseTotalEntity
import com.repzone.database.SystemLogInformationEntity
import com.repzone.database.TaskLogInformationEntity
import com.repzone.database.TaskVisitLogInformationEntity
import com.repzone.database.UploadFileTaskEntity
import com.repzone.database.VisitActivityLogInformationEntity
import com.repzone.database.VisitEntity
import com.repzone.database.VisitLogInformationEntity
import com.repzone.domain.model.AppSettingModel
import com.repzone.domain.model.CampaignMasterResultRequiredProductModel
import com.repzone.domain.model.CollectionLogInformationModel
import com.repzone.domain.model.CrashLogInformationModel
import com.repzone.domain.model.CustomDataDetailModel
import com.repzone.domain.model.CustomDataHeaderModel
import com.repzone.domain.model.CustomerCampaignImplementationLogInformationModel
import com.repzone.domain.model.CustomerNoteSpModel
import com.repzone.domain.model.DailyOperationLogInformationModel
import com.repzone.domain.model.DocumentMapDocNumberInformationModel
import com.repzone.domain.model.DownedDriveItemModel
import com.repzone.domain.model.DynamicDataRowModel
import com.repzone.domain.model.DynamicDataRowModelv2Model
import com.repzone.domain.model.DynamicListHeadersModel
import com.repzone.domain.model.DynamicListItemsModel
import com.repzone.domain.model.DynamicListOrganizationModel
import com.repzone.domain.model.EagleEyeLogInformationModel
import com.repzone.domain.model.FormHotSaleDailyOperationInformationModel
import com.repzone.domain.model.FormLogInformationModel
import com.repzone.domain.model.GeoLocationModel
import com.repzone.domain.model.HotSaleDailyInformationModel
import com.repzone.domain.model.InventoryCountLogInformationModel
import com.repzone.domain.model.InvoicePrintContentLogInformationModel
import com.repzone.domain.model.NotificationLogInformationModel
import com.repzone.domain.model.OrderLogInformationModel
import com.repzone.domain.model.PaymentTransactionLogInformationModel
import com.repzone.domain.model.PendingReportModel
import com.repzone.domain.model.PrinterDocumentRelationInformationModel
import com.repzone.domain.model.ProductParameterModel
import com.repzone.domain.model.ProductParameterv4Model
import com.repzone.domain.model.RestServiceTaskModel
import com.repzone.domain.model.SMSValidationLogInformationModel
import com.repzone.domain.model.ShipmentActionLogInformationModel
import com.repzone.domain.model.StockLoadingDemandLogInformationModel
import com.repzone.domain.model.StockTransactionLogInformationModel
import com.repzone.domain.model.SyncAppListModel
import com.repzone.domain.model.SyncCampaignCustomerImplementationsModel
import com.repzone.domain.model.SyncCampaignDynamicListOrganizationsModel
import com.repzone.domain.model.SyncCampaignMasterModel
import com.repzone.domain.model.SyncCampaignMasterResultModel
import com.repzone.domain.model.SyncCampaignMasterRuleModel
import com.repzone.domain.model.SyncCampaignOrganizationModel
import com.repzone.domain.model.SyncCampaignResultRuleModel
import com.repzone.domain.model.SyncCrmPriceListParameterModel
import com.repzone.domain.model.SyncCustomerCategoryClassificationModel
import com.repzone.domain.model.SyncCustomerChannelClassificationModel
import com.repzone.domain.model.SyncCustomerClassClassificationModel
import com.repzone.domain.model.SyncCustomerCustomFieldModel
import com.repzone.domain.model.SyncCustomerEmailModel
import com.repzone.domain.model.SyncCustomerGroupModel
import com.repzone.domain.model.SyncCustomerGroupProductDistributionModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.model.SyncCustomerOrganizationScopeModel
import com.repzone.domain.model.SyncCustomerProductDistributionModel
import com.repzone.domain.model.SyncCustomerSegmentClassificationModel
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.domain.model.SyncDocumentMapProcessModel
import com.repzone.domain.model.SyncDocumentMapProcessStepModel
import com.repzone.domain.model.SyncDocumentOrganizationModel
import com.repzone.domain.model.SyncDynamicPageReportModel
import com.repzone.domain.model.SyncEventReasonModel
import com.repzone.domain.model.SyncFeedbackCategoryModel
import com.repzone.domain.model.SyncFeedbackListModel
import com.repzone.domain.model.SyncFormBaseModel
import com.repzone.domain.model.SyncGameModel
import com.repzone.domain.model.SyncManufacturerCustomersModel
import com.repzone.domain.model.SyncManufacturerParameterModel
import com.repzone.domain.model.SyncManufacturerRepresentativesModel
import com.repzone.domain.model.SyncManufacturersModel
import com.repzone.domain.model.SyncMessageUserModel
import com.repzone.domain.model.SyncModuleModel
import com.repzone.domain.model.SyncOrganizationInfoModel
import com.repzone.domain.model.SyncOrganizationModel
import com.repzone.domain.model.SyncPackageCustomFieldModel
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import com.repzone.domain.model.SyncPaymentPlanModel
import com.repzone.domain.model.SyncProductAllocationModel
import com.repzone.domain.model.SyncProductCustomFieldModel
import com.repzone.domain.model.SyncProductDistributionLineModel
import com.repzone.domain.model.SyncProductDistributionModel
import com.repzone.domain.model.SyncProductGroupModel
import com.repzone.domain.model.SyncProductImagesModel
import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.model.SyncProductPriceLinesModel
import com.repzone.domain.model.SyncProductPricesModel
import com.repzone.domain.model.SyncProductUnitModel
import com.repzone.domain.model.SyncProductUnitParameterModel
import com.repzone.domain.model.SyncRepresentativeAllocationModel
import com.repzone.domain.model.SyncRepresentativeCustomFieldModel
import com.repzone.domain.model.SyncRepresentativeProductDistributionModel
import com.repzone.domain.model.SyncReservedStockModel
import com.repzone.domain.model.SyncRiskDueDayModel
import com.repzone.domain.model.SyncRouteAppointmentModel
import com.repzone.domain.model.SyncStepAttributeModel
import com.repzone.domain.model.SyncStockApprovalLogInformationModel
import com.repzone.domain.model.SyncStockModel
import com.repzone.domain.model.SyncTaskModel
import com.repzone.domain.model.SyncTaskModelAddressModel
import com.repzone.domain.model.SyncTaskStepModel
import com.repzone.domain.model.SyncTenantDiscountModel
import com.repzone.domain.model.SyncTransitStockModel
import com.repzone.domain.model.SyncUnitModel
import com.repzone.domain.model.SyncVisitActivityDefinitionModel
import com.repzone.domain.model.SyncWarehouseModel
import com.repzone.domain.model.SyncWarehouseTotalModel
import com.repzone.domain.model.SystemLogInformationModel
import com.repzone.domain.model.TaskLogInformationModel
import com.repzone.domain.model.TaskVisitLogInformationModel
import com.repzone.domain.model.UploadFileTaskModel
import com.repzone.domain.model.VisitActivityLogInformationModel
import com.repzone.domain.model.VisitLogInformationModel
import com.repzone.domain.model.VisitModel
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.ICustomerRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IProductRepository
import com.repzone.domain.repository.ISyncModuleRepository
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.network.dto.CustomerDto
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.network.dto.DocumentMapProcessDto
import com.repzone.network.dto.DocumentMapProcessStepDto
import com.repzone.network.dto.DynamicPageReportDto
import com.repzone.network.dto.EventReasonDto
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.dto.PackageCustomFieldProductDto
import com.repzone.network.dto.RouteDto
import org.koin.core.qualifier.named
import org.koin.dsl.module

val RepositoryModulePreview = module {

    //region REPOSITORY
    single<ICustomerRepository> { CustomerRepositoryImpl(get(named("CustomerEntityDbMapperInterface")), get()) }
    single<ICustomerListRepository> { CustomerListRepositoryImpl(get(), get(), get(), get(), get(named("CustomerEntityDbMapperInterface"))) }
    single<IProductRepository> { ProductRepositoryImpl(get(named("ProductEntityDbMapperInterface")), get()) }
    single<ISyncModuleRepository> { SyncModuleRepositoryImpl(get(named("SyncModuleEntityDbMapper")), get()) }
    single<IMobileModuleParameterRepository>{ MobileModuleParameterRepositoryImplPreview(get(), get(named("SyncPackageCustomFieldProductEntityDbMapper")), get()) }
    //endregion REPOSITORY

    //region DBMAPPERS
    //region Customer
    single<MapperDto<SyncCustomerEntity, SyncCustomerModel, CustomerDto>>(named("CustomerEntityDbMapperInterface")) { CustomerEntityDbMapper() }
    single{ CustomerEntityDbMapper() }
    //endregion

    //region Product
    single<Mapper<SyncProductEntity, SyncProductModel>>(named("ProductEntityDbMapperInterface")) { ProductEntityDbMapper() }
    //endregion

    //region SyncModule
    single<Mapper<SyncModuleEntity, SyncModuleModel>>(named("SyncModuleEntityDbMapper")) { SyncModuleEntityDbMapper() }
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
    single<MapperDto<SyncDocumentMapEntity, SyncDocumentMapModel, DocumentMapModelDto>>(named("SyncDocumentMapEntityDbMapper")) { SyncDocumentMapEntityDbMapper() }
    //endregion

    //region DocumentMapDocNumberInformation
    single<Mapper<DocumentMapDocNumberInformationEntity, DocumentMapDocNumberInformationModel>>(named("DocumentMapDocNumberInformationEntityDbMapper")) { DocumentMapDocNumberInformationEntityDbMapper() }
    //endregion

    //region DocumentMapProcess
    single<MapperDto<SyncDocumentMapProcessEntity, SyncDocumentMapProcessModel, DocumentMapProcessDto>>(named("SyncDocumentMapProcessEntityDbMapper")) { SyncDocumentMapProcessEntityDbMapper() }
    single{ SyncDocumentMapProcessEntityDbMapper() }
    //endregion

    //region DocumentMapProcessStep
    single<MapperDto<SyncDocumentMapProcessStepEntity, SyncDocumentMapProcessStepModel, DocumentMapProcessStepDto>>(named("SyncDocumentMapProcessStepEntityDbMapper")) { SyncDocumentMapProcessStepEntityDbMapper() }
    single { SyncDocumentMapProcessStepEntityDbMapper() }
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
    single<MapperDto<SyncDynamicPageReportEntity, SyncDynamicPageReportModel, DynamicPageReportDto>>(named("SyncDynamicPageReportEntityDbMapper")) { SyncDynamicPageReportEntityDbMapper() }
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
    //endregion DBMAPPERS
}