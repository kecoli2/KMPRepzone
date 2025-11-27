@file:OptIn(ExperimentalTime::class)

package com.repzone.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.enums.ThemeType
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.product.ProductRowState
import com.repzone.domain.util.models.VisitActionItem
import com.repzone.presentation.legacy.ui.productlist.ProductListScreenLegacy
import com.repzone.presentation.legacy.ui.productlist.component.DiscountDialogLegacy
import com.repzone.presentation.legacy.ui.productlist.component.ProductRow
import com.repzone.presentation.legacy.ui.visit.VisitActionList
import kotlin.time.ExperimentalTime

//region -------------------- VISIT SAMPLE PREVIEW --------------------

@Composable
fun ActivityVisit_Sample(themeManager: ThemeManager){
    val lists =  listOf(
        VisitActionItem(
            name = "Sipariş Oluştur",
            description = "Yeni sipariş kaydı oluşturun",
            documentType = DocumentActionType.ORDER,
            isMandatory = true,
            hasDone = false,
            displayOrder = 1,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Acil Sipariş",
            description = "Acil sipariş kaydı",
            documentType = DocumentActionType.ORDER,
            isMandatory = false,
            hasDone = true,
            displayOrder = 2,
            interval = TaskRepeatInterval.WEEK
        ),
        VisitActionItem(
            name = "Fatura Kes",
            description = "Müşteriye fatura düzenleyin",
            documentType = DocumentActionType.INVOICE,
            hasDone = false,
            displayOrder = 3,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Proforma Fatura",
            description = "Proforma fatura düzenleyin",
            documentType = DocumentActionType.INVOICE,
            hasDone = true,
            displayOrder = 4,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Sevkiyat Hazırla",
            description = "Sevkiyat belgesi hazırlayın",
            documentType = DocumentActionType.WAYBILL,
            hasDone = false,
            isFulfillment = true,
            displayOrder = 5,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Müşteri Formu",
            description = "Müşteri bilgi formunu doldurun",
            documentType = DocumentActionType.FORM,
            interval = TaskRepeatInterval.ONE_TIME,
            isMandatory = true,
            hasDone = false,
            displayOrder = 6
        ),
        VisitActionItem(
            name = "Memnuniyet Anketi",
            description = "Müşteri memnuniyet anketini doldurun",
            documentType = DocumentActionType.FORM,
            hasDone = false,
            displayOrder = 7,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Tahsilat Yap",
            description = "Müşteriden tahsilat yapın",
            documentType = DocumentActionType.COLLECTION,
            hasDone = false,
            displayOrder = 8,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Depo Fişi",
            description = "Depo giriş fişi oluşturun",
            documentType = DocumentActionType.WAREHOUSERECEIPT,
            hasDone = true,
            displayOrder = 9,
            interval = TaskRepeatInterval.NONE
        ),
        VisitActionItem(
            name = "Diğer İşlem",
            description = "Diğer işlemleri gerçekleştirin",
            documentType = DocumentActionType.OTHER,
            hasDone = false,
            displayOrder = 10,
            interval = TaskRepeatInterval.ATVISITSTART
        )
    )
    AppTheme(themeManager) {
        VisitActionList(
            items = lists,
            onItemClick = {

            },
            themeManager = themeManager,
            modifier = Modifier
        )
    }
}

//endregion -------------------- VISIT SAMPLE PREVIEW --------------------

//region -------------------- PRODUCTLIST SAMPLE PREVIEW --------------------
@Composable
fun ProductListScreen_Sample(themeManager: ThemeManager){
    AppTheme(themeManager) {
        ProductListScreenLegacy(
            onDissmiss = {

            }
        )
    }
}
@Composable
fun Productrow_Preview(themeManager: ThemeManager){
    val product = generateDummyProducts()[0]
    val productState = ProductRowState(
        productId = product.id,
        availableUnits = product.units,
        hasDiscount = true,
        quantityText = "1.00",
    )
    AppTheme(themeManager) {
        ProductRow(
            product = product,
            state = productState,
            hasDiscountPermission = true,
            onUnitCycle = {

            },
            onQuantityChanged = {

            },
            onDiscountClick = {

            },
            modifier = Modifier,
            backgroundColor = themeManager.getCurrentColorScheme().colorPalet.neutral95
        )
    }
}

