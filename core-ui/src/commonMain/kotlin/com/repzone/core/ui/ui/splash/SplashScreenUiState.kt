package com.repzone.core.ui.ui.splash

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame

data class SplashScreenUiState(
    val isControllSucces: Boolean = false,
    override val uiFrame: UiFrame = UiFrame(),
): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): SplashScreenUiState {
        return copy(uiFrame = newUiFrame)
    }

    override fun resetUiFrame(): SplashScreenUiState {
        return SplashScreenUiState()
    }
}