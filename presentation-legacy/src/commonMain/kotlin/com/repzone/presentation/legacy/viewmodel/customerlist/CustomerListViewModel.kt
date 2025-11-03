package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.enums.OnOf
import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.resetUiFrame
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IRepresentativeRepository
import com.repzone.presentation.legacy.model.CustomerGroup
import com.repzone.presentation.legacy.model.enum.CustomerSortOption
import com.repzone.presentation.legacy.viewmodel.customerlist.CustomerListScreenUiState.CustomerListState
import com.repzone.sync.interfaces.ISyncManager
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.model.SyncJobType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class CustomerListViewModel(private val iCustomerListRepository: ICustomerListRepository,
                            private val iModuleParameterRepository: IMobileModuleParameterRepository,
                            private val iSyncManager: ISyncManager,
                            private var representRepository: IRepresentativeRepository,
                            private val iPreferencesManager: IPreferencesManager,
                            private val iDatabaseManager: IDatabaseManager
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
            }
        }
    //endregion

    //region Public Method
    suspend fun onEvent(event: Event) {
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

            is Event.onClickCustomerItem -> {

            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
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
        updateState { currentState ->
            currentState.copy(
                customerListState = CustomerListScreenUiState.CustomerListState.Loading
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
                CustomerSortOption.NAME_ASC -> filtered.sortedBy { it.name?.lowercase() }
                CustomerSortOption.NAME_DESC -> filtered.sortedByDescending { it.name?.lowercase() }
                CustomerSortOption.DATE_ASC -> filtered.sortedBy { it.date?.toEpochMilliseconds() }
                CustomerSortOption.DATE_DESC -> filtered.sortedByDescending { it.date?.toEpochMilliseconds() }
            }

            currentState.copy(
                filteredCustomers = filtered,
                customerListState = if (filtered.isEmpty()) {
                    CustomerListScreenUiState.CustomerListState.Empty
                } else {
                    CustomerListScreenUiState.CustomerListState.Success
                },
                // Seçili filtreleri state'e kaydet
                selectedFilterGroups = selectedGroups,
                selectedSortOption = sortOption
            )
        }
    }
    private suspend fun loadCustomerList(date: Instant?) {
        updateState { currentState ->
            currentState.copy(
                customerListState = CustomerListScreenUiState.CustomerListState.Loading
            )
        }
        try {
            val list = iCustomerListRepository.getCustomerList(date)

            updateState { currentState ->
                currentState.copy(
                    customerListState = if (list.isEmpty()) {
                        CustomerListScreenUiState.CustomerListState.Empty
                    } else {
                        CustomerListScreenUiState.CustomerListState.Success
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
                    customerListState = CustomerListScreenUiState.CustomerListState.Error(
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
    private fun updateUiWithPermissions() {
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
                isDashboardActive = iModuleParameterRepository.getReportsParameters()?.isActive ?: false && iModuleParameterRepository.getReportsParameters()?.mobileDashBoard ?: false
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
    private suspend fun onClickedCustomer(itemModel: CustomerItemModel){
        try {
            updateState { currentState->
                currentState.copy(uiFrame = currentState.uiFrame.copy(true))
            }

            if(iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.groupByParentCustomer == OnOf.ON){

            }




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

        data class onClickCustomerItem(val selectedCustomer: CustomerItemModel): Event()
    }
    //endregion Event
}


































