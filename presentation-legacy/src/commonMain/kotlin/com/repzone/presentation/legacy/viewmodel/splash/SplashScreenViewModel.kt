package com.repzone.presentation.legacy.viewmodel.splash

import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.network.api.ITokenApiController
import com.repzone.presentation.legacy.ui.splash.SplashScreenUiState

class SplashScreenViewModel(private val tokenController: ITokenApiController,
                            private val sharedPreferences: IPreferencesManager): BaseViewModel<SplashScreenUiState, Nothing>(SplashScreenUiState()) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}