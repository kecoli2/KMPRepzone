package com.repzone.presentation.legacy.viewmodel.actionmenulist

import com.repzone.core.ui.base.BaseViewModel
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.IActionMenuRepository

class VisitViewModel(private val iActionMenuRepository: IActionMenuRepository): BaseViewModel<VisitUiState, VisitViewModel.Event>(VisitUiState()) {
    //region Field
    private var customer: CustomerItemModel? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    suspend fun prepareActions(customer: CustomerItemModel){
        this.customer = customer
        getActionMenuList()
        getActionButtonList()
    }

    override fun onDispose() {
        super.onDispose()
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    suspend fun getActionMenuList(){
        try {
            updateState { currentState ->
                currentState.copy(menuListState = VisitUiState.ActionMenuListState.Loading)
            }
            val actionMenuList = iActionMenuRepository.getActionMenuList(customer!!)
            updateState { currentState ->
                currentState.copy(menuListState = VisitUiState.ActionMenuListState.Success
                , actionMenuList = actionMenuList)
            }
        }catch (ex: Exception){
            updateState { currentState ->
                currentState.copy(menuListState = VisitUiState.ActionMenuListState.Error(ex.message ?: "Unknown error"))
            }
        }
    }
    suspend fun getActionButtonList(){
        try {
            updateState { currentState ->
                currentState.copy(buttonListState = VisitUiState.ActionMenuListState.Loading)
            }
            val actionMenuList = iActionMenuRepository.getActionButtonList(customer!!)
            updateState { currentState ->
                currentState.copy(buttonListState = VisitUiState.ActionMenuListState.Success
                    , actionButtonList = actionMenuList)
            }
        }catch (ex: Exception){
            updateState { currentState ->
                currentState.copy(buttonListState = VisitUiState.ActionMenuListState.Error(ex.message ?: "Unknown error"))
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