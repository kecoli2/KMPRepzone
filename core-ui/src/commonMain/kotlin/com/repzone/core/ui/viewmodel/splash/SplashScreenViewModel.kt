package com.repzone.core.ui.viewmodel.splash

import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.core.ui.ui.splash.SplashScreenUiState
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.wrapper.ApiResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val tokenController: ITokenApiController,
                            private val userSession: IUserSession,):
    BaseViewModel<SplashScreenUiState, SplashScreenViewModel.Event>(SplashScreenUiState()) {

    //region Field
        private var _nextOprerations : MutableMap<SplashScreenOperation,Any?> = mutableMapOf()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    init {
        prepareNextOperation()
        scope.launch {
            nextOperation()
        }
    }
    //endregion

    //region Public Method
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private suspend fun nextOperation(){
        val key = _nextOprerations.firstNotNullOfOrNull { it.key }
        if(key == null){
            sendEvent(Event.ControllSucces)
            return
        }
        when(key){
            SplashScreenOperation.CHECK_TOKEN -> {
                checkToken()
            }
            SplashScreenOperation.REGISTER_SMS_SERVICE ->{
                registerSmsService()
            }
            SplashScreenOperation.REGISTER_NOTIFICATION_SERVICE -> {
                registerNotificationService()
            }
        }
    }
    private fun prepareNextOperation(){
        _nextOprerations.put(SplashScreenOperation.CHECK_TOKEN, null)
        _nextOprerations.put(SplashScreenOperation.REGISTER_SMS_SERVICE, null)
        _nextOprerations.put(SplashScreenOperation.REGISTER_NOTIFICATION_SERVICE, null)
    }
    private suspend fun checkToken(){
        try {

            if(userSession.getActiveSession() == null || userSession.getActiveSession()?.token == null){
                _nextOprerations.clear()
                sendEvent(Event.NavigateToLogin)
            }else{
                val responseInfo = tokenController.verifyIdentity(userSession.getActiveSession()!!.tokenType, userSession.getActiveSession()!!.token!!)

                when(responseInfo){
                    is ApiResult.Success -> {
                        if (responseInfo.data.state == 1){
                            userSession.getActiveSession()!!.identity = responseInfo.data
                            userSession.save()
                            _nextOprerations.remove(SplashScreenOperation.CHECK_TOKEN)
                            nextOperation()
                        }else{
                            _nextOprerations.clear()
                            sendEvent(Event.NavigateToLogin)
                        }
                    }
                    is ApiResult.Error -> {
                        setError(responseInfo.exception.message)
                        delay(2000)
                        sendEvent(Event.NavigateToLogin)
                    }

                    ApiResult.Loading -> {

                    }
                }

            }
        }catch (ex: Exception){
            setError(ex.message)
        }
    }
    private suspend fun registerSmsService(){
        _nextOprerations.remove(SplashScreenOperation.REGISTER_SMS_SERVICE)
        nextOperation()
    }
    private suspend fun registerNotificationService(){
        _nextOprerations.remove(SplashScreenOperation.REGISTER_NOTIFICATION_SERVICE)
        nextOperation()
    }
    //endregion

    //region Event
     sealed class Event {
        object ControllSucces: Event()
        object NavigateToLogin: Event()
     }
    //endregion Event

    //region Enums
    private enum class SplashScreenOperation{
        CHECK_TOKEN,
        REGISTER_SMS_SERVICE,
        REGISTER_NOTIFICATION_SERVICE
    }
    //endregion Enums
}