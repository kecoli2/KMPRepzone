package com.repzone.presentation.viewmodel

import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import com.repzone.core.interfaces.ILocationService
import com.repzone.core.model.GeoPoint
import com.repzone.core.model.UiFrame
import com.repzone.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class TestScreenViewModel(
    private val iLocationService: ILocationService,
    private val iFireBaseRealtimeDatabase: IFireBaseRealtimeDatabase
    ): BaseViewModel<TestScreenUiState, Nothing>(TestScreenUiState()) {
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

    fun refresh() {
        scope.launch {
            updateState { it.copy(uiFrame = it.uiFrame.copy(isLoading = true, error = null)) }
        }
    }

    suspend fun writeRealTimeData(value: String) {
        iFireBaseRealtimeDatabase.set("Test", value)
    }

    suspend fun getNewLocation(): GeoPoint? {
        return iLocationService.getCurrentLocation(true)
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}