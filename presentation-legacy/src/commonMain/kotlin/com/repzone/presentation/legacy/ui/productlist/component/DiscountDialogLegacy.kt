package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.domain.document.model.DiscountSlotConfig
import com.repzone.domain.document.model.DiscountSlotEntry
import com.repzone.domain.document.model.DiscountType
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ProductUnit

/**
 * Full-screen dialog for entering discounts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscountDialogLegacy(
    product: Product,
    unit: ProductUnit,
    quantity: BigDecimal,
    existingDiscounts: List<DiscountSlotEntry>,
    slotConfigs: List<DiscountSlotConfig>,
    onApply: (List<DiscountSlotEntry>) -> Unit,
    onDismiss: () -> Unit
) {
    var discountSlots by remember {
        mutableStateOf(
            slotConfigs.map { config ->
                existingDiscounts.find { it.slotNumber == config.slotNumber }
                    ?: DiscountSlotEntry(
                        slotNumber = config.slotNumber,
                        isEnabled = config.allowManualEntry
                    )
            }
        )
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("İskonto Ekle") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, "Kapat")
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = { onApply(discountSlots) }
                        ) {
                            Text("Uygula")
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Product info
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Miktar",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "${quantity.toPlainString()} ${unit.unitName}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Birim Fiyat",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "${unit.price.toPlainString()} ₺",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                
                                Column {
                                    Text(
                                        text = "Toplam",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = "${(quantity * unit.price).toPlainString()} ₺",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Ürün için iskonto oranlarını veya tutarlarını girebilirsiniz.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Discount slots
                items(
                    items = discountSlots.filter { it.isEnabled },
                    key = { it.slotNumber }
                ) { slot ->
                    val config = slotConfigs.find { it.slotNumber == slot.slotNumber }
                    
                    if (config != null) {
                        DiscountSlotInput(
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
                                discountSlots = discountSlots.map {
                                    if (it.slotNumber == slot.slotNumber) {
                                        it.copy(type = newType)
                                    } else it
                                }
                            }
                        )
                    }
                }
                
                // Summary
                item {
                    val totalDiscount = calculateTotalDiscount(
                        discountSlots = discountSlots.filter { it.value.isNotEmpty() },
                        basePrice = quantity * unit.price
                    )
                    
                    if (totalDiscount > BigDecimal.ZERO) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "İskonto Özeti",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Toplam İskonto:")
                                    Text(
                                        text = "${totalDiscount.toPlainString()} ₺",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("İskonto Sonrası:")
                                    Text(
                                        text = "${((quantity * unit.price) - totalDiscount).toPlainString()} ₺",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscountSlotInput(
    slot: DiscountSlotEntry,
    config: DiscountSlotConfig,
    onValueChanged: (String) -> Unit,
    onTypeChanged: (DiscountType) -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Slot name
            Text(
                text = "${config.slotNumber}. ${config.name}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Value input
                OutlinedTextField(
                    value = slot.value,
                    onValueChange = { value ->
                        if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
                            onValueChanged(value)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Değer") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    isError = slot.validationError != null,
                    supportingText = slot.validationError?.let { error ->
                        { Text(error, color = MaterialTheme.colorScheme.error) }
                    }
                )
                
                // Type selector
                SegmentedButton(
                    selectedType = slot.type,
                    onTypeSelected = onTypeChanged
                )
            }
        }
    }
}

@Composable
private fun SegmentedButton(
    selectedType: DiscountType,
    onTypeSelected: (DiscountType) -> Unit
) {
    Row(
        modifier = Modifier
            .height(56.dp)
    ) {
        FilterChip(
            selected = selectedType == DiscountType.PERCENTAGE,
            onClick = { onTypeSelected(DiscountType.PERCENTAGE) },
            label = { Text("%") }
        )
        
        Spacer(Modifier.width(4.dp))
        
        FilterChip(
            selected = selectedType == DiscountType.FIXED_AMOUNT,
            onClick = { onTypeSelected(DiscountType.FIXED_AMOUNT) },
            label = { Text("₺") }
        )
    }
}

private fun calculateTotalDiscount(discountSlots: List<DiscountSlotEntry>, basePrice: BigDecimal): BigDecimal {
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
