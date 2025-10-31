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
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.model.MobileParameterModel
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.network.dto.PackageCustomFieldProductDto
import kotlinx.coroutines.runBlocking
import kotlin.time.ExperimentalTime

class MobileModuleParameterRepositoryImpl(
        private val iDatabaseManager: IDatabaseManager,
        private val mapperCustomFieldProduct: MapperDto<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel, PackageCustomFieldProductDto>,
        private val iUserSession: IUserSession ): IMobileModuleParameterRepository {

    //region Fields
      private var parametersMap : MutableMap<ModuleProductIdsEnum, Any> = mutableMapOf()
      private val modules = MobileParameterModel()
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
        return runBlocking {
            iDatabaseManager.getDatabase().syncPackageCustomFieldProductEntityQueries.selectBySyncPackageCustomFieldProductEntityProductId(productId.toLong()).executeAsList().map { mapperCustomFieldProduct.toDomain(it) }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun prepareLoadAllParameters(){
        parametersMap.clear()
        ModuleProductIdsEnum.entries.forEach { pkg ->
            val parameters = getMobileModulePrameters(pkg.value)
            when (pkg) {
                ModuleProductIdsEnum.ORDERS -> {
                    modules.order = parameters.getBooleanValue("IsActive", false)
                    val model = OrderParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        allowDraft = parameters.getBooleanValue("AllowDraft", false),
                        allowStockEntry = parameters.getEnumValue<OnOf>("AllowStockEntry", OnOf.OFF),
                        stockEntryFormId = parameters.getEnumValue<StockEntryForm>("StockEntryFormId", StockEntryForm.PRODUCTLIST),
                        askReasonCode = parameters.getEnumValue<OnOf>("AskReasonCode", OnOf.OFF),
                        printingAllowed = parameters.getBooleanValue("PrintingAllowed", false),
                        keepDraftAfterProcess = parameters.getBooleanValue("KeepDraftAfterProcess", false),
                        allowWarehouseSelection = parameters.getBooleanValue("AllowWarehouseSelection", false),
                        showDocumentLabels = parameters.getEnumValue<ShowDocumentLabelsOrderType>("ShowDocumentLabels", ShowDocumentLabelsOrderType.No),
                        formOrganizationSelectionOptions = parameters.getEnumValue<OrganizationType>("FormOrganizationSelectionOptions", OrganizationType.CUSTOMERORGANIZATION),
                        showProductOrderPending = parameters.getBooleanValue("ShowProductOrderPending", false),
                        showPaytermSelection = parameters.getEnumValue<ShowPaytermSelectionType>("ShowPaytermSelection", ShowPaytermSelectionType.No),
                        showWarehouseSelection = parameters.getEnumValue<UserSelectionType>("ShowWarehouseSelection", UserSelectionType.No),
                        dayToShip = parameters.getIntValue("DaysToShip", 0),
                        autoFilterByStock = parameters.getEnumValue<AutoFilterByStock>("AutoFilterByStock", AutoFilterByStock.NO),
                        showProductOrderAvailableStock = parameters.getBooleanValue("ShowProductOrderAvailableStock", false),
                        showProductOrderTransitStock = parameters.getBooleanValue("ShowProductOrderTransitStock", false)
                    )
                    parametersMap.put(ModuleProductIdsEnum.ORDERS, model)
                }
                ModuleProductIdsEnum.INVOICESANDEINVOICES -> {
                    modules.invoice = parameters.getBooleanValue("IsActive", false)
                    val model = InvoiceEInvoiceParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        printingAllowed = parameters.getBooleanValue("PrintingAllowed", false),
                        showDocumentLabels = parameters.getEnumValue<ShowDocumentLabelsType>("ShowDocumentLabels", ShowDocumentLabelsType.No)
                    )

                    parametersMap.put(ModuleProductIdsEnum.INVOICESANDEINVOICES, model)
                }
                ModuleProductIdsEnum.BUSINESSINTELLIGENCE -> {
                    modules.qlikReport = parameters.getBooleanValue("IsActive", false)
                    val model = BusinessIntelligenceParameters(
                        isActive = parameters.getBooleanValue("IsActive", false)
                    )
                    parametersMap.put(ModuleProductIdsEnum.BUSINESSINTELLIGENCE, model)
                }
                ModuleProductIdsEnum.DIGITALCONTENTSHARING -> {
                    modules.drive = parameters.getBooleanValue("IsActive", false)
                    val model = DigitalContentSharingParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                    )
                    parametersMap.put(ModuleProductIdsEnum.DIGITALCONTENTSHARING, model)

                }
                ModuleProductIdsEnum.TASKMANAGEMENT -> {
                    modules.task = parameters.getBooleanValue("IsActive", false)
                    val model = TaskManagmentParameters(
                        isActive = parameters.getBooleanValue("IsActive", false)
                    )
                    parametersMap.put(ModuleProductIdsEnum.TASKMANAGEMENT, model)
                }
                ModuleProductIdsEnum.EAGLEEYEANDLOCATIONTRACKING -> {
                    modules.eagleEye = parameters.getBooleanValue("IsActive", false)
                    val model = EagleEyeLocationTrackingParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        trackStatus = parameters.getBooleanValue("TrackStatus", false),
                        trackInterval = parameters.getIntValue("TrackInterval", 0),
                        trackStartTime = parameters.getDateTimeValue("TrackStartTime"),
                        trackEndTime = parameters.getDateTimeValue("TrackEndTime"),
                        trackDays = parameters.getStringValue("TrackDays")?.split(",")?.map { it.toInt() } ?: emptyList(),
                        backgroundTracking = parameters.getBooleanValue("BackgroundTracking", false),
                        showRepresentativePhoneDetails = parameters.getBooleanValue("ShowRepresentativePhoneDetails", false)
                    )
                    parametersMap.put(ModuleProductIdsEnum.EAGLEEYEANDLOCATIONTRACKING, model)
                }
                ModuleProductIdsEnum.MESSAGINGANDCHAT -> {
                    modules.chat = parameters.getBooleanValue("IsActive", false)
                    val model = MessagingChatFeedbackParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        messaging = parameters.getBooleanValue("Messaging", false),
                        feedback = parameters.getBooleanValue("Feedback", false),
                    )
                    parametersMap.put(ModuleProductIdsEnum.MESSAGINGANDCHAT, model)
                }
                ModuleProductIdsEnum.GEOFENCINGANDROUTETRACKING -> {
                    modules.geoFence = parameters.getBooleanValue("IsActive", false)
                    val model = GeofenceRouteTrackingParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        distance = parameters.getIntValue("Distance",250),
                        violationAction = parameters.getEnumValue<ViolationActionType>("ViolationAction", ViolationActionType.CONTINUE),
                        visitPlanSchedules = parameters.getEnumValue<VisitPlanSchedulesType>("VisitPlanSchedules",VisitPlanSchedulesType.FIXED_DATES),
                        navigation = parameters.getEnumValue<OnOf>("Navigation", OnOf.OFF),
                        previousDayVisits = parameters.getEnumValue<PreviousDayVisitsType>("PreviousDayVisits ", PreviousDayVisitsType.CONTINUE),
                        groupByParentCustomer = parameters.getEnumValue<OnOf>("GroupByParentCustomer", OnOf.OFF),
                        customerGPSUpdate = parameters.getEnumValue<CustomerGPSUpdateType>("CustomerGPSUpdate", CustomerGPSUpdateType.DONT_ALLOW),
                        visitNoteEntry = parameters.getEnumValue<OnOf>("VisitNoteEntry", OnOf.OFF),
                        customerNoteEntry = parameters.getEnumValue<OnOf>("CustomerNoteEntry", OnOf.OFF),
                        askVisitTypeEntryDuringVisitStart = parameters.getBooleanValue("AskVisitTypeEntryDuringVisitStart", false),
                        controlGPSWrtVisitType = parameters.getBooleanValue("ControlGPSWrtVisitType", false),
                        formMandatoryCheckWrtVisitType = parameters.getBooleanValue("FormMandatoryCheckWrtVisitType", false),
                        showNearbyCustomers = parameters.getEnumValue<ShowNearbyCustomersType>("ShowNearbyCustomers", ShowNearbyCustomersType.NO),
                    )
                    parametersMap.put(ModuleProductIdsEnum.GEOFENCINGANDROUTETRACKING, model)
                }
                ModuleProductIdsEnum.CUSTOMMOBILEFORMS -> {
                    modules.form = parameters.getBooleanValue("IsActive", false)
                    val models = CustomMobileFormsParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        allowDraft = parameters.getBooleanValue("AllowDraft", false),
                        askReasonCode = parameters.getEnumValue<OnOf>("AskReasonCode", OnOf.OFF),
                        allowCloudDownload = parameters.getBooleanValue("AllowCloudDownload", false),
                        keepDraftAfterProcess = parameters.getBooleanValue("KeepDraftAfterProcess", false),
                        orderOrganizationSelectionOptions = parameters.getEnumValue<OrganizationType>("OrderOrganizationSelectionOptions", OrganizationType.CUSTOMERORGANIZATION)
                    )
                    parametersMap.put(ModuleProductIdsEnum.CUSTOMMOBILEFORMS, models)

                }
                ModuleProductIdsEnum.ATTENDANCETRACKING -> {
                    modules.timeTracker = parameters.getBooleanValue("IsActive", false)
                    val model = AttendanceTrackingParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        multiLogin = parameters.getBooleanValue("MultiLogin", false),
                        leaveManagement = parameters.getBooleanValue("LeaveManagement", false),
                        leaveDurationLimit = parameters.getEnumValue<LeaveDurationLimitType>("LeaveDurationLimit", LeaveDurationLimitType.DAY_1),
                        returnDateLimit = parameters.getEnumValue<LeaveDurationLimitType>("ReturnDateLimit", LeaveDurationLimitType.DAY_1),
                        permissionDescription = parameters.getBooleanValue("PermissionDescription", false),
                        permissionPhoto =  parameters.getBooleanValue("PermissionPhoto", false),
                        askLeaveStatusAtStartup = parameters.getBooleanValue("AskLeaveStatusAtStartup", false),
                        showIconAtHeader = parameters.getBooleanValue("ShowIconAtHeader", false),
                        backwardLeaveDurationLimit = parameters.getIntValue("BackwardLeaveDurationLimit", 0)
                    )
                    parametersMap.put(ModuleProductIdsEnum.ATTENDANCETRACKING, model)
                }
                ModuleProductIdsEnum.REPORTS -> {
                    modules.report = parameters.getBooleanValue("IsActive", false)
                    val model = ReportsParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        mobileDashBoard = parameters.getBooleanValue("MobileDashboard", false),
                        mobileHub = parameters.getBooleanValue("Mobile Hub", false)
                    )
                    parametersMap.put(ModuleProductIdsEnum.REPORTS, model)
                }
                ModuleProductIdsEnum.PAYMENTCOLLECTION -> {
                    modules.payment = parameters.getBooleanValue("IsActive", false)
                    val model = PaymentCollectionsParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        printingAllowed = parameters.getBooleanValue("PrintingAllowed", false),
                    )
                    parametersMap.put(ModuleProductIdsEnum.PAYMENTCOLLECTION, model)
                }
                ModuleProductIdsEnum.DISPATCHES -> {
                    modules.dispatch = parameters.getBooleanValue("IsActive", false)
                    val model = DispatchesParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        printingAllowed = parameters.getBooleanValue("PrintingAllowed", false),
                        showDocumentLabels = parameters.getEnumValue<ShowDocumentLabelsType>("ShowDocumentLabels", ShowDocumentLabelsType.No)
                    )
                    parametersMap.put(ModuleProductIdsEnum.DISPATCHES, model)
                }
                ModuleProductIdsEnum.GAMIFICATION -> {
                    modules.gamification = parameters.getBooleanValue("IsActive", false)
                    val model = GamificationParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        slashScreen = parameters.getEnumValue<OnOf>("SplashScreen", OnOf.OFF),
                        infoScreen = parameters.getEnumValue<OnOf>("InfoScreen", OnOf.OFF)
                    )
                    parametersMap.put(ModuleProductIdsEnum.GAMIFICATION, model)

                }
                ModuleProductIdsEnum.CRMOPERATIONS -> {
                    modules.crmOperations = parameters.getBooleanValue("IsActive", false)
                    val model = CrmOperationsParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        canAddNewCustomer = parameters.getBooleanValue("CanAddNewCustomer", false),
                        groupCanBeChanged = parameters.getBooleanValue("GroupCanBeChanged", false),
                        afterAddingTheCustomerCanVisit = parameters.getBooleanValue("AfterAddingTheCustomerCanVisit", false),
                        defaultCustomerGroupId = parameters.getIntValue("DefaultCustomerGroupId", null)
                    )
                    parametersMap.put(ModuleProductIdsEnum.CRMOPERATIONS, model)
                }
                ModuleProductIdsEnum.DIGITALPRODUCTCATALOG -> {
                    val model = DigitalProductCatalogParameters(
                        isActive = parameters.getBooleanValue("IsActive", false)
                    )
                    parametersMap.put(ModuleProductIdsEnum.DIGITALPRODUCTCATALOG, model)
                }
                ModuleProductIdsEnum.QUICKACCESS -> {
                    val model = QuickAccessParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        quickAccessForm = parameters.getIntValue("QuickAccessForm", null)
                    )
                    parametersMap.put(ModuleProductIdsEnum.QUICKACCESS, model)
                }
                ModuleProductIdsEnum.PRODUCTALLOCATION -> {
                    val model = ProductAllocationParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                    )
                    parametersMap.put(ModuleProductIdsEnum.PRODUCTALLOCATION, model)
                }
                ModuleProductIdsEnum.VISIT -> {
                    val model = VisitParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        synchWarehouseStockrAtVisitStart = parameters.getBooleanValue("SynchWarehouseStockrAtVisitStart", false),
                        synchReservedStockrAtVisitStart = parameters.getBooleanValue("SynchReservedStockrAtVisitStart", false),
                        synchTransitStockrAtVisitStart = parameters.getBooleanValue("SynchTransitStockrAtVisitStart", false),
                        orderFullfillment = null,
                        representativeCanSelectMultipleOrganizations =  parameters.getBooleanValue("RepresentativeCanSelectMultipleOrganizations", false),
                    )
                    parametersMap.put(ModuleProductIdsEnum.VISIT, model)
                }
                ModuleProductIdsEnum.NOTIFICATIONS ->{
                    val model = NotificationParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        storeDuration = parameters.getEnumValue<StoreDurationType>("StoreDuration", StoreDurationType.DAILY)
                    )
                    parametersMap.put(ModuleProductIdsEnum.NOTIFICATIONS, model)
                }
                ModuleProductIdsEnum.WORKING_HOURS -> {
                    val model = WorkingHoursParameters(
                        isActive = parameters.getBooleanValue("IsActive", false),
                        howManyHoursAfterTheFirstVisit = parameters.getIntValue("HowManyHoursAfterTheFirstVisit", 0),
                        emailList = parameters.getStringValue("EmailList", "").split(",")
                    )
                    parametersMap.put(ModuleProductIdsEnum.WORKING_HOURS, model)
                }
            }
        }
        iUserSession.getReloadParameters()
    }

    private fun preLoadParameters(){
        if(iUserSession.getReloadParameters()){
            prepareLoadAllParameters()
        }
    }
    //endregion Private Method
}