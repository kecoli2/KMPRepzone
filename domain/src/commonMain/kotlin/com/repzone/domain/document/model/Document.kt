package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.PaymentPlanModel
import com.repzone.domain.model.SyncCustomerModel
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


/**
 * Belge
 */
@OptIn(ExperimentalTime::class)
data class Document(
    val id: String,
    val type: DocumentType,
    val number: String? = null,
    val customer: SyncCustomerModel,
    val lines: List<IDocumentLine>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val paymentPlan: PaymentPlanModel,
    val invoiceDiscont1: BigDecimal = BigDecimal.ZERO,
    val invoiceDiscont2: BigDecimal = BigDecimal.ZERO,
    val invoiceDiscont3: BigDecimal = BigDecimal.ZERO,
    val documentNote: String? = null,
    val documentPrintedNo: String? = null,
    val dispatchDate: Instant? = null
){
    //region Fields
    val subtotal: BigDecimal  // Ara toplam (KDV hariç)
        get() = lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotal }

    val totalVat: BigDecimal  // Toplam KDV
        get() = lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotalVat }

    val grandTotal: BigDecimal  // Genel toplam (KDV dahil)
        get() = subtotal + totalVat
    //endregion Fields

    //region Fields

    //endregion Fields


    //region Constructor
    //endregion Constructor
}
