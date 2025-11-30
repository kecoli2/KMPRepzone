package com.repzone.presentation.legacy.viewmodel.document.documentsettings

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.document.base.IDocumentSession
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
    suspend fun onEvent(event: Event){
        when(event){
            is Event.SetDispatchDate -> {
                iDocumentManager.changeDispatchDate(event.date).onError {
                }.onSuccess {

                }
            }
            is Event.SetDocumentNote -> {
                iDocumentManager.setDocumentNote(event.note)
            }
            is Event.SetInvoiceDiscount -> {
                iDocumentManager.setInvoiceDiscont(event.order, event.value).onError {
                    setError(it.message)
                }.onSuccess {

                }
            }
            is Event.ShowError -> {
                setError(event.message)
            }
            is Event.ShowSuccess -> {

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
    }
//endregion Event
}