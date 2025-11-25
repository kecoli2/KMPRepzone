package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.common.ErrorCode
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.DiscountCalculationMode
import com.repzone.domain.document.model.DiscountCalculationResult
import com.repzone.domain.document.model.DiscountSlot
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.DiscountStep
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.GeneralSettings
import com.repzone.domain.document.model.promotion.DiscountRule
import com.repzone.domain.common.Result
import com.repzone.domain.common.businessRuleException

/**
 * Satır iskonto hesaplama servisi
 */
class LineDiscountCalculator(private val slotConfigs: List<DiscountSlotConfig>,
                             private val settings: GeneralSettings
) {
    
    /**
     * Net fiyat hesaplama
     */
    fun calculateNetPrice(line: IDocumentLine): DiscountCalculationResult {
        val basePrice = line.unitPrice
        val steps = mutableListOf<DiscountStep>()
        
        var runningNet = basePrice
        var totalDiscountAmount = BigDecimal.ZERO
        
        line.discountSlots.forEachIndexed { index, slot ->
            if (slot !is DiscountSlot.Applied) return@forEachIndexed
            
            // Hesaplama modunu belirle
            val mode = slot.calculationMode ?: settings.defaultCalculationMode
            
            // Referans fiyat (hangi tutar üzerinden hesaplanacak)
            val referencePrice = when (mode) {
                DiscountCalculationMode.CASCADING -> runningNet
                DiscountCalculationMode.BASE -> basePrice
            }
            
            // İskonto tutarını hesapla
            val discountAmount = when (slot.type) {
                DiscountType.PERCENTAGE -> {
                    referencePrice * slot.value / 100
                }
                DiscountType.FIXED_AMOUNT -> {
                    slot.value
                }
            }
            
            // Net'ten düş
            runningNet -= discountAmount
            totalDiscountAmount += discountAmount
            
            // Adımı kaydet
            steps.add(
                DiscountStep(
                    slotNumber = index + 1,
                    description = slot.description,
                    referencePrice = referencePrice,
                    discountValue = slot.value,
                    discountType = slot.type,
                    discountAmount = discountAmount,
                    netAfter = runningNet,
                    mode = mode
                )
            )
        }
        
        return DiscountCalculationResult(
            basePrice = basePrice,
            steps = steps,
            totalDiscount = totalDiscountAmount,
            netPrice = runningNet.coerceAtLeast(BigDecimal.ZERO)
        )
    }
    
    /**
     * Kurala göre iskonto uygular
     */
    fun applyDiscount(line: IDocumentLine, rule: DiscountRule): IDocumentLine {
        val targetSlot = rule.targetSlot
        val config = slotConfigs.find { it.slotNumber == targetSlot }
            ?: throw IllegalArgumentException("Invalid slot: $targetSlot")
        
        // Otomatik yazılabilir mi?
        if (!config.allowAutomatic) {
            return line // Değişiklik yapma
        }
        
        // Max limit kontrolü
        val finalValue = if (rule.discountType == DiscountType.PERCENTAGE) {
            config.maxPercentage?.let { max ->
                rule.value.coerceAtMost(max)
            } ?: rule.value
        } else {
            rule.value
        }
        
        val slot = DiscountSlot.Applied(
            ruleId = rule.id,
            type = rule.discountType,
            value = finalValue,
            description = rule.name,
            calculationMode = rule.calculationMode,
            isManual = false
        )
        
        return line.withSlot(targetSlot, slot)
    }
    
    /**
     * Manuel iskonto uygular
     */
    fun applyManualDiscount(line: IDocumentLine, slotNumber: Int, type: DiscountType, value: BigDecimal): Result<IDocumentLine> {
        val config = slotConfigs.find { it.slotNumber == slotNumber }
            ?: return Result.Error(businessRuleException(ErrorCode.ERROR_INVALID_PROMOTION_SLOT))
        
        // Manuel girişe açık mı?
        if (!config.allowManualEntry) {
            return Result.Error(businessRuleException(ErrorCode.ERROR_INVALID_PROMOTION_SLOT_MANUEL,mapOf("slotNumber" to slotNumber.toString())))
            /*IllegalStateException("Slot $slotNumber does not allow manual entry")*/
        }
        
        // Max limit kontrolü
        if (type == DiscountType.PERCENTAGE && config.maxPercentage != null) {
            if (value > config.maxPercentage) {
                return Result.Error(businessRuleException(ErrorCode.ERROR_INVALID_PROMOTION_SLOT_MANUEL_MAXIMUM,mapOf("slotNumber" to config.maxPercentage)))
            }
        }
        
        val slot = DiscountSlot.Applied(
            ruleId = null,
            type = type,
            value = value,
            description = "Manuel İskonto",
            calculationMode = null,
            isManual = true
        )
        
        return Result.Success(line.withSlot(slotNumber, slot))
    }
}
