package com.repzone.presentation.legacy.viewmodel.login

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame

data class LoginScreenUiState(
    override val uiFrame: UiFrame = UiFrame(),
    val isLoginSuccessful: Boolean = false,
    val loadingMessage: String = "Giriş yapılıyor...",
    val username: String = "trdemouser6@rep.zone",
    val password: String = "Le4g7H") : HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): LoginScreenUiState {
        return copy(uiFrame = newUiFrame)
    }
    val isFormValid: Boolean get() = username.isNotBlank() && password.isNotBlank()
    val canSubmit: Boolean get() = isFormValid && !uiFrame.isLoading
}