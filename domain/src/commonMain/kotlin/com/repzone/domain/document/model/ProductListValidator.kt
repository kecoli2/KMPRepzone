package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.repository.ISettingsRepository

/**
 * Ürün miktarlarını stok ve iş kurallarına göre doğrular
 */
class ProductListValidator(private val settingsRepository: ISettingsRepository) {
    /**
     * Bir ürün birimi için miktar doğrulama işlemi
     */
    suspend fun validateQuantity(quantityText: String, unit: ProductUnit, product: Product): ValidationStatus {
        // Boş giriş
        if (quantityText.isEmpty()) {
            return ValidationStatus.Empty
        }

        // Miktarı parse et
        val quantity = quantityText.toBigDecimalOrNull()
            ?: return ValidationStatus.Error("Geçersiz miktar")

        // Negatif veya sıfır olamaz
        if (quantity <= BigDecimal.ZERO) {
            return ValidationStatus.Error("Miktar sıfırdan büyük olmalı")
        }

        // Stok doğrulama ayarlarını al
        val stockValidation = settingsRepository.getStockSettings()

        // Mevcut stok miktarını hesapla (seçili birimde)
        val availableStock = calculateAvailableStock(product, unit)

        // Doğrulama moduna göre stok kontrolü
        return when (stockValidation.orderStockBehavior) {
            StockBehavior.IGNORE -> {
                // Her zaman izin ver, doğrulama yapılmaz
                ValidationStatus.Valid
            }

            StockBehavior.WARN -> {
                if (quantity > availableStock) {
                    ValidationStatus.Warning(message = "Yetersiz stok. Mevcut: ${availableStock.toPlainString()} ${unit.unitName}", availableStock = availableStock)
                } else {
                    ValidationStatus.Valid
                }
            }

            StockBehavior.BLOCK -> {
                if (quantity > availableStock) {
                    ValidationStatus.Error("Yetersiz stok. Mevcut: ${availableStock.toPlainString()} ${unit.unitName}")
                } else {
                    ValidationStatus.Valid
                }
            }
        }
    }

    /**
     * Belirtilen birimdeki mevcut stok miktarını hesaplar
     */
    private fun calculateAvailableStock(product: Product, unit: ProductUnit): BigDecimal {
        // Temel stok her zaman temel birimde tutulur (genelde "Adet")
        val baseStock = product.stockQuantity

        // Temel birimi bul
        val baseUnit = product.units.find { it.isBaseUnit }
            ?: product.units.firstOrNull()
            ?: return BigDecimal.ZERO

        // Seçili birim temel birimse, stok olduğu gibi döndürülür
        if (unit.id == baseUnit.id) {
            return baseStock
        }

        // Stoku seçili birime dönüştür
        // Örnek: 100 Adet stok, istenen birim Koli (1 Koli = 12 Adet)
        // Sonuç: 100 / 12 = 8.33 Koli
        return baseStock.divide(unit.conversionFactor, DecimalMode.DEFAULT)
    }

    /**
     * İndirim slotlarını doğrular
     */
    fun validateDiscountSlot(slotNumber: Int, value: String, type: DiscountType, slotConfigs: List<DiscountSlotConfig>): String? {
        // Slot yapılandırmasını bul
        val config = slotConfigs.find { it.slotNumber == slotNumber }
            ?: return "Slot tanımı bulunamadı"

        // Slot aktif mi?
        if (!config.allowManualEntry) {
            return "Bu slot kullanılamaz"
        }

        // Değeri parse et
        val discountValue = value.toBigDecimalOrNull()
            ?: return "Geçersiz değer"

        // Tipe göre doğrulama
        return when (type) {
            DiscountType.PERCENTAGE -> {
                if (discountValue < BigDecimal.ZERO || discountValue > 100) {
                    "Yüzde değeri 0-100 arasında olmalı"
                } else {
                    null  // Geçerli
                }
            }

            DiscountType.FIXED_AMOUNT -> {
                if (discountValue < BigDecimal.ZERO) {
                    "Tutar negatif olamaz"
                } else {
                    null  // Geçerli
                }
            }
        }
    }
}