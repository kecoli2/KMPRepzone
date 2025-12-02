package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.domain.document.IProductStatisticsCalculator
import com.repzone.domain.document.model.ProductGroupStatistic
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductStatistics
import com.repzone.domain.document.model.UnitStatistic
import com.repzone.domain.model.product.ProductRowState

class ProductStatisticsCalculator : IProductStatisticsCalculator {

    override suspend fun calculate(products: Map<Int, ProductInformationModel>, rowStates: Map<Int, ProductRowState>): ProductStatistics {
        val entriesWithProducts = rowStates
            .filter { (_, state) -> state.hasValidQuantity() }
            .mapNotNull { (productId, state) ->
                products[productId]?.let { product ->
                    EntryData(product, state)
                }
            }

        if (entriesWithProducts.isEmpty()) {
            return ProductStatistics.EMPTY
        }

        // Group by category/group
        val groupedEntries = entriesWithProducts.groupBy { entry ->
            (entry.product.groupId) to (entry.product.groupName)
        }

        // Build group statistics
        val groups = groupedEntries.entries.mapIndexed { index, (categoryPair, entries) ->
            val (categoryId, categoryName) = categoryPair
            buildGroupStatistic(
                groupId = categoryId,
                groupName = categoryName,
                entries = entries,
                colorIndex = index
            )
        }.sortedByDescending { it.totalAmount }

        // Calculate totals
        val totalEntryCount = entriesWithProducts.size
        val totalAmount = groups.fold(BigDecimal.ZERO) { acc, group ->
            acc + group.totalAmount
        }

        return ProductStatistics(
            totalEntryCount = totalEntryCount,
            totalAmountWithoutDiscount = totalAmount,
            groups = groups
        )
    }
    private fun buildGroupStatistic(groupId: Int, groupName: String, entries: List<EntryData>, colorIndex: Int): ProductGroupStatistic {
        val unitGroups = entries.groupBy { entry ->
            entry.state.currentUnit?.unitName ?: entry.product.baseUnit.unitName
        }

        val unitBreakdown = unitGroups.map { (unitName, unitEntries) ->
            val totalQuantity = unitEntries.fold(BigDecimal.ZERO) { acc, entry ->
                acc + entry.getQuantity()
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

    private data class EntryData(val product: ProductInformationModel, val state: ProductRowState) {
        fun getQuantity(): BigDecimal {
            return state.quantityText.toBigDecimalOrNull() ?: BigDecimal.ZERO
        }

        fun calculateAmount(): BigDecimal {
            val quantity = getQuantity()
            val unitPrice = state.currentUnit?.price ?: product.baseUnit.price
            return quantity * unitPrice
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


    private fun ProductRowState.hasValidQuantity(): Boolean {
        if (quantityText.isNotBlank()) {
            try {
                val value = BigDecimal.parseString(quantityText.replace(",", "."))
                if (value.compare(BigDecimal.ZERO) > 0) return true
            } catch (e: Exception) {
            }
        }

        return unitEntries.any { (_, entry) ->
            entry.quantity.compare(BigDecimal.ZERO) > 0
        }
    }
}