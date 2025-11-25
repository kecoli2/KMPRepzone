package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal


/**
 * İskonto slot konfigürasyonu
 */
data class DiscountSlotConfig(
    /** Slot numarası (1-8) */
    val slotNumber: Int,
    
    /** Slot adı */
    val name: String,
    
    /** Manuel giriş yapılabilir mi? */
    val allowManualEntry: Boolean,
    
    /** Sistem otomatik yazabilir mi? */
    val allowAutomatic: Boolean,
    
    /** Maksimum yüzde limiti */
    val maxPercentage: BigDecimal?
)

/**
 * Genel ayarlar
 */
data class GeneralSettings(
    /** Varsayılan iskonto hesaplama modu */
    val defaultCalculationMode: DiscountCalculationMode,

    /** Varsayılan conflict çözüm stratejisi */
    val defaultConflictResolution: ConflictResolution
)

/**
 * Conflict çözüm stratejisi
 */
enum class ConflictResolution {
    /** Kullanıcıya sor */
    ASK_USER,
    
    /** En yüksek değeri seç */
    HIGHEST_VALUE,
    
    /** En düşük değeri seç */
    LOWEST_VALUE,
    
    /** İlk eşleşeni seç (priority'ye göre) */
    FIRST_MATCH,
    
    /** Topla (aynı tip ise) */
    COMBINE
}
