package com.repzone.presentation.legacy.viewmodel.document.basket

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.PaymentPlanModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class DocumentBasketUiState(
    // Document Settings (önceki ekrandan)
    val selectedPayment: PaymentPlanModel? = null,
    val paymentPlanList: List<PaymentPlanModel> = emptyList(),
    val invoiceDiscount1: BigDecimal = BigDecimal.ZERO,
    val invoiceDiscount2: BigDecimal = BigDecimal.ZERO,
    val invoiceDiscount3: BigDecimal = BigDecimal.ZERO,
    val dispatchDate: Instant? = null,

    // Sepet Satırları
    val lines: List<IDocumentLine> = emptyList(),

    // Düzenleme Dialog State
    val editingLine: IDocumentLine? = null,
    val editingLineUnits: List<ProductUnit> = emptyList(),
    val editingQuantity: String = "",
    val editingSelectedUnit: ProductUnit? = null,

    // Silme Dialog State
    val lineToDelete: IDocumentLine? = null,

    // Hesaplanan Tutarlar
    val grossTotal: BigDecimal = BigDecimal.ZERO,        // Brüt Tutar
    val discountTotal: BigDecimal = BigDecimal.ZERO,     // İskonto Tutarı
    val netTotal: BigDecimal = BigDecimal.ZERO,          // Net Tutar
    val vatTotal: BigDecimal = BigDecimal.ZERO,          // KDV Tutarı
    val grandTotal: BigDecimal = BigDecimal.ZERO,        // Toplam Tutar

    override val uiFrame: UiFrame = UiFrame()
) : HasUiFrame {
    override fun copyWithUiFrame(newUiFrame: UiFrame): DocumentBasketUiState {
        return copy(uiFrame = newUiFrame)
    }

    override fun resetUiFrame(): DocumentBasketUiState {
        return copy(uiFrame = UiFrame())
    }

    // Sepet boş mu kontrolü
    val isBasketEmpty: Boolean
        get() = lines.isEmpty()

    // Toplam ürün sayısı
    val totalItemCount: Int
        get() = lines.size

    // Toplam miktar
    val totalQuantity: BigDecimal
        get() = lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.quantity }
}