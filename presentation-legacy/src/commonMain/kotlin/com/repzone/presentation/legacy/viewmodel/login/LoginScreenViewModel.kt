package com.repzone.presentation.legacy.viewmodel.login

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.model.UiFrame
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.clearError
import com.repzone.core.ui.base.hasError
import com.repzone.core.ui.base.setError
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.wrapper.ApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.response.LoginResponse
import kotlinx.coroutines.delay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class LoginScreenViewModel(
    private val tokenApiController: ITokenApiController,
    private val isharedPreferences: IPreferencesManager
) : BaseViewModel<LoginScreenUiState, Nothing>(LoginScreenUiState()) {

    companion object {
        private const val DEBUG_DELAY = 3000L // Test için 3 saniye
        private const val MIN_LOADING_TIME = 1500L // Minimum loading süresi
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun login(email: String, password: String) {
        // Validation
        if (email.isBlank() || password.isBlank()) {
            setError("Kullanıcı adı ve şifre alanları boş olamaz")
            return
        }

        // Loading başlat ve username/password'ı state'e kaydet
        updateState { currentState ->
            currentState.copy(
                username = email,
                password = password,
                uiFrame = currentState.uiFrame.copy(isLoading = true, error = null),
                loadingMessage = "Kullanıcı bilgileri kontrol ediliyor..."
            )
        }

        try {

            // Progress simulation
            updateLoadingMessage("Sunucuya bağlanılıyor...")
            delay(800)

            updateLoadingMessage("Kimlik doğrulanıyor...")
            delay(700)

            val request = LoginRequest(
                email = email,
                password = password,
                uniqueId = Uuid.random().toString(),
            )

            updateLoadingMessage("Oturum oluşturuluyor...")
            val response = tokenApiController.pushToken(request)

            handleLoginResponse(response)

        } catch (e: Exception) {
            setError("Beklenmeyen hata: ${e.message}")
        }
    }
    fun updateUsername(username: String) {
        updateState { it.copy(username = username) }
        if (hasError()) clearError() // Clear error when user starts typing
    }

    fun updatePassword(password: String) {
        updateState { it.copy(password = password) }
        if (hasError()) clearError() // Clear error when user starts typing
    }

    fun clearLoginError() {
        clearError()
    }

    fun resetLoginState() {
        updateState {
            LoginScreenUiState() // Reset to initial state
        }
    }

    private fun updateLoadingMessage(message: String) {
        updateState { currentState ->
            currentState.copy(loadingMessage = message)
        }
    }

    private suspend fun handleLoginResponse(response: ApiResult<LoginResponse>) {
        when (response) {
            is ApiResult.Success -> {
                updateLoadingMessage("Giriş başarılı!")
                delay(500) // Success mesajını göster
                // Token'ı kaydet
                isharedPreferences.setToken(response.data.tokenResponse?.accessToken)

                // Success state
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = UiFrame(), // Reset UiFrame
                        isLoginSuccessful = true,
                        loadingMessage = "Giriş yapılıyor..."
                    )
                }
            }
            is ApiResult.Error -> {
                setError(getErrorMessage(response.exception))
            }
            is ApiResult.Loading -> {
                // Already loading
            }
        }
    }

    private fun getErrorMessage(exception: ApiException): String = when (exception) {
        is ApiException.Unauthorized -> "Kullanıcı adı veya şifre hatalı"
        is ApiException.NetworkError -> "İnternet bağlantısı kontrol edin"
        is ApiException.ValidationError -> exception.errors.joinToString(", ")
        is ApiException.ServerError -> "Sunucu hatası, lütfen daha sonra tekrar deneyin"
        is ApiException.UnknownError -> "Bilinmeyen hata: ${exception.originalMessage ?: "Bilinmeyen hata"}"
    }
}