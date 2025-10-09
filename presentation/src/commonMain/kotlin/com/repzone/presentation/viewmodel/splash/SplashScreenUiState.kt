package com.repzone.presentation.viewmodel.splash

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame

data class SplashScreenUiState(
    override val uiFrame: UiFrame = UiFrame(),
    val isCoplete: Boolean = false,
    val loadingMessage: String = "Loading..."
): HasUiFrame {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun copyWithUiFrame(newUiFrame: UiFrame): SplashScreenUiState {
        return copy(uiFrame = newUiFrame)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}