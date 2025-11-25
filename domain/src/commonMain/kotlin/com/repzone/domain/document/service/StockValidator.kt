package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.DocumentType
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.StockBehavior
import com.repzone.domain.document.model.StockSettings
import com.repzone.domain.document.model.StockValidationResult

/**
 * Stok validasyon servisi
 */
class StockValidator(private val stockCalculator: StockCalculator,
                     private val settings: StockSettings
) {
    
    /**
     * Stok validasyonu yapar
     */
    fun validate(product: Product, newQuantity: BigDecimal, newUnit: ProductUnit, existingLines: List<IDocumentLine>, currentLineId: String?, documentType: DocumentType): StockValidationResult {
        
        // Mevcut satırı hariç tut (edit durumu için)
        val otherLines = existingLines.filter { it.id != currentLineId }
        
        // Yeni eklenen miktarın base unit karşılığı
        val newBaseQuantity = newQuantity * newUnit.conversionFactor
        
        // Diğer satırlardaki kullanılan stok
        val usedStock = stockCalculator.calculateUsedStock(product.id, otherLines)
        
        // Toplam talep
        val totalDemand = usedStock + newBaseQuantity
        
        // Stok yeterli mi?
        val stockAvailable = product.stockQuantity
        val isOverStock = totalDemand > stockAvailable
        
        if (!isOverStock) {
            return StockValidationResult.Valid
        }
        
        // Stok aşıldı - belge tipine göre davran
        val behavior = when (documentType) {
            DocumentType.INVOICE -> settings.invoiceStockBehavior
            DocumentType.ORDER -> settings.orderStockBehavior
            DocumentType.WAYBILL -> settings.waybillStockBehavior
        }
        
        val overAmount = totalDemand - stockAvailable
        val maxAllowedInUnit = (stockAvailable - usedStock) / newUnit.conversionFactor
        
        return when (behavior) {
            StockBehavior.BLOCK -> StockValidationResult.Blocked(
                message = "Stok yetersiz",
                availableStock = stockAvailable,
                usedStock = usedStock,
                requestedStock = totalDemand,
                overAmount = overAmount,
                maxAllowedQuantity = maxAllowedInUnit.coerceAtLeast(BigDecimal.ZERO),
                unitName = newUnit.unitName
            )
            StockBehavior.WARN -> StockValidationResult.Warning(
                message = "Stok aşılıyor, devam etmek istiyor musunuz?",
                availableStock = stockAvailable,
                usedStock = usedStock,
                requestedStock = totalDemand,
                overAmount = overAmount
            )
            StockBehavior.IGNORE -> StockValidationResult.Valid
        }
    }
}
