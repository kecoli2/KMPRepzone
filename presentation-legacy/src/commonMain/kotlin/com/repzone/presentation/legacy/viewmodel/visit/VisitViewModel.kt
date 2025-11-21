package com.repzone.presentation.legacy.viewmodel.visit

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.resetUiFrame
import com.repzone.core.ui.base.setError
import com.repzone.core.ui.base.withLoading
import com.repzone.domain.common.fold
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DecisionEvents
import com.repzone.domain.events.base.events.DomainEvent
import com.repzone.domain.events.base.events.PipelineEvents
import com.repzone.domain.events.base.events.ScreenEvents
import com.repzone.domain.events.base.subscribeToEvents
import com.repzone.domain.manager.visitmanager.IVisitManager
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.DecisionDialogState
import com.repzone.domain.pipline.model.pipline.DecisionOption
import com.repzone.domain.pipline.rules.util.RuleId
import com.repzone.domain.pipline.usecase.ExecuteActionUseCase
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.util.enums.ActionButtonType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class VisitViewModel(private val iModuleParameters: IMobileModuleParameterRepository,
                     private val iVisitManager: IVisitManager,
                     private val executeActionUseCase: ExecuteActionUseCase,
                     private val eventBus: IEventBus): BaseViewModel<VisitUiState, VisitViewModel.Event>(VisitUiState()) {
    //region Field
    private var customer: CustomerItemModel? = null
    private var isInitialized = false // EKLEME
    //endregion

    //region Properties
    //endregion

    //region Constructor
    init {
        listenToEvent()
    }
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
        resetUiFrame()
    }

    fun onEvent(event: Event) {
        scope.launch {
            when(event){
                is Event.OnActionButton -> {
                    when(event.actionButton){
                        ActionButtonType.VISITING_START -> {
                            executeActionUseCase(DocumentActionType.START_VISIT, customer!!)
                        }
                        ActionButtonType.VISITING_END -> {
                            executeActionUseCase(DocumentActionType.END_VISIT, customer!!)
                        }
                        ActionButtonType.MAP -> {

                        }
                        ActionButtonType.ORDER_LOG -> {

                        }
                        ActionButtonType.DRIVE -> {

                        }
                        ActionButtonType.REPORT -> {

                        }
                        ActionButtonType.NOTES -> {

                        }
                    }
                }
                is Event.OnDecisionMade -> {
                    updateState { currentState ->
                        currentState.copy(showDecisionDialog = null)
                    }

                    eventBus.publish(DecisionEvents.DecisionMade(
                        ruleId = event.ruleId,
                        selectedOption = event.selectedOptions,
                        sessionId = event.sessionId
                    ))
                }
            }
        }
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

    private fun listenToEvent(){
        scope.launch {
            eventBus.subscribeToEvents().shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(),
                replay = 0
            ).collect {
                when(it){
                    is ScreenEvents.ScreenRequired -> {
                       if(1==1){

                       }
                    }
                    is DecisionEvents.DecisionRequired -> {
                        updateState { currentState ->
                            currentState.copy(showDecisionDialog = DecisionDialogState(
                                ruleId = it.ruleId,
                                title = it.title,
                                message = it.message,
                                options = it.options,
                                sessionId = it.sessionId
                            ))
                        }
                    }
                    is PipelineEvents.PipelineCompleted -> {

                    }
                    is DomainEvent.VisitStartEvent -> {
                        updateState { currentState ->
                            val list = currentState.actionButtonList.map { it->
                                if(it.actionType  == ActionButtonType.VISITING_START){
                                    it.copy(actionType = ActionButtonType.VISITING_END)
                                }else{
                                    it
                                }
                            }
                            currentState.copy(
                                actionButtonList = list
                            )

                        }
                    }

                    is DomainEvent.VisitStoptEvent -> {
                        updateState { currentState ->
                            val list = currentState.actionButtonList.map { it->
                                if(it.actionType  == ActionButtonType.VISITING_END){
                                    it.copy(actionType = ActionButtonType.VISITING_START)
                                }else{
                                    it
                                }
                            }
                            currentState.copy(
                                actionButtonList = list
                            )

                        }
                    }
                    else -> {
                        null
                    }
                }
            }
        }
    }
    //endregion

    //region Event
    sealed class Event {
        data class OnActionButton(val actionButton: ActionButtonType) : Event()
        data class OnDecisionMade(val ruleId: RuleId, val selectedOptions: DecisionOption, val sessionId: String): Event()
    }
    //endregion Event
}