@file:OptIn(ExperimentalTime::class)

package com.repzone.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.product.ProductRowState
import com.repzone.domain.util.models.VisitActionItem
import com.repzone.presentation.legacy.ui.document.basket.DocumentBasketScreenLegacy
import com.repzone.presentation.legacy.ui.document.documentsettings.DocumentSettingsScreenLegacy
import com.repzone.presentation.legacy.ui.document.productlist.ProductListScreenLegacy
import com.repzone.presentation.legacy.ui.document.productlist.component.DiscountDialogLegacy
import com.repzone.presentation.legacy.ui.document.productlist.component.ProductRow
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

            },
            onNavigateDocumentSettings = {

            }

        )
    }
}
@Composable
fun Productrow_Preview(themeManager: ThemeManager){
    val product = generateDummyProducts()[0]
    val focusManager = LocalFocusManager.current
    val productState = ProductRowState(
        productId = product.id,
        availableUnits = product.units,
        hasDiscount = true,
        quantityText = "1.00",
    )
    AppTheme(themeManager) {
        val focusRequesters = remember { mutableMapOf<Int, FocusRequester>() }
        val focusRequester = remember {
            focusRequesters.getOrPut(0) { FocusRequester() }
        }
/*        ProductRow(
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
            isLastItem = false,
            focusRequester = focusRequester,
            onNextRequested = {

            }
        )*/
    }
}

private fun generateDummyProducts(): List<ProductInformationModel> {
    val brands = listOf("Apple", "Samsung", "Sony", "LG", "Xiaomi", "Huawei", "Asus", "Dell", "HP", "Lenovo")
    val groups = listOf("Telefon", "Tablet", "Laptop", "Aksesuar", "TV")
    val tagOptions = listOf("Elektronik", "Yeni", "İndirimli", "Popüler", "Öne Çıkan", "Sınırlı Stok", "Kampanya")

    return (1..50).map { index ->
        val brandIndex = index % brands.size
        val groupIndex = (index % 5)
        val brand = brands[brandIndex]
        val selectedTags = tagOptions.shuffled().take((0..3).random())

        ProductInformationModel(
            id = index,
            name = "${brand} Ürün $index",
            sku = "CODE-$index",
            vat = BigDecimal.fromInt(20),
            tags = selectedTags,
            brandId = brandIndex + 1,
            brandName = brand,
            groupId = groupIndex + 1,
            groupName = groups[groupIndex],
            stock = BigDecimal.fromInt((10..500).random()),
            orderStock = BigDecimal.fromInt((0..50).random()),
            vanStock = BigDecimal.fromInt((0..30).random()),
            transitStock = BigDecimal.fromInt((0..20).random()),
            photoPath = "https://picsum.photos/200/200?random=$index",
            defaultUnitMultiplier = BigDecimal.fromInt(1),
            defaultUnitName = "Adet",
            defaultUnitWeight = BigDecimal.parseString("0.5"),
            units = generateProductUnits(index),
            color = listOf("#FF5733", "#33FF57", "#3357FF", "#F3FF33", "#FF33F3").random(),
            brandPhotoPath = "https://picsum.photos/100/100?brand=$brandIndex",
            groupPhotoPath = "https://picsum.photos/100/100?group=$groupIndex",
            displayOrder = index,
            description = "${brand} marka kaliteli ürün. Ürün kodu: CODE-$index",
            pendingStock = BigDecimal.fromInt((0..10).random()),
            reservedStock = BigDecimal.fromInt((0..15).random()),
            showAvailableStock = index % 2 == 0,
            showTransitStock = index % 3 == 0,
            manufacturerId = if (index % 4 == 0) index + 100 else null
        )
    }
}

private fun generateProductUnits(productIndex: Int): List<ProductUnit> {
    val basePrice = BigDecimal.fromInt((50..1000).random())
    val vat = BigDecimal.fromInt(20)

    return listOf(
        ProductUnit(
            unitId = 1,
            unitName = "Adet",
            price = basePrice,
            priceIncludesVat = false,
            vat = vat,
            multiplier = BigDecimal.fromInt(1),
            weight = 0.5,
            minimumOrderQuantity = 1,
            maxOrderQuantity = 100,
            orderQuantityFactor = 1,
            isBaseUnit = true,
            barcode = "869${productIndex.toString().padStart(10, '0')}",
            productId = productIndex.toLong(),
            unitDisplayOrder = 1
        ),
        ProductUnit(
            unitId = 2,
            unitName = "Kutu",
            price = basePrice * BigDecimal.fromInt(12) * BigDecimal.parseString("0.95"),
            priceIncludesVat = false,
            vat = vat,
            multiplier = BigDecimal.fromInt(12),
            weight = 6.0,
            minimumOrderQuantity = 1,
            maxOrderQuantity = 50,
            orderQuantityFactor = 1,
            isBaseUnit = false,
            barcode = "869${productIndex.toString().padStart(10, '0')}1",
            productId = productIndex.toLong(),
            unitDisplayOrder = 1

        ),
        ProductUnit(
            unitId = 3,
            unitName = "Koli",
            price = basePrice * BigDecimal.fromInt(48) * BigDecimal.parseString("0.90"),
            priceIncludesVat = false,
            vat = vat,
            multiplier = BigDecimal.fromInt(48),
            weight = 24.0,
            minimumOrderQuantity = 1,
            maxOrderQuantity = 20,
            orderQuantityFactor = 1,
            isBaseUnit = false,
            barcode = "869${productIndex.toString().padStart(10, '0')}2",
            productId = productIndex.toLong(),
            unitDisplayOrder = 1
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

//region -------------------- DOCUMENT SETTING PREVIEW --------------------
@Composable
fun DocumentSettingsScreen_Sample(themeManager: ThemeManager){
    AppTheme(themeManager) {
        DocumentSettingsScreenLegacy(
            onBasketNavigate = {

            },
            onNavigateBack = {

            },
            onElectronicSignatureNavigate = {

            }
        )
    }
}
//ENDregion -------------------- DOCUMENT SETTING PREVIEW --------------------

//region -------------------- BASKET PREVIEW --------------------
@Composable
fun DocumentBasketScreen_Sample(themeManager: ThemeManager){
    AppTheme(themeManager) {
        DocumentBasketScreenLegacy(
            onNavigateBack = {

            },
            onNavigateToSuccess = {

            }
        )
    }
}
//ENDregion -------------------- BASKET PREVIEW --------------------



























