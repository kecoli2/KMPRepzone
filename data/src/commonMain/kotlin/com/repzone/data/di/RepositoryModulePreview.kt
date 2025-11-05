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
import com.repzone.data.repository.imp.VisitRepositoryImpl
import com.repzone.data.repository.imp.CustomerListRepositoryImpl
import com.repzone.data.repository.imp.CustomerRepositoryImpl
import com.repzone.data.repository.imp.EventReasonRepositoryImpl
import com.repzone.data.repository.imp.MobileModuleParameterRepositoryImplPreview
import com.repzone.data.repository.imp.RepresentativeRepositoryImpl
import com.repzone.data.repository.imp.RouteAppointmentRepositoryImpl
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
import com.repzone.database.CustomerItemViewEntity
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
import com.repzone.domain.model.CustomerItemModel
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
import com.repzone.domain.repository.IVisitRepository
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.ICustomerRepository
import com.repzone.domain.repository.IEventReasonRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IRepresentativeRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
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
    single<ICustomerRepository> { CustomerRepositoryImpl(get(), get()) }
    single<ICustomerListRepository> { CustomerListRepositoryImpl(get(),
        get(), get(), get(), get()) }
    single<ISyncModuleRepository> { SyncModuleRepositoryImpl(get(), get()) }
    single<IMobileModuleParameterRepository>{ MobileModuleParameterRepositoryImplPreview(get(), get(), get()) }
    factory<IRepresentativeRepository>{ RepresentativeRepositoryImpl(get(), get()) }
    single<IRouteAppointmentRepository> { RouteAppointmentRepositoryImpl(get(), get()) }
    single<IEventReasonRepository> { EventReasonRepositoryImpl(get()) }
    factory<IVisitRepository>{ VisitRepositoryImpl(get(), get()) }
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