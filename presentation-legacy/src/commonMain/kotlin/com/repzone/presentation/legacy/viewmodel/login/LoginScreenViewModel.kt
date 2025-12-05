package com.repzone.presentation.legacy.viewmodel.login

import com.repzone.core.enums.StateType
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.model.UiFrame
import com.repzone.core.model.UserSessionModel
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.clearError
import com.repzone.core.ui.base.hasError
import com.repzone.core.ui.base.setError
import com.repzone.core.ui.base.setErrorStringResource
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.jsonToModel
import com.repzone.core.util.extensions.toJson
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.domain.common.onError
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.wrapper.ApiException
import com.repzone.network.http.wrapper.ApiResult
import com.repzone.network.models.request.LoginRequest
import com.repzone.network.models.response.LoginResponse
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

@Suppress("UNCHECKED_CAST")
class LoginScreenViewModel(
    private val tokenApiController: ITokenApiController,
    private val isharedPreferences: IPreferencesManager,
    private val iDatabaseManager: IDatabaseManager,
    private val iThemeManager: ThemeManager
) : BaseViewModel<LoginScreenUiState, LoginScreenViewModel.Event>(LoginScreenUiState()) {
    //region Public Method
    @OptIn(ExperimentalUuidApi::class)
    suspend fun login(email: String, password: String) {
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
            updateLoadingMessage(Res.string.checking_credentials)
            updateLoadingMessage(Res.string.authenticating)
            val request = LoginRequest(
                email = email,
                password = password,
                uniqueId = Uuid.random().toString(),
            )

            updateLoadingMessage(Res.string.creating_session)
            val response = tokenApiController.pushToken(request)

            handleLoginResponse(response)

        } catch (e: Exception) {
            setErrorStringResource(Res.string.unknown_error, listOf(e.message) as List<Any>)
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
                // Token'ı kaydet
                isharedPreferences.setActiveUserCode(response.data.userId)
                val activeSessions = UserSessionModel(
                    userId = response.data.userId,
                    firstName = response.data.firstName,
                    lastName = response.data.lastName,
                    phone = response.data.phone,
                    email = response.data.email,
                    profileImageUrl = response.data.profileImageUrl,
                    token = response.data.tokenResponse?.accessToken,
                    tokenType = response.data.tokenResponse?.tokenType ?: "bearer"
                )
                checkUserIdentity(activeSessions.tokenType, activeSessions.token!!)
                isharedPreferences.setUserSessions(activeSessions.toJson())
                iDatabaseManager.switchUser(response.data.userId)
                // Success state
                updateState { currentState ->
                    currentState.copy(
                        uiFrame = UiFrame(), // Reset UiFrame
                        loadingMessage = Res.string.logging_in
                    )
                }
                iThemeManager.loadSavedSettings()
                emitEvent(Event.Login)
            }
            is ApiResult.Error -> {
                setError(response.exception.message)
                //prepareErrorMessage(response.exception)
            }
            is ApiResult.Loading -> {
            }
        }
    }

    private suspend fun checkUserIdentity(tokenType: String, token: String){
        val responseInfo = tokenApiController.verifyIdentity(tokenType, token)

        when(responseInfo){
            is ApiResult.Success -> {
                if (responseInfo.data.state == StateType.ACTIVE){
                    val model = isharedPreferences.getUserSessions()?.jsonToModel<UserSessionModel>()
                    model?.identity = responseInfo.data
                    model?.let {
                        isharedPreferences.setUserSessions(it.toJson())
                    }
                }
            }
            is ApiResult.Error -> {
                setError(responseInfo.exception.message)
            }

            ApiResult.Loading -> {

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
                setErrorStringResource(Res.string.unknown_error,
                    listOf(exception.originalMessage) as List<Any>
                )
            }

            is ApiException.ValidationError -> {
                setErrorStringResource(Res.string.unknown_error, listOf(exception.errors.joinToString(", ")))
            }
        }

    }
    //endregion

    //region Event
    sealed class Event {
        data object Login : Event()
    }
    //endregion Event
}