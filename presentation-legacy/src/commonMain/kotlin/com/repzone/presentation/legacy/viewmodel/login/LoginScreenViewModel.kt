package com.repzone.presentation.legacy.viewmodel.login

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.model.UiFrame
import com.repzone.core.model.UserSessionModel
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.clearError
import com.repzone.core.ui.base.hasError
import com.repzone.core.ui.base.setErrorStringResource
import com.repzone.core.util.extensions.toJson
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.wrapper.ApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.response.LoginResponse
import kotlinx.coroutines.delay
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.authenticating
import repzonemobile.core.generated.resources.check_internet
import repzonemobile.core.generated.resources.checking_credentials
import repzonemobile.core.generated.resources.creating_session
import repzonemobile.core.generated.resources.invalid_credentials
import repzonemobile.core.generated.resources.logging_in
import repzonemobile.core.generated.resources.login_issue
import repzonemobile.core.generated.resources.login_successful
import repzonemobile.core.generated.resources.server_error
import repzonemobile.core.generated.resources.unknown_error
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class LoginScreenViewModel(
    private val tokenApiController: ITokenApiController,
    private val isharedPreferences: IPreferencesManager,
    private val iDatabaseManager: IDatabaseManager
) : BaseViewModel<LoginScreenUiState, Nothing>(LoginScreenUiState()) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    @OptIn(ExperimentalUuidApi::class)
    suspend fun login(email: String, password: String) {
        // Validation
        if (email.isBlank() || password.isBlank()) {
            setErrorStringResource(Res.string.login_issue)
            return
        }

        updateState { currentState ->
            currentState.copy(
                username = email,
                password = password,
                uiFrame = currentState.uiFrame.copy(isLoading = true, error = null),
                loadingMessage = Res.string.checking_credentials
            )
        }

        try {
            // Progress simulation
            updateLoadingMessage(Res.string.checking_credentials)
            delay(800)

            updateLoadingMessage(Res.string.authenticating)
            delay(700)

            val request = LoginRequest(
                email = email,
                password = password,
                uniqueId = Uuid.random().toString(),
            )

            updateLoadingMessage(Res.string.creating_session)
            val response = tokenApiController.pushToken(request)

            handleLoginResponse(response)

        } catch (e: Exception) {
            setErrorStringResource(Res.string.unknown_error,listOf(e.message))
        }
    }
    fun updateUsername(username: String) {
        updateState { it.copy(username = username) }
        if (hasError()) clearError()
    }

    fun updatePassword(password: String) {
        updateState { it.copy(password = password) }
        if (hasError()) clearError()
    }

    fun clearLoginError() {
        clearError()
    }

    fun resetLoginState() {
        updateState {
            LoginScreenUiState() // Reset to initial state
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun updateLoadingMessage(message: Any) {
        updateState { currentState ->
            currentState.copy(loadingMessage = message)
        }
    }

    private suspend fun handleLoginResponse(response: ApiResult<LoginResponse>) {
        when (response) {
            is ApiResult.Success -> {
                updateLoadingMessage(Res.string.login_successful)
                delay(500) // Success mesajını göster
                // Token'ı kaydet
                isharedPreferences.setActiveUserCode(response.data.userId)
                val activeSessions = UserSessionModel(
                    userId = response.data.userId,
                    firstName = response.data.firstName,
                    lastName = response.data.lastName,
                    phone = response.data.phone,
                    email = response.data.email,
                    profileImageUrl = response.data.profileImageUrl,
                    token = response.data.tokenResponse?.accessToken
                )
                isharedPreferences.setUserSessions(activeSessions.toJson())
                iDatabaseManager.switchUser(response.data.userId)
                // Success state
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = UiFrame(), // Reset UiFrame
                        isLoginSuccessful = true,
                        loadingMessage = Res.string.logging_in
                    )
                }
            }
            is ApiResult.Error -> {
                prepareErrorMessage(response.exception)
            }
            is ApiResult.Loading -> {
                // Already loading
            }
        }
    }

    private fun prepareErrorMessage(exception: ApiException) {
        when (exception){
            is ApiException.Unauthorized ->{
                setErrorStringResource(Res.string.invalid_credentials)
            }
            is ApiException.NetworkError ->{
                setErrorStringResource(Res.string.check_internet)
            }
            is ApiException.ServerError ->{
                setErrorStringResource(Res.string.server_error)
            }
            is ApiException.UnknownError ->{
                setErrorStringResource(Res.string.unknown_error, listOf(exception.originalMessage))
            }

            is ApiException.ValidationError -> {
                setErrorStringResource(Res.string.unknown_error, listOf(exception.errors.joinToString(", ")))
            }
        }

    }
    //endregion
}