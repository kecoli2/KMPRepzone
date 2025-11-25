package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.Product

/**
 * Stok hesaplama servisi
 */
class StockCalculator {

    //region Field
    //endregion

    //region Public Method
    /**
     * Bir ürün için tüm satırlardaki toplam kullanılan stok (base unit cinsinden)
     */
    fun calculateUsedStock(productId: String, lines: List<IDocumentLine>): BigDecimal {
        return lines
            .filter { it.productId == productId }
            .fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotal }
    }

    /**
     * Kalan stok (base unit cinsinden)
     */
    fun calculateRemainingStock(product: Product, lines: List<IDocumentLine>): BigDecimal {
        val used = calculateUsedStock(product.id, lines)
        return product.stockQuantity - used
    }

    /**
     * Belirli birimde kalan stok
     */
    fun calculateRemainingStockInUnit(product: Product, unitConversionFactor: BigDecimal, lines: List<IDocumentLine>): BigDecimal {
        val remainingBase = calculateRemainingStock(product, lines)
        return remainingBase / unitConversionFactor
    }
    //endregion

    //region Private Method
    //endregion
}
