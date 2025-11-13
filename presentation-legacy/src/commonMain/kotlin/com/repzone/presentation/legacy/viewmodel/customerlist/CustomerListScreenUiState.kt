package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.core.ui.component.floatactionbutton.model.FabAction
import com.repzone.core.ui.component.floatactionbutton.model.FabMenuItem
import com.repzone.domain.model.CustomerByParrentModel
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.RepresentSummary
import com.repzone.presentation.legacy.model.CustomerGroup
import com.repzone.presentation.legacy.model.enum.CustomerSortOption
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
    val isDashboardActive: Boolean = false,

    // Customer List State
    val customerListState: CustomerListState = CustomerListState.Initial,
    val allCustomers: List<CustomerItemModel> = emptyList(),
    val filteredCustomers: List<CustomerItemModel> = emptyList(),
    val selectedDate: Instant? = null,
    val activeCustomerGroup : List<CustomerGroup> = emptyList(),

    val selectedFilterGroups: List<String> = emptyList(),
    val selectedSortOption: CustomerSortOption = CustomerSortOption.DATE_ASC,

    // Represent Summary
    val representSummary: RepresentSummary = RepresentSummary(
        visitTotal = 0,
        visitDoneTotal = 0,
        orderCount = 0,
        orderValue = 0.0,
        formCount = 0,
        activeAppoinmentDayCount = 0
    ),

    val customerParentModel: CustomerByParrentModel? = null,
    val floatActionButtonList: FabAction? = null

): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): CustomerListScreenUiState {
        return copy(uiFrame = uiFrame,)
    }

    override fun resetUiFrame(): CustomerListScreenUiState {
        return CustomerListScreenUiState()
    }

    sealed class CustomerListState {
        data object Initial : CustomerListState()
        data object Loading : CustomerListState()
        data object Empty : CustomerListState()
        data object Success : CustomerListState()

        data class Error(val message: String) : CustomerListState()
    }
}