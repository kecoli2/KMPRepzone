package com.repzone.domain.document.model.promotion

import com.repzone.domain.document.model.GiftOption
import com.repzone.domain.document.model.SelectionType

/**
 * Hediye kuralı
 */
data class GiftRule(
    override val id: String,
    override val name: String,
    override val priority: Int,
    override val conditions: List<Condition>,
    override val isActive: Boolean = true,

    /** Hediye tipi */
    val giftType: GiftType,

    /** Hediye seçenekleri */
    val giftOptions: List<GiftOption>,

    /** Seçim konfigürasyonu */
    val selectionConfig: SelectionConfig
) : PromotionRule

/**
 * Hediye tipi
 */
enum class GiftType {
    /** Otomatik sepete eklenir */
    AUTOMATIC,
    
    /** Kullanıcı seçer */
    SELECTABLE
}

/**
 * Seçim konfigürasyonu
 */
data class SelectionConfig(val selectionType: SelectionType, val totalQuantity: Int)
