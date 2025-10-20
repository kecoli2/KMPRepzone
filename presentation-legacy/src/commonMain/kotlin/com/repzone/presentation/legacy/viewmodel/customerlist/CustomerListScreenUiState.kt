package com.repzone.presentation.legacy.viewmodel.customerlist

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame

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
    val isChatButtonContainer: Boolean = false
): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): CustomerListScreenUiState {
        return copy(uiFrame = uiFrame)
    }
}