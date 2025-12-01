package com.repzone.presentation.legacy.viewmodel.document.basket


import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.ui.base.BaseViewModel
import com.repzone.core.ui.base.setError
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.base.IDocumentSession
import com.repzone.domain.document.base.UpdateLineResult
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.PaymentPlanModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class DocumentBasketViewModel(iDocumentSession: IDocumentSession
) : BaseViewModel<DocumentBasketUiState, DocumentBasketViewModel.Event>(DocumentBasketUiState()) {

    //region Field
    private val iDocumentManager = iDocumentSession.current()
    //endregion Field

    //region Public Method
    suspend fun onStartDocument() {
        // Document Manager'dan verileri al
        updateState { currentState ->
            currentState.copy(
                selectedPayment = iDocumentManager.getAvailablePaymentPlan(),
                paymentPlanList = iDocumentManager.getAvailablePaymentPlanList(),
                invoiceDiscount1 = iDocumentManager.getInvoiceDiscont(1),
                invoiceDiscount2 = iDocumentManager.getInvoiceDiscont(2),
                invoiceDiscount3 = iDocumentManager.getInvoiceDiscont(3),
                dispatchDate = iDocumentManager.getDispatchDate()
            )
        }

        // Lines'ı collect et
        iDocumentManager.lines.collectLatest { lines ->
            updateState { currentState ->
                currentState.copy(
                    lines = lines
                ).calculateTotals()
            }
        }
    }

    suspend fun onEvent(event: Event) {
        when (event) {
            // ===== Ödeme Bilgileri Events =====
            is Event.SetPaymentPlan -> {
                iDocumentManager.setPaymentPlan(event.paymentPlan)
                updateState { it.copy(selectedPayment = event.paymentPlan) }
            }

            is Event.SetDispatchDate -> {
                iDocumentManager.changeDispatchDate(event.date).onError {
                    setError(it.message)
                }.onSuccess {
                    updateState { it.copy(dispatchDate = event.date) }
                }
            }

            is Event.SetInvoiceDiscount -> {
                iDocumentManager.setInvoiceDiscont(event.order, event.value).onError {
                    setError(it.message)
                }.onSuccess {
                    updateState { currentState ->
                        when (event.order) {
                            1 -> currentState.copy(invoiceDiscount1 = event.value)
                            2 -> currentState.copy(invoiceDiscount2 = event.value)
                            3 -> currentState.copy(invoiceDiscount3 = event.value)
                            else -> currentState
                        }.calculateTotals()
                    }
                }
            }

            // ===== Satır Düzenleme Events =====
            is Event.OpenEditDialog -> {
                val units = iDocumentManager.getProductUnitMap()[event.line.productId] ?: emptyList()
                val currentUnit = units.find { it.unitId == event.line.unitId }
                updateState { currentState ->
                    currentState.copy(
                        editingLine = event.line,
                        editingLineUnits = units,
                        editingQuantity = event.line.quantity.toPlainString(),
                        editingSelectedUnit = currentUnit
                    )
                }
            }

            is Event.CloseEditDialog -> {
                updateState { currentState ->
                    currentState.copy(
                        editingLine = null,
                        editingLineUnits = emptyList(),
                        editingQuantity = "",
                        editingSelectedUnit = null
                    )
                }
            }

            is Event.UpdateEditingQuantity -> {
                val result = iDocumentManager.updateLine(event.line.id, event.line.productUnit, event.line.quantity)
                when(result) {
                    is UpdateLineResult.Blocked -> {
                        setError(result.error.message)
                    }
                    is UpdateLineResult.NeedsConfirmation -> {

                    }
                    else -> {
                        updateState { it.copy(editingQuantity = event.quantity) }
                    }
                }
            }

            is Event.UpdateEditingUnit -> {
                val result = iDocumentManager.updateLine(event.line.id, event.unit, event.line.quantity)
                when(result) {
                    is UpdateLineResult.Blocked -> {
                        setError(result.error.message)
                    }
                    is UpdateLineResult.NeedsConfirmation -> {

                    }
                    else -> {
                        updateState { it.copy(editingSelectedUnit = event.unit) }
                    }
                }
            }

            is Event.ConfirmLineEdit -> {
                val editingLine = state.value.editingLine ?: return
                val newUnit = state.value.editingSelectedUnit ?: return
                val newQuantity = try {
                    BigDecimal.parseString(state.value.editingQuantity.replace(",", "."))
                } catch (e: Exception) {
                    setError("Geçersiz miktar")
                    return
                }

                if (newQuantity <= BigDecimal.ZERO) {
                    setError("Miktar sıfırdan büyük olmalıdır")
                    return
                }

                iDocumentManager.updateLine(editingLine.id, newUnit, newQuantity)
                onEvent(Event.CloseEditDialog)
            }

            // ===== Satır Inline Düzenleme Events =====
            is Event.UpdateLineQuantity -> {
                val newQuantity = try {
                    BigDecimal.parseString(event.quantity.replace(",", "."))
                } catch (e: Exception) {
                    return // Geçersiz girdi belki sonra uyarı veririz,
                }

                if (newQuantity > BigDecimal.ZERO) {
                    val line = state.value.lines.find { it.id == event.lineId } ?: return
                    val currentUnit = iDocumentManager.getProductUnitMap()[line.productId]
                        ?.find { it.unitId == line.unitId } ?: return
                    iDocumentManager.updateLine(event.lineId, currentUnit, newQuantity)
                }
            }

            is Event.CycleLineUnit -> {
                val line = state.value.lines.find { it.id == event.lineId } ?: return
                val units = iDocumentManager.getProductUnitMap()[line.productId] ?: return
                if (units.size <= 1) return

                val currentIndex = units.indexOfFirst { it.unitId == line.unitId }
                val nextIndex = (currentIndex + 1) % units.size
                val nextUnit = units[nextIndex]

                iDocumentManager.updateLine(event.lineId, nextUnit, line.quantity)
            }

            // ===== Satır Silme Events =====
            is Event.OpenDeleteDialog -> {
                updateState { it.copy(lineToDelete = event.line) }
            }

            is Event.CloseDeleteDialog -> {
                updateState { it.copy(lineToDelete = null) }
            }

            is Event.ConfirmDelete -> {
                val lineToDelete = state.value.lineToDelete ?: return
                iDocumentManager.removeLine(lineToDelete.id)
                updateState { it.copy(lineToDelete = null) }
            }

            // ===== Kaydetme =====
            is Event.SaveDocument -> {
                if (state.value.lines.isEmpty()) {
                    setError("Sepet boş, kaydetmek için ürün ekleyin")
                    return
                }

                if (state.value.selectedPayment == null) {
                    setError("Lütfen ödeme planı seçin")
                    return
                }

                try {
                    val document = iDocumentManager.toDocument()
                    // TODO: Document'ı kaydet
                    sendEvent(Event.NavigateToSuccess)
                } catch (e: Exception) {
                    setError("Belge kaydedilemedi: ${e.message}")
                }
            }

            // ===== Navigasyon =====
            is Event.NavigateBack -> {
                // Handle navigation
            }

            is Event.NavigateToSuccess -> {
                // Handle navigation
            }

            is Event.ShowError -> {
                setError(event.message)
            }
        }
    }
    //endregion Public Method

    //region Private Method
    private fun DocumentBasketUiState.calculateTotals(): DocumentBasketUiState {
        if (lines.isEmpty()) {
            return copy(
                grossTotal = BigDecimal.ZERO,
                discountTotal = BigDecimal.ZERO,
                netTotal = BigDecimal.ZERO,
                vatTotal = BigDecimal.ZERO,
                grandTotal = BigDecimal.ZERO
            )
        }

        // Brüt Tutar: Tüm satırların birim fiyat * miktar toplamı (iskonto öncesi)
        val gross = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + (line.unitPrice * line.quantity)
        }

        // Net Tutar: Tüm satırların lineTotal toplamı (satır iskontoları uygulanmış)
        var net = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + line.lineTotal
        }

        // Fatura altı iskontolarını uygula
        if (invoiceDiscount1 > BigDecimal.ZERO) {
            net -= net * invoiceDiscount1 / BigDecimal.fromInt(100)
        }
        if (invoiceDiscount2 > BigDecimal.ZERO) {
            net -= net * invoiceDiscount2 / BigDecimal.fromInt(100)
        }
        if (invoiceDiscount3 > BigDecimal.ZERO) {
            net -= net * invoiceDiscount3 / BigDecimal.fromInt(100)
        }

        // İskonto Tutarı
        val discount = gross - net

        // KDV Tutarı
        val vat = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + line.lineTotalVat
        }

        // Toplam Tutar
        val grand = net + vat

        return copy(
            grossTotal = gross,
            discountTotal = discount,
            netTotal = net,
            vatTotal = vat,
            grandTotal = grand
        )
    }
    //endregion Private Method

    //region Event
    sealed class Event {
        // Ödeme Bilgileri
        data class SetPaymentPlan(val paymentPlan: PaymentPlanModel) : Event()
        @OptIn(ExperimentalTime::class)
        data class SetDispatchDate(val date: Instant) : Event()
        data class SetInvoiceDiscount(val order: Int, val value: BigDecimal) : Event()

        // Satır Düzenleme (Dialog)
        data class OpenEditDialog(val line: IDocumentLine) : Event()
        data object CloseEditDialog : Event()
        data class UpdateEditingQuantity(val line : IDocumentLine, val quantity: String) : Event()
        data class UpdateEditingUnit(val line : IDocumentLine, val unit: ProductUnit) : Event()
        data object ConfirmLineEdit : Event()

        // Satır Inline Düzenleme (Row içinde)
        data class UpdateLineQuantity(val lineId: String, val quantity: String) : Event()
        data class CycleLineUnit(val lineId: String) : Event()

        // Satır Silme
        data class OpenDeleteDialog(val line: IDocumentLine) : Event()
        data object CloseDeleteDialog : Event()
        data object ConfirmDelete : Event()

        // Kaydetme & Navigasyon
        data object SaveDocument : Event()
        data object NavigateBack : Event()
        data object NavigateToSuccess : Event()

        // Hata
        data class ShowError(val message: String) : Event()
    }
    //endregion Event
}
