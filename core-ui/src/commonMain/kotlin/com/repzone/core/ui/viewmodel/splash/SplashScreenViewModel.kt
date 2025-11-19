package com.repzone.core.ui.viewmodel.splash

import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.core.ui.manager.permissions.PermissionManager
import com.repzone.core.ui.ui.splash.SplashScreenUiState
import com.repzone.core.util.PermissionStatus
import com.repzone.core.util.PermissionStatus.DeniedPermanently.isPermanentDenied
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.manager.gps.IGpsTrackingManager
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.network.api.ITokenApiController
import com.repzone.network.http.wrapper.ApiResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val tokenController: ITokenApiController,
                            private val userSession: IUserSession
): BaseViewModel<SplashScreenUiState, SplashScreenViewModel.Event>(SplashScreenUiState()) {

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
    suspend fun onEvent(event: Event){
        when(event){
            Event.NextOperationHandle -> {
                nextOperation()
            }
            else -> {

            }
        }
    }
    suspend fun checkPermissionsAndProceed(pm: PermissionManager) {

        // 1) Bluetooth
        val bluetoothStatus = pm.ensureBluetooth()

        // 2) Notifications (Bluetooth başarısızsa devam etmeye gerek yok)
        val notificationStatus = if (bluetoothStatus == PermissionStatus.Granted) {
            pm.ensureNotifications()
        } else {
            PermissionStatus.Denied(canAskAgain = true)
        }

        // 3) Location (BT + Notification başarılıysa)
        val locationStatus = if (bluetoothStatus == PermissionStatus.Granted
            && notificationStatus == PermissionStatus.Granted) {
            pm.ensureLocation()
        } else {
            PermissionStatus.Denied(canAskAgain = true)
        }

        // 4) Foreground service (bilgi amaçlı, normal izin)
        val foregroundStatus = pm.checkForegroundService()

        // 5) Bütün izinler tamam mı?
        val allGranted = listOf(bluetoothStatus, notificationStatus, locationStatus, foregroundStatus)
            .all { it == PermissionStatus.Granted }

        if (allGranted) {
            _nextOprerations.remove(SplashScreenOperation.CEHCK_PERMISSION)
            nextOperation()
            return
        }

        // 6) Kalıcı reddedilme kontrolü
        val anyPermanentDenied = listOf(bluetoothStatus, notificationStatus, locationStatus, foregroundStatus)
            .any { it.isPermanentDenied }

        if (anyPermanentDenied) {
            sendEvent(Event.PermissionDeniedPermanent)
        } else {
            sendEvent(Event.PermissionDeniedTemporary)
        }
    }
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

            SplashScreenOperation.CEHCK_PERMISSION -> {

            }
        }
    }

    private fun prepareNextOperation(){
        _nextOprerations.put(SplashScreenOperation.CEHCK_PERMISSION, null)
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
        object PermissionDeniedTemporary: Event()
        object PermissionDeniedPermanent : Event()
        object NextOperationHandle : Event()

     }
    //endregion Event

    //region Enums
    private enum class SplashScreenOperation{
        CEHCK_PERMISSION,
        CHECK_TOKEN,
        REGISTER_SMS_SERVICE,
        REGISTER_NOTIFICATION_SERVICE
    }
    //endregion Enums
}