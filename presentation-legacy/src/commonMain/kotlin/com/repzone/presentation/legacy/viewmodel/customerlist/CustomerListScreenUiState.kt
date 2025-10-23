package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.model.CustomerItemModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class CustomerListScreenUiState(
    override val uiFrame: UiFrame = UiFrame(),
    val isTabActive: Boolean = false,
    val supposeRouteButton: Boolean = true,
    val showIconHeader: Boolean = true,
    val isAttendanceTrackingModuleActive: Boolean = false,
    val taskButtonContainerVisibility: Boolean = false,
    val routeWellcomeBar: Boolean = false,
    val isOnlineHubTargetsModuleActive: Boolean = false,
    val isCustomerAddModuleActive: Boolean = false,
    val isFeedbackModuleActive: Boolean = false,
    val isChatButtonContainer: Boolean = false,
    val isSyncInProgress: Boolean = false,

    // Customer List State
    val customerListState: CustomerListState = CustomerListState.Initial,
    val allCustomers: List<CustomerItemModel> = emptyList(),
    val filteredCustomers: List<CustomerItemModel> = emptyList(),
    val selectedDate: Instant? = null
): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): CustomerListScreenUiState {
        return copy(uiFrame = uiFrame,)
    }

    sealed class CustomerListState {
        data object Initial : CustomerListState()
        data object Loading : CustomerListState()
        data object Empty : CustomerListState()
        data object Success : CustomerListState()
        data class Error(val message: String) : CustomerListState()
    }
}