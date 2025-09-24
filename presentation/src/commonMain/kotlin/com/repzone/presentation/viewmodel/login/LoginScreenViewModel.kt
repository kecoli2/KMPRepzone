package com.repzone.presentation.viewmodel.login

import com.repzone.core.constant.ITokenApiControllerConstant
import com.repzone.core.interfaces.IPreferencesManager
import com.repzone.network.api.ITokenApiController
import com.repzone.network.models.request.LoginRequest
import com.repzone.presentation.base.BaseViewModel

class LoginScreenViewModel(private val tokenApiController: ITokenApiController, private val isharedPreferences: IPreferencesManager): BaseViewModel<LoginScreenUiState, Nothing>(LoginScreenUiState()) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun onStart() {
        super.onStart()
    }

    override fun onDispose() {
        super.onDispose()
    }

    override fun onStop() {
        super.onStop()
    }


    suspend fun login(username: String, password: String) {
        val response = tokenApiController.pushToken(LoginRequest(username, password))
        if(response.isSuccess){
            isharedPreferences.setToken(response.getOrNull()?.token)
        }
        else{

        }

    }



    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}