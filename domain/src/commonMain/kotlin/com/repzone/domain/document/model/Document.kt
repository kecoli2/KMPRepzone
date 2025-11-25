package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
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
    val customerId: String? = null,
    val lines: List<IDocumentLine>,
    val createdAt: Instant,
    val updatedAt: Instant
){
    val subtotal: BigDecimal  // Ara toplam (KDV hariç)
        get() = lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotal }

    val totalVat: BigDecimal  // Toplam KDV
        get() = lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotalVat }

    val grandTotal: BigDecimal  // Genel toplam (KDV dahil)
        get() = subtotal + totalVat
}
