package com.repzone.presentation.legacy.viewmodel.customerlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.LeaveBagsAtHome
import androidx.compose.material.icons.filled.Report
import com.repzone.core.enums.OnOf
import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.component.floatactionbutton.model.FabAction
import com.repzone.core.ui.component.floatactionbutton.model.FabMenuItem
import com.repzone.core.ui.component.floatactionbutton.model.FabMenuItemType
import com.repzone.core.util.extensions.moveToFirst
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DomainEvent
import com.repzone.domain.events.base.subscribeToEvents
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.IDynamicPageReport
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IRepresentativeRepository
import com.repzone.presentation.legacy.model.CustomerGroup
import com.repzone.presentation.legacy.model.enum.CustomerSortOption
import com.repzone.presentation.legacy.viewmodel.customerlist.CustomerListScreenUiState.CustomerListState
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class CustomerListViewModel(private val iCustomerListRepository: ICustomerListRepository,
                            private val iModuleParameterRepository: IMobileModuleParameterRepository,
                            private val iSyncManager: ISyncManager,
                            private var representRepository: IRepresentativeRepository,
                            private val iPreferencesManager: IPreferencesManager,
                            private val iDatabaseManager: IDatabaseManager,
                            private val iEventBus: IEventBus,
                            private val iUserSession: IUserSession,
                            private val iDynamicReportRepository: IDynamicPageReport
): BaseViewModel<CustomerListScreenUiState, CustomerListViewModel.Event>(CustomerListScreenUiState()) {
    //region Field
    private var searchQuery: String = ""
    //endregion

    //region Properties
    //endregion

    //region Constructor
        init {
            scope.launch {
                updateUiWithPermissions()
                subscribeEventBus()
            }
        }
    //endregion

    //region Public Method
    fun onEvent(event: Event) {
        scope.launch(Dispatchers.IO){
            when(event){
                is Event.StartSync -> {
                    updateState { currentState ->
                        currentState.copy(isSyncInProgress = true)
                    }
                    iSyncManager.startSpecificJobs(listOf(SyncJobType.COMMON_MODULES, SyncJobType.ROUTE))
                    iSyncManager.allJobsStatus.collect { jobStatuses ->
                        when(jobStatuses[SyncJobType.COMMON_MODULES]){
                            is SyncJobStatus.Idle,
                            is SyncJobStatus.Failed,
                            is SyncJobStatus.Success -> {
                                updateState { currentState ->
                                    currentState.copy(isSyncInProgress = false)
                                }
                                updateUiWithPermissions()
                                // Sync tamamlandıktan sonra listeyi yenile
                                loadCustomerList(state.value.selectedDate)
                            }
                            else -> {}
                        }
                    }
                }

                is Event.LoadCustomerList -> {
                    updateState { currentState ->
                        currentState.copy(selectedDate = event.date)
                    }
                    loadCustomerList(event.date)
                }

                is Event.FilterCustomerList -> {
                    filterCustomerList(event.query)
                }

                is Event.RefreshCustomerList -> {
                    loadCustomerList(state.value.selectedDate)
                }

                is Event.ApplyFilter -> {
                    applyFilters(event.selectedGroups, event.sortOption)
                }

                is Event.ClearFilters -> {
                    clearFilters()
                }

                is Event.LogOut -> {
                    iDatabaseManager.logout()
                    iPreferencesManager.setActiveUserCode(0)
                }

                is Event.OnClickCustomerItem -> {
                    onClickedCustomer(event.selectedCustomer)
                }

                is Event.ClearParentCustomer -> {
                    updateState { currentState ->
                        currentState.copy(customerParentModel = null)
                    }
                }
                is Event.OnClickFab -> {
                    if(1==1){

                    }
                }
                is Event.OnClickFabMenuItem -> {
                    if(1==1){

                    }
                }
                else -> {}
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
    }

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private suspend  fun subscribeEventBus(){
        iEventBus.subscribeToEvents().shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            replay = 0
        ).collect { event ->
            when(event){
                is DomainEvent.VisitStartEvent ->{
                    updateState { currentState ->
                        val lst = currentState.filteredCustomers.moveToFirst(
                            predicate =  { it.customerId == event.customerId },
                            transform = { it.copy(visitId = event.visitId) }
                        )
                        currentState.copy(
                            filteredCustomers = lst,
                            representSummary = representRepository.getSummary(currentState.selectedDate, lst))
                    }
                }
                else -> null
            }
        }
    }
    private suspend fun clearFilters() {
        updateState { currentState ->
            currentState.copy(
                selectedFilterGroups = emptyList(),
                selectedSortOption = CustomerSortOption.DATE_ASC
            )
        }
        applyFilters(emptyList(), CustomerSortOption.DATE_ASC)
    }
    private suspend fun applyFilters(selectedGroups: List<String>, sortOption: CustomerSortOption) {
        withContext(Dispatchers.Default){
            updateState { currentState ->
                currentState.copy(
                    customerListState = CustomerListState.Loading
                )
            }

            var filtered = if (searchQuery.isBlank()) {
                state.value.allCustomers
            } else {
                state.value.allCustomers.filter { customer ->
                    customer.name?.contains(searchQuery, ignoreCase = true) == true ||
                            customer.customerCode?.contains(searchQuery, ignoreCase = true) == true || customer.customerId.toString().contains(searchQuery, ignoreCase = true)
                }
            }

            updateState { currentState ->
                if (selectedGroups.isNotEmpty()) {
                    filtered = filtered.filter { customer ->
                        selectedGroups.contains(customer.customerGroupName)
                    }
                }

                // 2. Sıralama uygula
                filtered = when (sortOption) {
                    CustomerSortOption.NAME_ASC -> filtered.sortedBy { it.name?.lowercase() }.sortedByDescending { it.visitId != null && it.finishDate == null }
                    CustomerSortOption.NAME_DESC -> filtered.sortedByDescending { it.name?.lowercase() }.sortedByDescending { it.visitId != null && it.finishDate == null }
                    CustomerSortOption.DATE_ASC -> filtered.sortedBy { it.date?.toEpochMilliseconds() }.sortedByDescending { it.visitId != null && it.finishDate == null }
                    CustomerSortOption.DATE_DESC -> filtered.sortedByDescending { it.date?.toEpochMilliseconds() }.sortedByDescending { it.visitId != null && it.finishDate == null }
                }

                currentState.copy(
                    filteredCustomers = filtered,
                    customerListState = if (filtered.isEmpty()) {
                        CustomerListState.Empty
                    } else {
                        CustomerListState.Success
                    },
                    // Seçili filtreleri state'e kaydet
                    selectedFilterGroups = selectedGroups,
                    selectedSortOption = sortOption
                )
            }
        }
    }
    private suspend fun loadCustomerList(date: Instant?) {
        updateState { currentState ->
            currentState.copy(
                customerListState = CustomerListState.Loading
            )
        }
        try {
            val list = iCustomerListRepository.getCustomerList(date)

            updateState { currentState ->
                currentState.copy(
                    customerListState = if (list.isEmpty()) {
                        CustomerListState.Empty
                    } else {
                        CustomerListState.Success
                    },
                    allCustomers = list,
                    activeCustomerGroup = prepareCustomerGroup(list),
                    representSummary = representRepository.getSummary(date, list)
                )
            }
            applyFilters(state.value.selectedFilterGroups, state.value.selectedSortOption)
        } catch (e: Exception) {
            updateState { currentState ->
                currentState.copy(
                    customerListState = CustomerListState.Error(
                        e.message ?: "Unknown error"
                    )
                )
            }
        }
    }
    private suspend fun filterCustomerList(query: String) {
        searchQuery = query
        applyFilters(state.value.selectedFilterGroups, state.value.selectedSortOption)
    }
    private suspend fun updateUiWithPermissions() {
        //region Reports Module
        val isActive = iModuleParameterRepository.getReportsParameters()?.isActive ?: false
        val isDashboardActive = iModuleParameterRepository.getReportsParameters()?.mobileDashBoard ?: false
        val isOnlineHubActive = iModuleParameterRepository.getReportsParameters()?.mobileHub ?: false
        //endregion Reports Module

        //region Customer add - CRM Operations
        val isActiveCRM = iModuleParameterRepository.getCrmOperationsParameters()?.isActive ?: false
        val canAddNewCustomer = iModuleParameterRepository.getCrmOperationsParameters()?.canAddNewCustomer ?: false
        //endregion Customer add - CRM Operations



        //region Feedback - Messaging and Chat
        val isActiveMsgChat = iModuleParameterRepository.getMessagingChatFeedbackParameters()?.isActive ?: false
        val isActiveFeedback = iModuleParameterRepository.getMessagingChatFeedbackParameters()?.feedback ?: false
        val isActiveMessaging = iModuleParameterRepository.getMessagingChatFeedbackParameters()?.messaging ?: false
        //endregion Feedback - Messaging and Chat

        //region float action button
        var fabAction : FabAction? = null
        val listActionMenuItem : ArrayList<FabMenuItem> = arrayListOf()
        if(isActiveCRM && canAddNewCustomer){
            listActionMenuItem.add(
                FabMenuItem(
                    type = FabMenuItemType.ADD_CUSTOMER,
                    icon = Icons.Default.Add,
                    label = "PopupItemNewCustomerLegacy"
                )
            )
        }

        iUserSession.getActiveSession()?.identity?.let { it ->
            if(it.tenantId == 788 || it.organizationId == 2706){
                listActionMenuItem.add(
                    FabMenuItem(
                        type = FabMenuItemType.ADD_CUSTOMER,
                        icon = Icons.Default.Add,
                        label = "PopupItemNewCustomer"
                    )
                )
            }
        }

        if(isActive && isOnlineHubActive){
            listActionMenuItem.add(
                FabMenuItem(
                    type = FabMenuItemType.REPORT,
                    icon = Icons.Default.Report,
                    label = "PopupItemReports"
                )
            )
            val reportList = iDynamicReportRepository.getAll().filter { it.quickAccessShow } .sortedBy { it.quickAccessOrder }

            reportList.forEach { it->
                listActionMenuItem.add(
                    FabMenuItem(
                        type = FabMenuItemType.REPORT,
                        icon = Icons.Default.Report,
                        label = it.name ?: "",
                        typeId = it.code
                    )
                )
            }
        }

        if(isActiveMsgChat && isActiveFeedback){
            FabMenuItem(
                type = FabMenuItemType.FEEDBACK,
                icon = Icons.Default.Feedback,
                label = "PopupItemFeedback"
            )
        }

        iModuleParameterRepository.getAttendanceTrackingParameters()?.let { it->
            if(it.isActive){
                listActionMenuItem.add(
                    FabMenuItem(
                        type = FabMenuItemType.REQUEST_FOR_PERMIT,
                        icon = Icons.Default.LeaveBagsAtHome,
                        label = "PopupItemLeave"
                    )
                )
            }
        }
        if(listActionMenuItem.isNotEmpty()){
            fabAction = FabAction.Multiple(
                icon = Icons.Default.Add,
                contentDescription = "Add",
                items = listActionMenuItem
            )
        }

        //endregion float action button

        //region Update Ui State
        updateState { currentState ->
            currentState.copy(
                isTabActive = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FIXED_DATES,
                supposeRouteButton = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FIXED_DATES,
                showIconHeader = iModuleParameterRepository.getAttendanceTrackingParameters()?.showIconAtHeader ?: false,
                isAttendanceTrackingModuleActive = iModuleParameterRepository.getAttendanceTrackingParameters()?.isActive ?: false,
                taskButtonContainerVisibility = iModuleParameterRepository.getModule().task,
                routeWellcomeBar = isActive && isDashboardActive,
                isOnlineHubTargetsModuleActive = isActive && isOnlineHubActive,
                isCustomerAddModuleActive = isActiveCRM && canAddNewCustomer,
                isFeedbackModuleActive = isActiveMsgChat && isActiveFeedback,
                isChatButtonContainer = isActiveMsgChat && isActiveMessaging,
                isDashboardActive = iModuleParameterRepository.getReportsParameters()?.isActive ?: false && iModuleParameterRepository.getReportsParameters()?.mobileDashBoard ?: false,
                floatActionButtonList = fabAction
            )
        }

        //endregion Update Ui State
    }
    private fun prepareCustomerGroup(list: List<CustomerItemModel>): List<CustomerGroup> {
        val grpoupList = list.filter { it.customerGroupName?.isNotEmpty() == true }.groupBy { it.customerGroupName }.map { (group, _) ->
            CustomerGroup(group!!, group)
        }
        return grpoupList
    }
    private suspend fun onClickedCustomer(selectedCustomer: CustomerItemModel){
        try {
            updateState { currentState->
                currentState.copy(uiFrame = currentState.uiFrame.copy(true))
            }

            if(iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true
                && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.groupByParentCustomer == OnOf.ON && (selectedCustomer.parentCustomerId
                    ?: 0) == 0L
            ) {
                val parentData = iCustomerListRepository.getAllByParrent(selectedCustomer)
                if(parentData.parrentCustomers.isNotEmpty()){
                    if(parentData.parrentCustomers.size == 1){
                        emitEvent(Event.NavigateVisitPage(parentData.parrentCustomers.first()))
                        return
                    }
                    updateState { currentState->
                        currentState.copy(customerParentModel = parentData)
                    }
                   emitEvent(Event.ShowDialogParentCustomer)
                }else{
                    emitEvent(Event.NavigateVisitPage(selectedCustomer))
                }
                return
            }
            emitEvent(Event.NavigateVisitPage(selectedCustomer))
        }catch (ex: Exception){
            updateState { currentState->
                currentState.copy(uiFrame = currentState.uiFrame.copy(false, ex.message))
            }
        }finally {
            updateState { currentState->
                currentState.copy(uiFrame = currentState.uiFrame.copy(false, null))
            }
        }
    }
    //endregion

    //region Event
    sealed class Event {
        data object StartSync : Event()
        data class FilterCustomerList(val query: String) : Event()
        data class LoadCustomerList(val date: Instant?) : Event()
        data object RefreshCustomerList : Event()
        data class ApplyFilter(val selectedGroups: List<String>, val sortOption: CustomerSortOption) : Event()
        data object ClearFilters : Event()
        data object LogOut: Event()
        data class OnClickCustomerItem(val selectedCustomer: CustomerItemModel): Event()
        data object ShowDialogParentCustomer: Event()
        data class NavigateVisitPage(val selectedCustomer: CustomerItemModel): Event()
        data object ClearParentCustomer: Event()
        data object OnClickFab: Event()
        data class OnClickFabMenuItem(val fabMenuItem: FabMenuItem): Event()
    }
    //endregion Event
}