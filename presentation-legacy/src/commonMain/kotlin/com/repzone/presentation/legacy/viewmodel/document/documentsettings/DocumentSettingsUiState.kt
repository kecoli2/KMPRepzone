package com.repzone.presentation.legacy.viewmodel.document.documentsettings

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.model.PaymentPlanModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class DocumentSettingsUiState(
    val selectedPayment: PaymentPlanModel? = null,
    val invoiceDiscont1: BigDecimal,
    val invoiceDiscont2: BigDecimal,
    val invoiceDiscont3: BigDecimal,
    val documentNote: String? = null,
    val dispatchDate: Instant? = null,
    val customerDebt: BigDecimal,
    val riskyBalance: BigDecimal = BigDecimal.ZERO,
    val creditLimit: BigDecimal = BigDecimal.ZERO,
    val showElectronicSignature: Boolean = true
)
