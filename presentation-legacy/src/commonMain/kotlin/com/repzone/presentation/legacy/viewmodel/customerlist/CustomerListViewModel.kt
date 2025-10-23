package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
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
                            private val iSyncManager: ISyncManager
): BaseViewModel<CustomerListScreenUiState, CustomerListViewModel.Event>(CustomerListScreenUiState()) {
    //region Field
    private val _customerList = MutableStateFlow<List<CustomerItemModel>>(emptyList())
    val customerList: StateFlow<List<CustomerItemModel>> = _customerList.asStateFlow()
    private var utcDate: Instant? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
        init {
            scope.launch {
                updateUiWithPermissions()
                loadCustomerList(null)
            }
        }
    //endregion

    //region Public Method
    fun onEvent(event: Event) {
        when(event){
            Event.StartSync -> {
                updateState { currentState ->
                    currentState.copy(isSyncInProgress = true)
                }
                scope.launch {
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
            }

            is Event.LoadCustomerList -> {
                scope.launch {
                    updateState { currentState ->
                        currentState.copy(selectedDate = event.date)
                    }
                    loadCustomerList(event.date)
                }
            }

            is Event.FilterCustomerList -> {
                filterCustomerList(event.query)
            }

            Event.RefreshCustomerList -> {
                scope.launch {
                    loadCustomerList(state.value.selectedDate)
                }
            }
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method

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
                    filteredCustomers = list
                )
            }
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

    private fun filterCustomerList(query: String) {
        updateState { currentState ->
            val filtered = if (query.isBlank()) {
                currentState.allCustomers
            } else {
                currentState.allCustomers.filter { customer ->
                    customer.name?.contains(query, ignoreCase = true) == true ||
                            customer.customerCode?.contains(query, ignoreCase = true) == true
                }
            }

            currentState.copy(
                filteredCustomers = filtered,
                customerListState = if (filtered.isEmpty()) {
                    CustomerListScreenUiState.CustomerListState.Empty
                } else {
                    CustomerListScreenUiState.CustomerListState.Success
                }
            )
        }
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
                isTabActive = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FLEXIBLE_DATES,
                supposeRouteButton = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FIXED_DATES,
                showIconHeader = iModuleParameterRepository.getAttendanceTrackingParameters()?.showIconAtHeader ?: false,
                isAttendanceTrackingModuleActive = iModuleParameterRepository.getAttendanceTrackingParameters()?.isActive ?: false,
                taskButtonContainerVisibility = iModuleParameterRepository.getModule().task,
                routeWellcomeBar = isActive && isDashboardActive,
                isOnlineHubTargetsModuleActive = isActive && isOnlineHubActive,
                isCustomerAddModuleActive = isActiveCRM && canAddNewCustomer,
                isFeedbackModuleActive = isActiveMsgChat && isActiveFeedback,
                isChatButtonContainer = isActiveMsgChat && isActiveMessaging,
            )
        }

        //endregion Update Ui State
    }
    //endregion

    //region Event
    sealed class Event {
        data object StartSync : Event()
        data class FilterCustomerList(val query: String) : Event()
        data class LoadCustomerList(val date: Instant?) : Event()
        data object RefreshCustomerList : Event()
    }
    //endregion Event
}


































