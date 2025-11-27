package com.repzone.domain.document.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.repository.ISettingsRepository

/**
 * Ürün miktarlarını stok ve iş kurallarına göre doğrular
 */
class ProductListValidator(private val settingsRepository: ISettingsRepository) {
    /**
     * Bir ürün birimi için miktar doğrulama işlemi
     */
    suspend fun validateQuantity(quantityText: String, unit: ProductUnit, product: Product, reservedBaseQuantity: BigDecimal = BigDecimal.ZERO): ValidationStatus {
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

        // Reserved miktarı seçili birime çevir
        val reservedInCurrentUnit = if (reservedBaseQuantity > BigDecimal.ZERO) {
            reservedBaseQuantity.divide(
                unit.conversionFactor,
                DecimalMode(decimalPrecision = 10, roundingMode = RoundingMode.ROUND_HALF_CEILING)
            )
        } else {
            BigDecimal.ZERO
        }

        // Gerçek kullanılabilir stok = mevcut stok - önceden girilen
        val effectiveAvailableStock = (availableStock - reservedInCurrentUnit).let {
            if (it < BigDecimal.ZERO) BigDecimal.ZERO else it
        }

        // Toplam istenen miktar (mesaj için)
        val totalRequested = reservedInCurrentUnit + quantity

        // Doğrulama moduna göre stok kontrolü
        return when (stockValidation.orderStockBehavior) {
            StockBehavior.IGNORE -> {
                ValidationStatus.Valid
            }

            StockBehavior.WARN -> {
                if (quantity > effectiveAvailableStock) {
                    ValidationStatus.Warning(
                        message = "Yetersiz stok. Mevcut: ${availableStock.toPlainString()} ${unit.unitName}, Toplam istenen: ${totalRequested.toPlainString()} ${unit.unitName}",
                        availableStock = effectiveAvailableStock
                    )
                } else {
                    ValidationStatus.Valid
                }
            }

            StockBehavior.BLOCK -> {
                if (quantity > effectiveAvailableStock) {
                    ValidationStatus.Error(
                        "Yetersiz stok. Mevcut: ${availableStock.toPlainString()} ${unit.unitName}, Toplam istenen: ${totalRequested.toPlainString()} ${unit.unitName}"
                    )
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
        val baseStock = product.stockQuantity

        // Temel birimi bul
        val baseUnit = product.units.find { it.isBaseUnit }
            ?: product.units.firstOrNull()
            ?: return BigDecimal.ZERO

        if (unit.id == baseUnit.id) {
            return baseStock
        }

        return baseStock.divide(
            unit.conversionFactor,
            DecimalMode(decimalPrecision = 10, roundingMode = RoundingMode.ROUND_HALF_CEILING)
        )
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
                if (discountValue < BigDecimal.ZERO || discountValue > BigDecimal.fromInt(100)) {
                    "Yüzde değeri 0-100 arasında olmalı"
                } else {
                    null
                }
            }

            DiscountType.FIXED_AMOUNT -> {
                if (discountValue < BigDecimal.ZERO) {
                    "Tutar negatif olamaz"
                } else {
                    null
                }
            }
        }
    }
}