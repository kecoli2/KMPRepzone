package com.repzone.presentation.legacy.viewmodel.splash

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.network.api.ITokenApiController
import com.repzone.presentation.legacy.ui.splash.SplashScreenUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val tokenController: ITokenApiController,
                            private val sharedPreferences: IPreferencesManager): BaseViewModel<SplashScreenUiState, SplashScreenViewModel.Event>(SplashScreenUiState()) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    init {
        checkControl()
    }
    //endregion

    //region Public Method
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun checkControl(){
        scope.launch {
            delay(3000)
            sendEvent(Event.ControllSucces)
            return@launch
        }
    }
    //endregion

    //region Event
     sealed class Event {
        object ControllSucces: Event()
        object NavigateToLogin: Event()
     }
    //endregion Event
}