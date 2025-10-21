package com.repzone.data.repository.imp

import com.repzone.core.enums.AutoFilterByStock
import com.repzone.core.enums.CustomerGPSUpdateType
import com.repzone.core.enums.LeaveDurationLimitType
import com.repzone.core.enums.ModuleProductIdsEnum
import com.repzone.core.enums.OnOf
import com.repzone.core.enums.OrganizationType
import com.repzone.core.enums.PreviousDayVisitsType
import com.repzone.core.enums.ShowDocumentLabelsOrderType
import com.repzone.core.enums.ShowDocumentLabelsType
import com.repzone.core.enums.ShowNearbyCustomersType
import com.repzone.core.enums.ShowPaytermSelectionType
import com.repzone.core.enums.StockEntryForm
import com.repzone.core.enums.StoreDurationType
import com.repzone.core.enums.UserSelectionType
import com.repzone.core.enums.ViolationActionType
import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.model.module.parameters.AttendanceTrackingParameters
import com.repzone.core.model.module.parameters.BusinessIntelligenceParameters
import com.repzone.core.model.module.parameters.CrmOperationsParameters
import com.repzone.core.model.module.parameters.CustomMobileFormsParameters
import com.repzone.core.model.module.parameters.DigitalContentSharingParameters
import com.repzone.core.model.module.parameters.DigitalProductCatalogParameters
import com.repzone.core.model.module.parameters.DispatchesParameters
import com.repzone.core.model.module.parameters.EagleEyeLocationTrackingParameters
import com.repzone.core.model.module.parameters.GamificationParameters
import com.repzone.core.model.module.parameters.GeofenceRouteTrackingParameters
import com.repzone.core.model.module.parameters.InvoiceEInvoiceParameters
import com.repzone.core.model.module.parameters.MessagingChatFeedbackParameters
import com.repzone.core.model.module.parameters.NotificationParameters
import com.repzone.core.model.module.parameters.OrderParameters
import com.repzone.core.model.module.parameters.PaymentCollectionsParameters
import com.repzone.core.model.module.parameters.ProductAllocationParameters
import com.repzone.core.model.module.parameters.QuickAccessParameters
import com.repzone.core.model.module.parameters.ReportsParameters
import com.repzone.core.model.module.parameters.TaskManagmentParameters
import com.repzone.core.model.module.parameters.VisitParameters
import com.repzone.core.model.module.parameters.WorkingHoursParameters
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.MapperDto
import com.repzone.data.util.getBooleanValue
import com.repzone.data.util.getDateTimeValue
import com.repzone.data.util.getEnumValue
import com.repzone.data.util.getIntValue
import com.repzone.data.util.getStringValue
import com.repzone.database.SyncPackageCustomFieldProductEntity
import com.repzone.database.SyncPackageCustomFieldProductEntityQueries
import com.repzone.domain.model.MobileParameterModel
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.network.dto.PackageCustomFieldProductDto
import kotlin.time.ExperimentalTime

class MobileModuleParameterRepositoryImplPreview(
        private val syncPackageCustomFieldProductQueries: SyncPackageCustomFieldProductEntityQueries,
        private val mapperCustomFieldProduct: MapperDto<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel, PackageCustomFieldProductDto>,
        private val iUserSession: IUserSession ): IMobileModuleParameterRepository {

    //region Fields
      private var parametersMap : MutableMap<ModuleProductIdsEnum, Any> = mutableMapOf()
      private var modules = MobileParameterModel()
    //endregion Fields

    //region Constructor
    init {
        prepareLoadAllParameters()
    }
    //endregion Constructor

    //region Public Method
    override fun getModule(): MobileParameterModel {
        return modules
    }

    override fun getOrdersParameters(): OrderParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.ORDERS] as OrderParameters?
    }

    override fun getInvoiceEInvoceParameters(): InvoiceEInvoiceParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.INVOICESANDEINVOICES] as InvoiceEInvoiceParameters?
    }

    override fun getBusinessIntelligenceParameters(): BusinessIntelligenceParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.BUSINESSINTELLIGENCE] as BusinessIntelligenceParameters?
    }

    override fun getDigitalContentSharingParameters(): DigitalContentSharingParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.DIGITALCONTENTSHARING] as DigitalContentSharingParameters?
    }

    override fun getTaskManagmentParameters(): TaskManagmentParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.TASKMANAGEMENT] as TaskManagmentParameters?
    }

    override fun getEagleEyeLocationTrackingParameters(): EagleEyeLocationTrackingParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.EAGLEEYEANDLOCATIONTRACKING] as EagleEyeLocationTrackingParameters?
    }

    override fun getMessagingChatFeedbackParameters(): MessagingChatFeedbackParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.MESSAGINGANDCHAT] as MessagingChatFeedbackParameters?
    }

    override fun getGeofenceRouteTrackingParameters(): GeofenceRouteTrackingParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.GEOFENCINGANDROUTETRACKING] as GeofenceRouteTrackingParameters?
    }

    override fun getCustomMobileFormsParameters(): CustomMobileFormsParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.CUSTOMMOBILEFORMS] as CustomMobileFormsParameters?
    }

    override fun getAttendanceTrackingParameters(): AttendanceTrackingParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.ATTENDANCETRACKING] as AttendanceTrackingParameters?
    }

    override fun getReportsParameters(): ReportsParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.REPORTS] as ReportsParameters?
    }

    override fun getPaymentCollectionsParameters(): PaymentCollectionsParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.PAYMENTCOLLECTION] as PaymentCollectionsParameters?
    }

    override fun getDispatchesParameters(): DispatchesParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.DISPATCHES] as DispatchesParameters?
    }

    override fun getCrmOperationsParameters(): CrmOperationsParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.CRMOPERATIONS] as CrmOperationsParameters?
    }

    override fun getDigitalProductCatalogParameters(): DigitalProductCatalogParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.DIGITALPRODUCTCATALOG] as DigitalProductCatalogParameters?
    }

    override fun getGamificationParameters(): GamificationParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.GAMIFICATION] as GamificationParameters?
    }

    override fun getQuickAccessParameters(): QuickAccessParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.QUICKACCESS] as QuickAccessParameters?
    }

    override fun getProductAllocationParameters(): ProductAllocationParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.PRODUCTALLOCATION] as ProductAllocationParameters?
    }

    override fun getVisitParameters(): VisitParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.VISIT] as VisitParameters?
    }

    override fun getNotificationParameters(): NotificationParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.NOTIFICATIONS] as NotificationParameters?
    }

    override fun getWorkingHoursParameters(): WorkingHoursParameters? {
        preLoadParameters()
        return parametersMap[ModuleProductIdsEnum.WORKING_HOURS] as WorkingHoursParameters?
    }
    //endregion

    //region Private Method
    private fun getMobileModulePrameters(productId: Int): List<SyncPackageCustomFieldProductModel> {
        return syncPackageCustomFieldProductQueries.selectBySyncPackageCustomFieldProductEntityProductId(productId.toLong()).executeAsList().map { mapperCustomFieldProduct.toDomain(it) }
    }

    @OptIn(ExperimentalTime::class)
    private fun prepareLoadAllParameters(){

    }

    private fun preLoadParameters(){

    }
    //endregion Private Method
}