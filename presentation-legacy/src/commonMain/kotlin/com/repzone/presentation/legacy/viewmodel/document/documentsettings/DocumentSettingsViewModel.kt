package com.repzone.presentation.legacy.viewmodel.document.documentsettings

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.model.PaymentPlanModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class DocumentSettingsViewModel(iDocumentSession: IDocumentSession):
    BaseViewModel<DocumentSettingsUiState, DocumentSettingsViewModel.Event>(DocumentSettingsUiState()) {
    //region Field
    private val iDocumentManager = iDocumentSession.current()
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    suspend fun onStartDocument(){
        updateState { currentState ->
            currentState.copy(
                selectedPayment = iDocumentManager.getAvailablePaymentPlan(),
                paymentPlanList = iDocumentManager.getAvailablePaymentPlanList(),
                invoiceDiscont1 = iDocumentManager.getInvoiceDiscont(1),
                invoiceDiscont2 = iDocumentManager.getInvoiceDiscont(2),
                invoiceDiscont3 = iDocumentManager.getInvoiceDiscont(3),
                documentNote = iDocumentManager.getDocumentNote(),
                dispatchDate = iDocumentManager.getDispatchDate(),
                customerDebt = BigDecimal.fromDouble(iDocumentManager.getCustomer().balance ?: 0.0, DecimalMode.US_CURRENCY) ,
                riskyBalance = BigDecimal.fromDouble(iDocumentManager.getCustomer().risk ?: 0.0, DecimalMode.US_CURRENCY),
                creditLimit = BigDecimal.fromDouble( 0.0, DecimalMode.US_CURRENCY),
                showElectronicSignature = true
            )
        }
    }
    fun onEvent(event: Event){
        when(event){
            is Event.SetDispatchDate -> {
                iDocumentManager.changeDispatchDate(event.date).onError {
                }.onSuccess {
                    iDocumentManager.changeDispatchDate(event.date).onSuccess {
                    updateState { currentState ->
                        currentState.copy(
                            dispatchDate = event.date
                        )
                    }
                    }.onError {
                        setError(it.message)
                    }
                }
            }
            is Event.SetDocumentNote -> {
                iDocumentManager.setDocumentNote(event.note)
                updateState { currentState ->
                    currentState.copy(
                        documentNote = event.note
                    )
                }
            }
            is Event.SetInvoiceDiscount -> {
                iDocumentManager.setInvoiceDiscont(event.order, event.value).onError {
                    setError(it.message)
                }.onSuccess {
                    updateState { currentState ->
                        currentState.copy(
                            invoiceDiscont1 = when(event.order){
                                1 -> event.value
                                else -> currentState.invoiceDiscont1
                            },
                            invoiceDiscont2 = when(event.order){
                                2 -> event.value
                                else -> currentState.invoiceDiscont2
                            },
                            invoiceDiscont3 = when(event.order){
                                3 -> event.value
                                else -> currentState.invoiceDiscont3}
                        )
                    }
                }
            }
            is Event.ShowError -> {
                setError(event.message)
            }
            is Event.ShowSuccess -> {

            }
            is Event.SetSelectedPayment -> {
                iDocumentManager.setPaymentPlan(event.payment)
                updateState { currentState ->
                    currentState.copy(
                        selectedPayment = event.payment
                    )
                }
            }
            is Event.NavigateToBack -> {

            }
            else -> {}
        }
    }
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
    //region Event
    sealed class Event {
        data class ShowError(val message: String) : Event()
        data class ShowSuccess(val message: String) : Event()
        data class SetDocumentNote(val note: String) : Event()
        data class SetInvoiceDiscount(val order: Int, val value: BigDecimal) : Event()
        @OptIn(ExperimentalTime::class)
        data class SetDispatchDate(val date: Instant) : Event()
        object NavigateToPreviewDocument : Event()
        object NavigateToBack : Event()
        data class SetSelectedPayment(val payment: PaymentPlanModel): Event()
    }
//endregion Event
}