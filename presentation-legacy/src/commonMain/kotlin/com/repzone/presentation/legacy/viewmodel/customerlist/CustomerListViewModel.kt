package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.enums.ModuleProductIdsEnum
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
                val fields = iModuleParameterRepository.getMobileModulePrameters(ModuleProductIdsEnum.GEOFENCINGANDROUTETRACKING.value)
                var isActive = false
                var routeParam = 0
                var radarParameter = 0
                fields.forEach { field ->
                    when (field.fieldName) {
                        "IsActive" -> {
                            isActive = field.value.toBoolean()
                        }
                        "VisitPlanSchedules" -> {
                            val itemList = field.itemList?.split(',')
                            routeParam = itemList?.indexOf(field.value) ?: 0
                        }
                        "ShowNearbyCustomers" -> {
                            val itemList = field.itemList?.split(',')
                            radarParameter = itemList?.indexOf(field.value) ?: 0
                        }
                    }
                }

                val atfields = iModuleParameterRepository.getMobileModulePrameters(ModuleProductIdsEnum.ATTENDANCETRACKING.value)
                var atIsActive = false
                var showIconAtHeader = true
                atfields.forEach { field ->
                    when (field.fieldName) {
                        "IsActive" -> {
                            atIsActive = field.value.toBoolean()
                        }
                        "ShowIconAtHeader" -> {
                            showIconAtHeader = field.value.toBoolean()
                        }
                    }
                }

                updateState { currentState ->
                    currentState.copy(
                        isAttendanceTrackingModuleActive = atIsActive,
                        showIconHeader = showIconAtHeader,
                        isTabActive = isActive && routeParam == 1,
                        supposeRouteButton = isActive && routeParam == 0
                    )
                }
            }
            updateUiWithPermissions()
        }
    //endregion

    //region Public Method
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun updateUiWithPermissions() {
        val moduleParams = iModuleParameterRepository.getMobileModuleParameter()

        //region Reports Module
        val fields = iModuleParameterRepository.getMobileModulePrameters(ModuleProductIdsEnum.REPORTS.value)
        var isActive = false
        var isDashboardActive = false
        var isOnlineHubActive = false

        fields.forEach { field ->
            when (field.fieldName) {
                "IsActive" -> isActive = field.value.toBoolean()
                "MobileDashboard" -> isDashboardActive = field.value.toBoolean()
                "Mobile Hub" -> isOnlineHubActive = field.value.toBoolean()
            }
        }
        //endregion Reports Module

        //region Customer add - CRM Operations

        val crmFields = iModuleParameterRepository.getMobileModulePrameters(ModuleProductIdsEnum.CRMOPERATIONS.value)
        var isActiveCRM = false
        var canAddNewCustomer = false

        crmFields.forEach { field ->
            when (field.fieldName) {
                "IsActive" -> isActiveCRM = field.value.toBoolean()
                "CanAddNewCustomer" -> canAddNewCustomer = field.value.toBoolean()
            }
        }

        //endregion Customer add - CRM Operations

        //region Feedback - Messaging and Chat
        val fbFields = iModuleParameterRepository.getMobileModulePrameters(ModuleProductIdsEnum.MESSAGINGANDCHAT.value)
        var isActiveMsgChat = false
        var isActiveFeedback = false
        var isActiveMessaging = false

        fbFields.forEach { field ->
            when (field.fieldName) {
                "IsActive" -> isActiveMsgChat = field.value.toBoolean()
                "Feedback" -> isActiveFeedback = field.value.toBoolean()
                "Messaging" -> isActiveMessaging = field.value.toBoolean()
            }
        }

        //endregion Feedback - Messaging and Chat

        //region Update Ui State
        updateState { currentState ->
            currentState.copy(
                taskButtonContainerVisibility = moduleParams.task,
                routeWellcomeBar = isActive && isDashboardActive,
                isOnlineHubTargetsModuleActive = isActive && isOnlineHubActive,
                isCustomerAddModuleActive = isActiveCRM && canAddNewCustomer,
                isFeedbackModuleActive = isActiveMsgChat && isActiveFeedback,
                isChatButtonContainer = isActiveMsgChat && isActiveMessaging
            )
        }

        //endregion Update Ui State
    }
    //endregion
}


































