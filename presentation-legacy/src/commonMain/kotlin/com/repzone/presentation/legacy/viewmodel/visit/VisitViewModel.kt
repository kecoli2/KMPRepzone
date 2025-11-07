package com.repzone.presentation.legacy.viewmodel.visit

import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.core.ui.base.withLoading
import com.repzone.domain.common.fold
import com.repzone.domain.manager.visitmanager.IVisitManager
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IVisitRepository

class VisitViewModel(private val iModuleParameters: IMobileModuleParameterRepository,
                    private val iVisitManager: IVisitManager): BaseViewModel<VisitUiState, VisitViewModel.Event>(VisitUiState()) {
    //region Field
    private var customer: CustomerItemModel? = null
    private var isInitialized = false // EKLEME
    //endregion

    //region Properties
    //endregion

    //region Constructor
    suspend fun initiliaze(customers: CustomerItemModel){
        if (isInitialized && customer?.customerId == customers.customerId) {
            return
        }
        customer = customers
        iVisitManager.initiliaze(customers).fold(
            onSuccess = {
                prepareUiParameters()
                prepareActions()
                isInitialized = true
            },
            onError = {
                setError(it)
            }
        )
    }
    //endregion

    //region Public Method
    suspend fun prepareActions(){
        try {
            updateState { currentState ->
                currentState.copy(uiFrame = currentState.uiFrame.withLoading())
            }
            customer?.let {
                iVisitManager.prepareVisitMenu().fold(
                    onSuccess = { (actionMenulist, actionButtonList) ->
                        updateState { currentState ->
                            currentState.copy(menuListState = VisitUiState.ActionMenuListState.Success
                                , actionMenuList = actionMenulist
                                , actionButtonList = actionButtonList)
                        }
                    },
                    onError = {
                        setError(it)
                    }
                )
            }
        }catch (ex:Exception){
            setError(ex.message)
        }finally {
            updateState { currentState ->
                currentState.copy(uiFrame = currentState.uiFrame.copy(
                    false
                ))
            }
        }
    }

    override fun onDispose() {
        super.onDispose()
        isInitialized = false
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun prepareUiParameters(){
        if(!iModuleParameters.getModule().order){
            updateState { currentState ->
                currentState.copy(visibleBalanceText = false)
            }
        }
    }
    //endregion

    //region Event
    sealed class Event {
        data class OnClickCustomer(val customer: CustomerItemModel) : Event()

    }
    //endregion Event
}