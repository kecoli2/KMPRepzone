package com.repzone.core.ui.ui.splash

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.common.DomainException

data class SplashScreenUiState(
    val isControllSucces: Boolean = false,
    override val uiFrame: UiFrame = UiFrame(),
    val domainException: DomainException? = null
): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): SplashScreenUiState {
        return copy(uiFrame = newUiFrame)
    }

    override fun resetUiFrame(): SplashScreenUiState {
        return SplashScreenUiState()
    }
}