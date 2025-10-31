package com.repzone.presentation.legacy.viewmodel.actionmenulist

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame

data class ActionMenuListUiState(
    override val uiFrame: UiFrame = UiFrame(),
): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): ActionMenuListUiState {
        return copy(uiFrame = uiFrame)
    }
}
