package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal


/**
 * Promosyon değerlendirme sonucu
 */
data class PromotionResult(
    /** Satır bazlı iskonto değerlendirmeleri */
    val lineDiscounts: Map<String, LineDiscountEvaluation>,
    
    /** Belge bazlı iskontolar */
    val documentDiscounts: List<AppliedDiscount>,
    
    /** Otomatik eklenen hediyeler */
    val automaticGifts: List<AppliedGift>,
    
    /** Kullanıcı seçimi gereken hediyeler */
    val pendingSelections: List<PendingGiftSelection>,
    
    /** Çözümlenmemiş conflict'ler */
    val conflicts: List<LineConflict>
)

/**
 * Satır iskonto değerlendirmesi
 */
data class LineDiscountEvaluation(
    val lineId: String,
    
    /** Otomatik uygulanan iskontolar (slot number -> slot) */
    val autoApplied: Map<Int, DiscountSlot>,
    
    /** Çözümlenmemiş conflict'ler */
    val conflicts: List<SlotConflict>
)

/**
 * Slot conflict'i
 */
data class SlotConflict(
    val slotNumber: Int,
    val options: List<ConflictOption>
)

/**
 * Conflict seçeneği
 */
data class ConflictOption(
    val ruleId: String,
    val ruleName: String,
    val discountType: DiscountType,
    val value: BigDecimal,
    val description: String
)

/**
 * Satır conflict'i (UI için)
 */
data class LineConflict(
    val lineId: String,
    val productName: String,
    val conflicts: List<SlotConflict>
)

/**
 * Uygulanmış iskonto
 */
data class AppliedDiscount(
    val ruleId: String,
    val ruleName: String,
    val type: DiscountType,
    val value: BigDecimal,
    val description: String
)

/**
 * Uygulanmış hediye
 */
data class AppliedGift(
    val ruleId: String,
    val productId: String,
    val quantity: Int
)

/**
 * Bekleyen hediye seçimi
 */
data class PendingGiftSelection(
    val ruleId: String,
    val ruleName: String,
    val options: List<GiftOption>,
    val selectionType: SelectionType,
    val totalQuantity: Int
)

/**
 * Hediye seçeneği
 */
data class GiftOption(
    val productId: String,
    val productName: String,
    val quantity: Int
)

/**
 * Hediye seçim tipi
 */
enum class SelectionType {
    /** Sadece birini seç */
    PICK_ONE,
    
    /** İstediğin kombinasyonda seç */
    PICK_ANY,
    
    /** Hepsini al */
    ALL
}

/**
 * Kullanıcı hediye seçimi
 */
data class GiftSelection(
    val productId: String,
    val quantity: Int
)

/**
 * Hediye değerlendirme sonucu
 */
data class GiftEvaluation(
    val automaticGifts: List<AppliedGift>,
    val pendingSelections: List<PendingGiftSelection>
)
