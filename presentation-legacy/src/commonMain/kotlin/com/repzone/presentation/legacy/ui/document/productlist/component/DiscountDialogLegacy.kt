package com.repzone.presentation.legacy.ui.document.productlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Summarize
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
import com.repzone.core.ui.component.card.CardBadge
import com.repzone.core.ui.component.card.CardHeaderStyle
import com.repzone.core.ui.component.card.CardVariant
import com.repzone.core.ui.component.card.RepzoneCard
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
                        maximumValue = config.maxPercentage,
                        type = config.slotType
                    )
            }
        )
    }

    // Enabled slot'ları filtrele
    val enabledSlots = discountSlots.filter { it.isEnabled }

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

                    // İndirim Kalemleri Section - RepzoneCard içinde
                    item {
                        DiscountSlotsSection(
                            enabledSlots = enabledSlots,
                            slotConfigs = slotConfigs,
                            onValueChanged = { slotNumber, newValue ->
                                discountSlots = discountSlots.map {
                                    if (it.slotNumber == slotNumber) {
                                        it.copy(value = newValue)
                                    } else it
                                }
                            },
                            onTypeChanged = { slotNumber, newType ->
                                focusManager.clearFocus()
                                discountSlots = discountSlots.map {
                                    if (it.slotNumber == slotNumber) {
                                        it.copy(type = newType)
                                    } else it
                                }
                            }
                        )
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

                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
private fun DiscountSlotsSection(
    enabledSlots: List<DiscountSlotEntry>,
    slotConfigs: List<DiscountSlotConfig>,
    onValueChanged: (Int, String) -> Unit,
    onTypeChanged: (Int, DiscountType) -> Unit
) {
    RepzoneCard(
        title = StringResource.DISCOUNT_ITEMS.fromResource(),
        leadingIcon = Icons.Default.Discount,
        trailingContent = { CardBadge(count = enabledSlots.size) },
        modifier = Modifier.fillMaxWidth(),
        variant = CardVariant.ELEVATED,
        elevation = 2.dp,
        headerStyle = CardHeaderStyle.COMPACT
    ) {
        Column(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            enabledSlots.forEachIndexed { index, slot ->
                val config = slotConfigs.find { it.slotNumber == slot.slotNumber }

                if (config != null) {
                    DiscountSlotRow(
                        index = index + 1,
                        slot = slot,
                        config = config,
                        onValueChanged = { newValue -> onValueChanged(slot.slotNumber, newValue) },
                        onTypeChanged = { newType -> onTypeChanged(slot.slotNumber, newType) }
                    )

                    // Divider between items
                    if (index < enabledSlots.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 36.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscountSlotRow(
    index: Int,
    slot: DiscountSlotEntry,
    config: DiscountSlotConfig,
    onValueChanged: (String) -> Unit,
    onTypeChanged: (DiscountType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = index.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = config.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
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
                borderWidth = 0.7.dp,
                backgroundColor = Color.Transparent,
                suffix = when (slot.type) {
                    DiscountType.PERCENTAGE -> "%"
                    DiscountType.FIXED_AMOUNT -> NumberFormatInfo.currencySymbol
                }
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
            TopBarAction(Icons.Default.Save, "Kaydet", Color.White, onClick = {
                onApply()
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
    RepzoneCard(
        title = product.name,
        subtitle = product.sku.ifEmpty { null },
        leadingIcon = Icons.Outlined.Inventory2,
        modifier = Modifier.fillMaxWidth(),
        variant = CardVariant.ELEVATED,
        elevation = 2.dp,
        headerStyle = CardHeaderStyle.DEFAULT
    ) {
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
private fun DiscountTypeSelector(
    selectedType: DiscountType,
    onTypeSelected: (DiscountType) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
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
    RepzoneCard(
        title = StringResource.DISCOUNT_SUMMARY.fromResource(),
        leadingIcon = Icons.Outlined.Summarize,
        modifier = Modifier.fillMaxWidth(),
        variant = CardVariant.ELEVATED,
        elevation = 2.dp,
        headerStyle = CardHeaderStyle.COMPACT
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Base price
            SummaryRow(
                label = StringResource.SUB_TOTAL.fromResource(),
                value = basePrice.toMoney(),
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            // Discount
            SummaryRow(
                label = StringResource.DISCOUNT_TOTAL.fromResource(),
                value = "- ${totalDiscount.toMoney()}",
                valueColor = MaterialTheme.colorScheme.error
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Final price
            SummaryRow(
                label = StringResource.GRAND_TOTAL.fromResource(),
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