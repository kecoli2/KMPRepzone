package com.repzone.domain.document.service

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.repzone.domain.document.IProductStatisticsCalculator
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.BasketStatistics
import com.repzone.domain.document.model.ProductGroupStatistic
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductStatistics
import com.repzone.domain.document.model.UnitStatistic
import com.repzone.domain.model.product.ProductRowState

class ProductStatisticsCalculator : IProductStatisticsCalculator {
    //region Public Method
    override suspend fun calculate(products: Map<Int, ProductInformationModel>, rowStates: Map<Int, ProductRowState>): ProductStatistics {
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
        val groupedEntries = allEntries.groupBy { entry ->
            entry.product.groupId to entry.product.groupName
        }

        val groups = groupedEntries.entries.mapIndexed { index, (groupPair, entries) ->
            val (groupId, groupName) = groupPair
            buildGroupStatistic(
                groupId = groupId,
                groupName = groupName,
                entries = entries,
                colorIndex = index
            )
        }.sortedByDescending { it.totalAmount }

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

    override suspend fun calculateBasketStatistics(lines: List<IDocumentLine>, invoiceDiscounts: List<BigDecimal>): BasketStatistics {
        if (lines.isEmpty()) {
            return BasketStatistics.EMPTY
        }

        //region ===== Miktar Hesaplamaları =====
        // Base birimde toplam miktar
        val totalBaseQuantity = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + line.baseQuantity
        }

        // Birim bazında dagılım
        val unitBreakdown = lines
            .groupBy { it.unitName }
            .map { (unitName, unitLines) ->
                val totalQuantity = unitLines.fold(BigDecimal.ZERO) { acc, line ->
                    acc + line.quantity
                }
                val totalAmount = unitLines.fold(BigDecimal.ZERO) { acc, line ->
                    acc + line.lineTotal
                }
                UnitStatistic(
                    unitName = unitName,
                    quantity = totalQuantity,
                    amount = totalAmount
                )
            }
            .sortedByDescending { it.amount }
        //endregion ===== Miktar Hesaplamaları =====

        //region ===== Finansal Hesaplamalar =====
        // Brüt tutar (iskonto öncesi)
        val grossTotal = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + (line.unitPrice * line.quantity)
        }

        // Satır iskontoları sonrası net (fatura iskontosu öncesi)
        val netAfterLineDiscounts = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + line.lineTotal
        }

        // Fatura altı iskontolarını uygula
        var netTotal = netAfterLineDiscounts
        val hundred = BigDecimal.fromInt(100)
        val decimalMode = DecimalMode(decimalPrecision = 4, roundingMode = RoundingMode.ROUND_HALF_CEILING)

        invoiceDiscounts.forEach { discountPercent ->
            if (discountPercent.compare(BigDecimal.ZERO) > 0) {
                val discountAmount = (netTotal * discountPercent).divide(hundred, decimalMode)
                netTotal -= discountAmount
            }
        }

        // Toplam indirim tutarı
        val discountAmount = grossTotal - netTotal

        // İndirim yüzdesi
        val discountPercentage = if (grossTotal.compare(BigDecimal.ZERO) > 0) {
            (discountAmount * BigDecimal.fromInt(100)).divide(
                grossTotal,
                decimalMode = DecimalMode(
                    decimalPrecision = 2,
                    roundingMode = RoundingMode.ROUND_HALF_CEILING
                )
            )
        } else {
            BigDecimal.ZERO
        }

        // KDV tutarı
        val vatTotal = lines.fold(BigDecimal.ZERO) { acc, line ->
            acc + line.lineTotalVat
        }

        // Genel toplam
        val grandTotal = netTotal + vatTotal

        //endregion ===== Finansal Hesaplamalar =====

        //region ===== Çeşitlilik Hesaplamaları =====
        val lineCount = lines.size
        val productCount = lines.map { it.productId }.distinct().size
        val brandCount = lines.mapNotNull { it.productInfo.brandName }.distinct().size
        val groupCount = lines.map { it.productInfo.groupId }.distinct().size
        //endregion ===== Çeşitlilik Hesaplamaları =====

        //region ===== Lojistik Hesaplamaları =====
        // Toplam ağırlık (baseQuantity * weight)
        val totalWeight = lines.fold(BigDecimal.ZERO) { acc, line ->
            val unitWeight = line.productInfo.defaultUnitWeight ?: BigDecimal.ZERO
            acc + (line.baseQuantity * unitWeight)
        }
        //endregion ===== Lojistik Hesaplamaları =====

        return BasketStatistics(
            totalBaseQuantity = totalBaseQuantity,
            unitBreakdown = unitBreakdown,
            grossTotal = grossTotal,
            discountAmount = discountAmount,
            discountPercentage = discountPercentage,
            netTotal = netTotal,
            vatTotal = vatTotal,
            grandTotal = grandTotal,
            lineCount = lineCount,
            productCount = productCount,
            brandCount = brandCount,
            groupCount = groupCount,
            totalWeight = totalWeight
        )
    }

    //endregion Public Method

    //region Private Method
    private fun buildGroupStatistic(groupId: Int, groupName: String, entries: List<EntryData>, colorIndex: Int): ProductGroupStatistic {
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
            e.printStackTrace()
            null
        }
    }
    //endregion Private Method
}