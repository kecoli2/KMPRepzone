package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal


/**
 * Stok ayarları
 */
data class StockSettings(
    val invoiceStockBehavior: StockBehavior = StockBehavior.BLOCK,
    val orderStockBehavior: StockBehavior = StockBehavior.WARN,
    val waybillStockBehavior: StockBehavior = StockBehavior.BLOCK,
    val allowNegativeStock: Boolean = false
)

/**
 * Stok davranışı
 */
enum class StockBehavior {
    /** Engelle - stok aşılamaz */
    BLOCK,
    
    /** Uyar - kullanıcı karar verir */
    WARN,
    
    /** Görmezden gel - kontrol yok */
    IGNORE
}

/**
 * Stok validasyon sonucu
 */
sealed interface StockValidationResult {
    /** Stok yeterli */
    object Valid : StockValidationResult
    
    /** Stok yetersiz ama uyarı ile devam edilebilir */
    data class Warning(
        val message: String,
        val availableStock: BigDecimal,
        val usedStock: BigDecimal,
        val requestedStock: BigDecimal,
        val overAmount: BigDecimal
    ) : StockValidationResult
    
    /** Stok yetersiz ve engellendi */
    data class Blocked(
        val message: String,
        val availableStock: BigDecimal,
        val usedStock: BigDecimal,
        val requestedStock: BigDecimal,
        val overAmount: BigDecimal,
        val maxAllowedQuantity: BigDecimal,
        val unitName: String
    ) : StockValidationResult
}

/**
 * Stok durumu
 */
data class StockStatus(
    val totalStock: BigDecimal,
    val usedStock: BigDecimal,
    val remainingStock: BigDecimal,
    val stockUnit: String,
    val lineBreakdown: List<LineStockUsage>
)

/**
 * Satır stok kullanımı
 */
data class LineStockUsage(
    val lineId: String,
    val quantity: BigDecimal,
    val unitName: String,
    val baseQuantity: BigDecimal
)

/**
 * Bekleyen satır (stok onayı için)
 */
data class PendingLine(
    val product: ProductInformationModel,
    val unit: ProductUnit,
    val quantity: BigDecimal
)

/**
 * Bekleyen güncelleme (stok onayı için)
 */
data class PendingUpdate(
    val lineId: String,
    val unit: ProductUnit,
    val quantity: BigDecimal
)
