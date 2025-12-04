package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.IProductStatisticsCalculator
import com.repzone.domain.document.model.ProductGroupStatistic
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductStatistics
import com.repzone.domain.document.model.UnitStatistic
import com.repzone.domain.model.product.ProductRowState

class ProductStatisticsCalculator : IProductStatisticsCalculator {
    override suspend fun calculate(
        products: Map<Int, ProductInformationModel>,
        rowStates: Map<Int, ProductRowState>
    ): ProductStatistics {
        val allEntries = mutableListOf<EntryData>()

        rowStates.forEach { (productId, state) ->
            val product = products[productId] ?: return@forEach

            state.unitEntries.forEach { (unitId, unitEntry) ->
                if (unitEntry.quantity.compare(BigDecimal.ZERO) > 0) {
                    val unit = state.availableUnits.find { it.unitId == unitId }
                    if (unit != null) {
                        allEntries.add(
                            EntryData(
                                product = product,
                                unitName = unit.unitName,
                                quantity = unitEntry.quantity,
                                unitPrice = unit.price
                            )
                        )
                    }
                }
            }

            val currentQuantity = state.quantityText.toBigDecimalOrNull()
            val currentUnit = state.currentUnit

            if (currentQuantity != null &&
                currentQuantity.compare(BigDecimal.ZERO) > 0 &&
                currentUnit != null) {
                val alreadyInEntries = state.unitEntries.containsKey(currentUnit.unitId)
                if (!alreadyInEntries) {
                    allEntries.add(
                        EntryData(
                            product = product,
                            unitName = currentUnit.unitName,
                            quantity = currentQuantity,
                            unitPrice = currentUnit.price
                        )
                    )
                }
            }
        }

        if (allEntries.isEmpty()) {
            return ProductStatistics.EMPTY
        }

        // Group by product group
        val groupedEntries = allEntries.groupBy { entry ->
            entry.product.groupId to entry.product.groupName
        }

        // Build group statistics
        val groups = groupedEntries.entries.mapIndexed { index, (groupPair, entries) ->
            val (groupId, groupName) = groupPair
            buildGroupStatistic(
                groupId = groupId,
                groupName = groupName,
                entries = entries,
                colorIndex = index
            )
        }.sortedByDescending { it.totalAmount }

        // Calculate totals
        val totalEntryCount = allEntries.size
        val totalAmount = groups.fold(BigDecimal.ZERO) { acc, group ->
            acc + group.totalAmount
        }

        return ProductStatistics(
            totalEntryCount = totalEntryCount,
            totalAmountWithoutDiscount = totalAmount,
            groups = groups
        )
    }

    private fun buildGroupStatistic(
        groupId: Int,
        groupName: String,
        entries: List<EntryData>,
        colorIndex: Int
    ): ProductGroupStatistic {
        val unitGroups = entries.groupBy { it.unitName }
        val unitBreakdown = unitGroups.map { (unitName, unitEntries) ->
            val totalQuantity = unitEntries.fold(BigDecimal.ZERO) { acc, entry ->
                acc + entry.quantity
            }
            val totalAmount = unitEntries.fold(BigDecimal.ZERO) { acc, entry ->
                acc + entry.calculateAmount()
            }

            UnitStatistic(
                unitName = unitName,
                quantity = totalQuantity,
                amount = totalAmount
            )
        }.sortedByDescending { it.amount }

        val groupTotalAmount = unitBreakdown.fold(BigDecimal.ZERO) { acc, unit ->
            acc + unit.amount
        }

        return ProductGroupStatistic(
            groupId = groupId,
            groupName = groupName,
            groupColor = ProductGroupStatistic.getColorForIndex(colorIndex),
            entryCount = entries.size,
            totalAmount = groupTotalAmount,
            unitBreakdown = unitBreakdown
        )
    }

    private data class EntryData(val product: ProductInformationModel, val unitName: String, val quantity: BigDecimal, val unitPrice: BigDecimal) {
        fun calculateAmount(): BigDecimal = quantity * unitPrice
    }

    private fun String.toBigDecimalOrNull(): BigDecimal? {
        return try {
            if (this.isBlank()) null
            else BigDecimal.parseString(this.replace(",", "."))
        } catch (e: Exception) {
            null
        }
    }
}