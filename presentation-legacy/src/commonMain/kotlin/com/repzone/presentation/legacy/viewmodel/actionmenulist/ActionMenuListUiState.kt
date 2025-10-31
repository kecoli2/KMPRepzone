package com.repzone.presentation.legacy.viewmodel.actionmenulist

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.domain.util.models.ActionMenuListItem

data class ActionMenuListUiState(
    override val uiFrame: UiFrame = UiFrame(),
    val actionMenuList: List<ActionMenuListItem> = emptyList(),
    val actionButtonList: List<ActionButtonListItem> = emptyList(),
    val menuListState: ActionMenuListState = ActionMenuListState.Initial,
    val buttonListState: ActionMenuListState = ActionMenuListState.Initial
): HasUiFrame {
    override fun copyWithUiFrame(newUiFrame: UiFrame): ActionMenuListUiState {
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
