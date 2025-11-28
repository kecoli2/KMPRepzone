package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.repzone.core.model.StringResource
import com.repzone.core.ui.component.textfield.BorderType
import com.repzone.core.ui.component.textfield.DecimalTextField
import com.repzone.core.ui.component.textfield.TextAlignment
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarAction
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.NumberFormatInfo
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit

/**
 * Full-screen dialog for entering discounts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountDialogLegacy(
    product: ProductInformationModel,
    unit: ProductUnit,
    quantity: BigDecimal,
    existingDiscounts: List<DiscountSlotEntry>,
    slotConfigs: List<DiscountSlotConfig>,
    onApply: (List<DiscountSlotEntry>) -> Unit,
    onDismiss: () -> Unit,
    themeManager: ThemeManager
) {
    val focusManager = LocalFocusManager.current
    // Hesaplamalar
    val basePrice = quantity * unit.price
    val basePriceDouble = basePrice.doubleValue(false)

    var discountSlots by remember {
        mutableStateOf(
            slotConfigs.map { config ->
                existingDiscounts.find { it.slotNumber == config.slotNumber }
                    ?: DiscountSlotEntry(
                        slotNumber = config.slotNumber,
                        isEnabled = config.allowManualEntry,
                        maximumValue = config.maxPercentage
                    )
            }
        )
    }

    val totalDiscount = calculateTotalDiscount(
        discountSlots = discountSlots.filter { it.value.isNotEmpty() },
        basePrice = basePrice
    )
    val totalDiscountDouble = totalDiscount.doubleValue(false)
    val finalPrice = basePrice - totalDiscount
    val finalPriceDouble = finalPrice.doubleValue(false)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Bar
                DiscountTopBar(
                    productName = product.name,
                    onClose = onDismiss,
                    onApply = { onApply(discountSlots.filter { it.value.isNotEmpty() }) },
                    themeManager = themeManager
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { focusManager.clearFocus() })
                        },
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    // Product Info Card
                    item {
                        ProductInfoCard(
                            product = product,
                            unit = unit,
                            quantity = quantity,
                            basePrice = basePriceDouble
                        )
                    }

                    // Discount Slots
                    item {
                        Text(
                            text = StringResource.DISCOUNT_ITEMS.fromResource(),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    itemsIndexed(
                        items = discountSlots.filter { it.isEnabled },
                        key = { _, slot -> slot.slotNumber }
                    ) { index, slot ->
                        val config = slotConfigs.find { it.slotNumber == slot.slotNumber }

                        if (config != null) {
                            DiscountSlotCard(
                                index = index + 1,
                                slot = slot,
                                config = config,
                                onValueChanged = { newValue ->
                                    discountSlots = discountSlots.map {
                                        if (it.slotNumber == slot.slotNumber) {
                                            it.copy(value = newValue)
                                        } else it
                                    }
                                },
                                onTypeChanged = { newType ->
                                    focusManager.clearFocus()
                                    discountSlots = discountSlots.map {
                                        if (it.slotNumber == slot.slotNumber) {
                                            it.copy(type = newType)
                                        } else it
                                    }
                                }
                            )
                        }
                    }

                    // Summary Card
                    if (totalDiscount > BigDecimal.ZERO) {
                        item {
                            DiscountSummaryCard(
                                basePrice = basePriceDouble,
                                totalDiscount = totalDiscountDouble,
                                finalPrice = finalPriceDouble
                            )
                        }
                    }

                    // Bottom spacing
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscountTopBar(
    productName: String,
    onClose: () -> Unit,
    onApply: () -> Unit,
    themeManager: ThemeManager
) {
    RepzoneTopAppBar(
        modifier = Modifier.padding(0.dp),
        themeManager = themeManager,
        leftIconType = TopBarLeftIcon.Close(onClick = onClose),
        title = StringResource.DISCOUNT_CUSTOM.fromResource(),
        subtitle = productName,
        rightIcons = listOf(
            TopBarAction(Icons.Default.Save, "Kaydey", Color.White, {
                onApply
            }),
        )
    )
}

@Composable
private fun ProductInfoCard(
    product: ProductInformationModel,
    unit: ProductUnit,
    quantity: BigDecimal,
    basePrice: Double
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Product header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Product icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Inventory2,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    if (product.sku.isNotEmpty()) {
                        Text(
                            text = product.sku,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = StringResource.QUANTITY.fromResource(),
                    value = "${quantity.toPlainString()} ${unit.unitName}",
                    modifier = Modifier.weight(1f)
                )
                InfoItem(
                    label = StringResource.UNIT_PRICE.fromResource(),
                    value = unit.price.doubleValue(false).toMoney(),
                    modifier = Modifier.weight(1f),
                    alignment = TextAlign.Center
                )
                InfoItem(
                    label = StringResource.TOTAL_PURE.fromResource(),
                    value = basePrice.toMoney(),
                    modifier = Modifier.weight(1f),
                    alignment = TextAlign.End,
                    valueColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    alignment: TextAlign = TextAlign.Start,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = alignment,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
            textAlign = alignment,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DiscountSlotCard(
    index: Int,
    slot: DiscountSlotEntry,
    config: DiscountSlotConfig,
    onValueChanged: (String) -> Unit,
    onTypeChanged: (DiscountType) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Number badge
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = index.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = config.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Input row
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Type selector (segmented buttons)
                DiscountTypeSelector(
                    selectedType = slot.type,
                    onTypeSelected = onTypeChanged
                )

                // Value input
                DecimalTextField(
                    value = slot.value,
                    onValueChange = onValueChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = "0",
                    cornerRadius = 6.dp,
                    showBorder = true,
                    borderType = BorderType.FULL,
                    textAlignment = TextAlignment.END,
                    selectAllOnFocus = true,
                    maxValue = slot.maximumValue,
                    showClearIcon = false,
                    suffix= when(slot.type){
                        DiscountType.PERCENTAGE -> "%"
                        DiscountType.FIXED_AMOUNT -> NumberFormatInfo.currencySymbol
                    },
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Transparent
                )
            }

            // Validation error
            if (slot.validationError != null) {
                Text(
                    text = slot.validationError!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun DiscountTypeSelector(
    selectedType: DiscountType,
    onTypeSelected: (DiscountType) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        DiscountTypeChip(
            text = "%",
            isSelected = selectedType == DiscountType.PERCENTAGE,
            onClick = { onTypeSelected(DiscountType.PERCENTAGE) }
        )

        DiscountTypeChip(
            text = "₺",
            isSelected = selectedType == DiscountType.FIXED_AMOUNT,
            onClick = { onTypeSelected(DiscountType.FIXED_AMOUNT) }
        )
    }
}

@Composable
private fun DiscountTypeChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        },
        modifier = Modifier.size(24.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun DiscountSummaryCard(
    basePrice: Double,
    totalDiscount: Double,
    finalPrice: Double
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "İskonto Özeti",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Base price
            SummaryRow(
                label = "Ara Toplam",
                value = basePrice.toMoney(),
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            // Discount
            SummaryRow(
                label = "Toplam İskonto",
                value = "- ${totalDiscount.toMoney()}",
                valueColor = MaterialTheme.colorScheme.error
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Final price
            SummaryRow(
                label = "Genel Toplam",
                value = finalPrice.toMoney(),
                valueColor = MaterialTheme.colorScheme.primary,
                isBold = true
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

private fun calculateTotalDiscount(
    discountSlots: List<DiscountSlotEntry>,
    basePrice: BigDecimal
): BigDecimal {
    var total = BigDecimal.ZERO

    discountSlots.forEach { slot ->
        val value = slot.value.toBigDecimalOrNull() ?: return@forEach

        total += when (slot.type) {
            DiscountType.PERCENTAGE -> {
                (basePrice * value).divide(BigDecimal.fromInt(100), DecimalMode.DEFAULT)
            }
            DiscountType.FIXED_AMOUNT -> {
                value
            }
        }
    }

    return total
}