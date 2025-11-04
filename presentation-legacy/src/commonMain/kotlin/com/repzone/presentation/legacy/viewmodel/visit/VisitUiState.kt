package com.repzone.presentation.legacy.viewmodel.visit

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.util.models.VisitButtonItem
import com.repzone.domain.util.models.VisitActionItem

data class VisitUiState(
    override val uiFrame: UiFrame = UiFrame(),
    val actionMenuList: List<VisitActionItem> = emptyList(),
    val actionButtonList: List<VisitButtonItem> = emptyList(),
    val menuListState: ActionMenuListState = ActionMenuListState.Initial,
    val buttonListState: ActionMenuListState = ActionMenuListState.Initial,
    val visibleBalanceText : Boolean = true,
    val customerBalance: Double = 0.0,
    val customerRiskBalance: Double = 0.0,
    val appoinmentDescription : String = ""
): HasUiFrame {
    override fun copyWithUiFrame(newUiFrame: UiFrame): VisitUiState {
        return copy(uiFrame = uiFrame)
    }

    sealed class ActionMenuListState {
        data object Initial : ActionMenuListState()
        data object Loading : ActionMenuListState()
        data object Empty : ActionMenuListState()
        data object Success : ActionMenuListState()
        data class Error(val message: String) : ActionMenuListState()
    }
}
