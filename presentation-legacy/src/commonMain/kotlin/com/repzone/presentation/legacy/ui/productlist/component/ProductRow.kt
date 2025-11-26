package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ValidationStatus
import com.repzone.domain.model.product.ProductRowState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRow(
    product: Product,
    state: ProductRowState,
    hasDiscountPermission: Boolean,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    onDiscountClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (hasDiscountPermission) {
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.EndToStart) {
                    onDiscountClick()
                }
                false // Don't dismiss, reset position
            }
        )

        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                // Background revealed on swipe
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Column(
                        modifier = Modifier.padding(end = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Percent,
                            contentDescription = "İskonto",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "İskonto",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        if (state.hasDiscount) {
                            Badge {
                                Text(state.discountSlots.count { it.value.isNotEmpty() }.toString())
                            }
                        }
                    }
                }
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            modifier = modifier
        ) {
            ProductCardContent(
                product = product,
                state = state,
                onUnitCycle = onUnitCycle,
                onQuantityChanged = onQuantityChanged,
                onAddClick = onAddClick
            )
        }
    } else {
        ProductCardContent(
            product = product,
            state = state,
            onUnitCycle = onUnitCycle,
            onQuantityChanged = onQuantityChanged,
            onAddClick = onAddClick,
            modifier = modifier
        )
    }
}

@Composable
private fun ProductCardContent(
    product: Product,
    state: ProductRowState,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product image
            AsyncImage(
                model = "",
                contentDescription = product.name,
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            )

            // Product info and controls
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Product name and brand
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (product.brand.isNotEmpty()) {
                            Text(
                                text = product.brand,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (product.groupId?.isNotEmpty() == true) {
                            Text("•", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = product.groupId!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Quantity controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Unit cycle button
                    state.currentUnit?.let { unit ->
                        FilledTonalIconButton(
                            onClick = onUnitCycle,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = unit.unitName.take(3),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = "Birim değiştir",
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    // Quantity input
                    OutlinedTextField(
                        value = state.quantityText,
                        onValueChange = onQuantityChanged,
                        modifier = Modifier.width(100.dp),
                        placeholder = { Text("Miktar") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = state.validationStatus is ValidationStatus.Error
                    )

                    // Badge
                    state.currentUnit?.let { unit ->
                        if (state.quantityText.isNotEmpty()) {
                            UnitBadge(
                                quantity = state.quantityText,
                                unit = unit,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    Spacer(Modifier.weight(1f))

                    // Add button
                    FilledIconButton(
                        onClick = onAddClick,
                        enabled = state.canAddToDocument,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ekle"
                        )
                    }
                }

                // Validation message
                ValidationMessage(state.validationStatus)

                // Document status
                if (state.isInDocument) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Sepette: ${state.documentQuantity.toPlainString()} ${state.documentUnitName ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ValidationMessage(status: ValidationStatus) {
    when (status) {
        is ValidationStatus.Warning -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = status.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }

        is ValidationStatus.Error -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = status.message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        ValidationStatus.Valid -> {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }

        ValidationStatus.Empty -> { /* Nothing */ }
    }
}