private fun generateDummyProducts(): List<Product> {
    val brands = listOf("Apple", "Samsung DENEME MARKALARI BURADA nasıl ama", "Sony", "LG", "Xiaomi", "Huawei", "Asus", "Dell", "HP", "Lenovo")
    val tagOptions = listOf("Elektronik", "Yeni", "İndirimli", "Popüler", "Öne Çıkan", "Sınırlı Stok", "Kampanya")

    return (1..50).map { index ->
        val productId = "PRD-${index.toString().padStart(4, '0')}"
        val brand = brands[index % brands.size]
        val selectedTags = tagOptions.shuffled().take((0..3).random())

        Product(
            id = productId,
            name = "${brand} Ürün $index",
            code = "CODE-$index",
            groupId = "GRP-${(index % 5) + 1}",
            tags = selectedTags,
            stockQuantity = BigDecimal.fromInt((10..500).random()),
            stockUnitId = "UNIT-ADET",
            units = generateProductUnits(productId, index),
            vatRate = BigDecimal.fromInt(20),
            brand = brand
        )
    }
}

private fun generateProductUnits(productId: String, index: Int): List<ProductUnit> {
    val basePrice = BigDecimal.fromInt((50..1000).random())

    return listOf(
        ProductUnit(
            id = "${productId}-UNIT-1",
            productId = productId,
            unitId = "UNIT-ADET",
            unitName = "Adet",
            conversionFactor = BigDecimal.fromInt(1),
            isBaseUnit = true,
            price = basePrice,
            barcode = "869${index.toString().padStart(10, '0')}",
            priceIncludesVat = false
        ),
        ProductUnit(
            id = "${productId}-UNIT-2",
            productId = productId,
            unitId = "UNIT-KUTU",
            unitName = "Kutu",
            conversionFactor = BigDecimal.fromInt(12),
            isBaseUnit = false,
            price = basePrice * BigDecimal.fromInt(12) * BigDecimal.parseString("0.95"), // %5 kutu indirimi
            barcode = "869${index.toString().padStart(10, '0')}1",
            priceIncludesVat = false
        ),
        ProductUnit(
            id = "${productId}-UNIT-3",
            productId = productId,
            unitId = "UNIT-KOLI",
            unitName = "Koli",
            conversionFactor = BigDecimal.fromInt(48),
            isBaseUnit = false,
            price = basePrice * BigDecimal.fromInt(48) * BigDecimal.parseString("0.90"), // %10 koli indirimi
            barcode = "869${index.toString().padStart(10, '0')}2",
            priceIncludesVat = false
        )
    )
}
//endregion -------------------- PRODUCTLIST SAMPLE PREVIEW --------------------

//region -------------------- SERBEST ISKONTO SAMPLE PREVIEW --------------------
@Composable
fun DiscountScreen_Sample(themeManager: ThemeManager){
    val product = generateDummyProducts()[0]
    val productState = ProductRowState(
        productId = product.id,
        availableUnits = product.units,
        hasDiscount = true,
        quantityText = "1.00",
    )
    AppTheme(themeManager) {
        DiscountDialogLegacy(
            product = product,
            unit = product.units.first(),
            quantity = BigDecimal.fromInt(100),
            existingDiscounts = listOf(
                DiscountSlotEntry(
                    slotNumber = 1,
                    value = "15",
                    type = DiscountType.PERCENTAGE,
                    isEnabled = true,
                    validationError = null
                ),
                DiscountSlotEntry(
                    slotNumber = 5,
                    value = "245.65",
                    type = DiscountType.PERCENTAGE,
                    isEnabled = true,
                    validationError = null
                )
            ),
            slotConfigs = listOf(
                DiscountSlotConfig(
                    slotNumber = 1,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 2,
                    name = "İndirim Iskontosu 2",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(40)
                ),
                DiscountSlotConfig(
                    slotNumber = 3,
                    name = "İndirim Iskontosu 3 Deneme",
                    allowManualEntry = false,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 4,
                    name = "İndirim Iskontosu 4",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(14)
                ),
                DiscountSlotConfig(
                    slotNumber = 5,
                    name = "İndirim Iskontosu Tutar",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = null
                ),
                DiscountSlotConfig(
                    slotNumber = 6,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 7,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                ),
                DiscountSlotConfig(
                    slotNumber = 8,
                    name = "İndirim Iskontosu",
                    allowManualEntry = true,
                    allowAutomatic = true,
                    maxPercentage = BigDecimal.fromInt(100)
                )
            ),
            onApply = {

            },
            onDismiss = {

            },
            themeManager = themeManager
        )
    }
}

//endregion -------------------- SERBEST ISKONTO SAMPLE PREVIEW --------------------





























