package com.repzone.presentation.legacy.viewmodel.document.documentsettings

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.model.PaymentPlanModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class DocumentSettingsUiState(
    val selectedPayment: PaymentPlanModel? = null,
    val paymentPlanList: List<PaymentPlanModel> = emptyList(),
    val invoiceDiscont1: BigDecimal = BigDecimal.ZERO,
    val invoiceDiscont2: BigDecimal = BigDecimal.ZERO,
    val invoiceDiscont3: BigDecimal = BigDecimal.ZERO,
    val documentNote: String? = null,
    val dispatchDate: Instant? = null,
    val customerDebt: BigDecimal = BigDecimal.ZERO,
    val riskyBalance: BigDecimal = BigDecimal.ZERO,
    val creditLimit: BigDecimal = BigDecimal.ZERO,
    val showElectronicSignature: Boolean = true,
    override val uiFrame: UiFrame = UiFrame()
): HasUiFrame {
    override fun copyWithUiFrame(newUiFrame: UiFrame): DocumentSettingsUiState {
        return copy(uiFrame = newUiFrame)
    }

    override fun resetUiFrame(): DocumentSettingsUiState {
        return copy(uiFrame = UiFrame())
    }
}
