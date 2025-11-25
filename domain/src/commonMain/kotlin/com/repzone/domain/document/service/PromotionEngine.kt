package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.IPromotionEngine
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.AppliedDiscount
import com.repzone.domain.document.model.AppliedGift
import com.repzone.domain.document.model.ConflictOption
import com.repzone.domain.document.model.ConflictResolution
import com.repzone.domain.document.model.DiscountSlot
import com.repzone.domain.document.model.GeneralSettings
import com.repzone.domain.document.model.GiftEvaluation
import com.repzone.domain.document.model.LineConflict
import com.repzone.domain.document.model.LineDiscountEvaluation
import com.repzone.domain.document.model.PendingGiftSelection
import com.repzone.domain.document.model.PromotionContext
import com.repzone.domain.document.model.PromotionResult
import com.repzone.domain.document.model.SlotConflict
import com.repzone.domain.document.model.promotion.DiscountRule
import com.repzone.domain.document.model.promotion.DiscountScope
import com.repzone.domain.document.model.promotion.GiftType
import com.repzone.domain.document.model.promotion.PromotionRule
import com.repzone.domain.repository.IPromotionRuleRepository

/**
 * Promosyon engine implementation
 */
class PromotionEngine(private val ruleRepository: IPromotionRuleRepository,
                      private val lineCalculator: LineDiscountCalculator,
                      private val settings: GeneralSettings) : IPromotionEngine {

    //region Field
    //endregion

    //region Public Method
    override suspend fun evaluate(context: PromotionContext): PromotionResult {
        val rules = ruleRepository.getActiveRules()

        // Satır iskontolarını değerlendir
        val lineDiscounts = mutableMapOf<String, LineDiscountEvaluation>()
        val conflicts = mutableListOf<LineConflict>()

        context.allLines.forEach { line ->
            val evaluation = evaluateLineDiscountsForLine(line, context, rules)
            lineDiscounts[line.id] = evaluation

            if (evaluation.conflicts.isNotEmpty()) {
                conflicts.add(
                    LineConflict(
                        lineId = line.id,
                        productName = line.productName,
                        conflicts = evaluation.conflicts
                    )
                )
            }
        }

        // Belge iskontolarını değerlendir
        val documentDiscounts = evaluateDocumentDiscounts(context)

        // Hediyeleri değerlendir
        val giftEvaluation = evaluateGifts(context)

        return PromotionResult(
            lineDiscounts = lineDiscounts,
            documentDiscounts = documentDiscounts,
            automaticGifts = giftEvaluation.automaticGifts,
            pendingSelections = giftEvaluation.pendingSelections,
            conflicts = conflicts
        )
    }

    override suspend fun evaluateLineDiscounts(lines: List<IDocumentLine>, context: PromotionContext): List<LineDiscountEvaluation> {
        val rules = ruleRepository.getActiveRules()
        return lines.map { line ->
            evaluateLineDiscountsForLine(line, context, rules)
        }
    }
    override suspend fun evaluateDocumentDiscounts(context: PromotionContext): List<AppliedDiscount> {
        val rules = ruleRepository.getActiveDiscountRules()
            .filter { it.scope == DiscountScope.DOCUMENT }
            .filter { it.isApplicable(context) }
            .sortedBy { it.priority }

        return rules.map { rule ->
            AppliedDiscount(
                ruleId = rule.id,
                ruleName = rule.name,
                type = rule.discountType,
                value = rule.value,
                description = rule.name
            )
        }
    }
    override suspend fun evaluateGifts(context: PromotionContext): GiftEvaluation {
        val giftRules = ruleRepository.getActiveGiftRules()
            .filter { it.isApplicable(context) }
            .sortedBy { it.priority }

        val automaticGifts = mutableListOf<AppliedGift>()
        val pendingSelections = mutableListOf<PendingGiftSelection>()

        giftRules.forEach { rule ->
            when (rule.giftType) {
                GiftType.AUTOMATIC -> {
                    rule.giftOptions.forEach { option ->
                        automaticGifts.add(
                            AppliedGift(
                                ruleId = rule.id,
                                productId = option.productId,
                                quantity = option.quantity
                            )
                        )
                    }
                }
                GiftType.SELECTABLE -> {
                    pendingSelections.add(
                        PendingGiftSelection(
                            ruleId = rule.id,
                            ruleName = rule.name,
                            options = rule.giftOptions,
                            selectionType = rule.selectionConfig.selectionType,
                            totalQuantity = rule.selectionConfig.totalQuantity
                        )
                    )
                }
            }
        }

        return GiftEvaluation(
            automaticGifts = automaticGifts,
            pendingSelections = pendingSelections
        )
    }
    //endregion

    //region Private Method
    private fun evaluateLineDiscountsForLine(line: IDocumentLine, context: PromotionContext, allRules: List<PromotionRule>): LineDiscountEvaluation {

        val discountRules = allRules
            .filterIsInstance<DiscountRule>()
            .filter { it.scope == DiscountScope.LINE }
            .sortedBy { it.priority }

        val lineContext = context.copy(currentLine = line)

        val applicableRules = discountRules.filter {
            it.isApplicable(lineContext)
        }

        // Slot bazında grupla
        val rulesBySlot = applicableRules.groupBy { it.targetSlot }

        val autoApplied = mutableMapOf<Int, DiscountSlot>()
        val conflicts = mutableListOf<SlotConflict>()

        rulesBySlot.forEach { (slot, rules) ->
            when {
                rules.size == 1 -> {
                    // Tek kural - direkt uygula
                    autoApplied[slot] = rules.first().toDiscountSlot()
                }
                rules.size > 1 -> {
                    // Birden fazla kural - conflict resolution
                    val resolution = resolveConflict(rules, settings.defaultConflictResolution)

                    when (resolution) {
                        is ConflictResult.AutoResolved -> {
                            autoApplied[slot] = resolution.slot
                        }
                        is ConflictResult.NeedsUserInput -> {
                            conflicts.add(
                                SlotConflict(
                                    slotNumber = slot,
                                    options = resolution.options
                                )
                            )
                        }
                    }
                }
            }
        }

        return LineDiscountEvaluation(
            lineId = line.id,
            autoApplied = autoApplied,
            conflicts = conflicts
        )
    }

    /**
     * Conflict'i çözer
     */
    private fun resolveConflict(rules: List<DiscountRule>, defaultResolution: ConflictResolution): ConflictResult {

        // Herhangi biri ASK_USER ise kullanıcıya sor
        val resolution = rules
            .map { it.conflictResolution }
            .firstOrNull { it == ConflictResolution.ASK_USER }
            ?: defaultResolution

        return when (resolution) {
            ConflictResolution.ASK_USER -> {
                ConflictResult.NeedsUserInput(
                    options = rules.map { it.toConflictOption() }
                )
            }
            ConflictResolution.HIGHEST_VALUE -> {
                val winner = rules.maxByOrNull { it.effectiveValue() }!!
                ConflictResult.AutoResolved(winner.toDiscountSlot())
            }
            ConflictResolution.LOWEST_VALUE -> {
                val winner = rules.minByOrNull { it.effectiveValue() }!!
                ConflictResult.AutoResolved(winner.toDiscountSlot())
            }
            ConflictResolution.FIRST_MATCH -> {
                ConflictResult.AutoResolved(rules.first().toDiscountSlot())
            }
            ConflictResolution.COMBINE -> {
                val combined = rules.fold(BigDecimal.ZERO) { acc, rule ->
                    acc + rule.value
                }
                ConflictResult.AutoResolved(
                    DiscountSlot.Applied(
                        ruleId = "combined",
                        type = rules.first().discountType,
                        value = combined,
                        description = "Kombine İskonto",
                        calculationMode = null,
                        isManual = false
                    )
                )
            }
        }
    }

    /**
     * DiscountRule'u DiscountSlot'a çevirir
     */
    private fun DiscountRule.toDiscountSlot(): DiscountSlot.Applied {
        return DiscountSlot.Applied(
            ruleId = id,
            type = discountType,
            value = value,
            description = name,
            calculationMode = calculationMode,
            isManual = false
        )
    }

    /**
     * DiscountRule'u ConflictOption'a çevirir
     */
    private fun DiscountRule.toConflictOption(): ConflictOption {
        return ConflictOption(
            ruleId = id,
            ruleName = name,
            discountType = discountType,
            value = value,
            description = name
        )
    }
    //endregion
    

}

/**
 * Conflict çözüm sonucu
 */
private sealed interface ConflictResult {
    data class AutoResolved(val slot: DiscountSlot.Applied) : ConflictResult
    data class NeedsUserInput(val options: List<ConflictOption>) : ConflictResult
}
