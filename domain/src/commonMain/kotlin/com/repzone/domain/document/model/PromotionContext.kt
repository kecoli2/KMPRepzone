package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Promosyon değerlendirme konteksti
 */
data class PromotionContext(
    /** Mevcut satır (satır bazlı kurallar için) */
    val currentLine: IDocumentLine?,

    /** Tüm satırlar */
    val allLines: List<IDocumentLine>,

    /** Belge toplamı */
    val documentTotal: BigDecimal,

    /** Müşteri bilgisi */
    val customer: Customer?,

    /** Belge tipi */
    val documentType: DocumentType,

    /** Müşteri satın alma geçmişi */
    val customerPurchaseHistory: List<PurchaseRecord>? = null
)

/**
 * Satın alma kaydı
 */
@OptIn(ExperimentalTime::class)
data class PurchaseRecord(
    val productId: String,
    val productGroupId: String,
    val quantity: BigDecimal,
    val date: Instant
)
