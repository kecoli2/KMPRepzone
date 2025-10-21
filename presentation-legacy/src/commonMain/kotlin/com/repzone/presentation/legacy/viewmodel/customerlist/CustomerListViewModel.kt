package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import kotlinx.coroutines.launch

class CustomerListViewModel(private val iCustomerListRepository: ICustomerListRepository,
                            private val iModuleParameterRepository: IMobileModuleParameterRepository
): BaseViewModel<CustomerListScreenUiState, Nothing>(CustomerListScreenUiState()) {
    //region Field
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
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
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
                taskButtonContainerVisibility = iModuleParameterRepository.getModule().task,
                routeWellcomeBar = isActive && isDashboardActive,
                isOnlineHubTargetsModuleActive = isActive && isOnlineHubActive,
                isCustomerAddModuleActive = isActiveCRM && canAddNewCustomer,
                isFeedbackModuleActive = isActiveMsgChat && isActiveFeedback,
                isChatButtonContainer = isActiveMsgChat && isActiveMessaging,
                isAttendanceTrackingModuleActive = iModuleParameterRepository.getAttendanceTrackingParameters()?.isActive ?: false,
                showIconHeader = iModuleParameterRepository.getAttendanceTrackingParameters()?.showIconAtHeader ?: false,
                isTabActive = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FLEXIBLE_DATES ,
                supposeRouteButton = iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.isActive == true && iModuleParameterRepository.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FIXED_DATES
            )
        }

        //endregion Update Ui State
    }
    //endregion
}


































