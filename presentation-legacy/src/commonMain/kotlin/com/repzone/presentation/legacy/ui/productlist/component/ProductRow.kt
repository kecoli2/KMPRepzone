package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.repzone.core.ui.component.textfield.BorderType
import com.repzone.core.ui.component.textfield.NumberTextField
import com.repzone.core.ui.component.textfield.TextAlignment
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.document.model.ValidationStatus
import com.repzone.domain.model.product.ProductRowState
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.image_not_found

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRow(
    product: ProductInformationModel,
    state: ProductRowState,
    hasDiscountPermission: Boolean,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    onDiscountClick: () -> Unit,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color.Red,
    backgroundColor: Color
) {
    if (hasDiscountPermission) {
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value == SwipeToDismissBoxValue.EndToStart) {
                    onDiscountClick()
                }
                false
            }
        )
        val discountCount = state.discountSlots.count { it.value.isNotEmpty() }

        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                SwipeBackground(
                    hasDiscount = state.hasDiscount,
                    discountCount = discountCount
                )
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProductRowContent(
                product = product,
                state = state,
                onUnitCycle = onUnitCycle,
                onQuantityChanged = onQuantityChanged,
                indicatorColor = indicatorColor,
                backgroundColor = backgroundColor,
                modifier = modifier
            )
        }
    } else {
        ProductRowContent(
            product = product,
            state = state,
            onUnitCycle = onUnitCycle,
            onQuantityChanged = onQuantityChanged,
            indicatorColor = indicatorColor,
            backgroundColor = backgroundColor,
            modifier = modifier
        )
    }
}

@Composable
private fun SwipeBackground(
    hasDiscount: Boolean,
    discountCount: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier.padding(end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
            if (hasDiscount) {
                Badge { Text(discountCount.toString()) }
            }
        }
    }
}

@Composable
private fun ProductRowContent(
    product: ProductInformationModel,
    state: ProductRowState,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    indicatorColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val priceText = state.currentUnit?.price?.doubleValue(false)?.toMoney() ?: "-"
    val stockQty = product.stock.doubleValue(false)
    val stockText = "${stockQty.toInt()} ${product.baseUnit.unitName}"
    val hasStock = stockQty > 0

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Color Indicator Bar
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(80.dp)
                    .background(
                        color = indicatorColor,
                        shape = RoundedCornerShape(1.dp)
                    )
            )

            // Product Image
            ProductImage(
                imageUrl = null,
                productName = product.name
            )
            Spacer(modifier = Modifier.width(4.dp))
            // Product Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Name Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (product.sku.isNotEmpty()) {
                        Text(
                            "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = product.sku,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                // Brand
                if (product.brandName?.isNotEmpty() == true) {
                    Text(
                        text = product.brandName!!,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                // Price
                Text(
                    text = priceText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Stock
                StockInfoRow(stockText = stockText, hasStock = hasStock)

                // Document Badge
                if (state.isInDocument) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }

                // Validation Status
                ValidationStatusRow(
                    validationStatus = state.validationStatus,
                    isInDocument = state.isInDocument,
                    documentQuantity = state.documentQuantity.toPlainString(),
                    documentUnitName = state.documentUnitName
                )

                if (state.hasAnyEntry) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        // Kaydedilmiş entry'ler (unitEntries)
                        state.unitEntries.values.forEach { entry ->
                            EntryChip(
                                text = "${entry.quantity.toPlainString()} ${entry.unitName}",
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        // Mevcut giriş (henüz kaydedilmemiş)
                        if (state.isValidQuantity) {
                            state.currentUnit?.let { unit ->
                                EntryChip(
                                    text = "${state.quantityText} ${unit.unitName}",
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }
            }

            // Quantity Controls
            QuantityControls(
                currentUnit = state.currentUnit,
                quantityText = state.quantityText,
                validationStatus = state.validationStatus,
                onUnitCycle = onUnitCycle,
                onQuantityChanged = onQuantityChanged,
                backgroundColor = backgroundColor
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun StockInfoRow(
    stockText: String,
    hasStock: Boolean
) {
    val stockColor = if (hasStock) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.error
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Inventory2,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = stockColor
        )
        Text(
            text = stockText,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 11.sp,
            color = stockColor
        )
    }
}

@Composable
private fun QuantityControls(
    currentUnit: ProductUnit?,
    quantityText: String,
    validationStatus: ValidationStatus,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    backgroundColor: Color
) {
    val isError = validationStatus is ValidationStatus.Error

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        currentUnit?.let { unit ->
            TextButton(
                onClick = onUnitCycle,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.height(28.dp),
            ) {
                Text(
                    text = unit.unitName,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Outlined.Sync,
                    contentDescription = "Birim değiştir",
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        NumberTextField(
            value = quantityText,
            onValueChange = onQuantityChanged,
            modifier = Modifier.width(60.dp),
            placeholder = "0",
            height = 36.dp,
            cornerRadius = 0.dp,
            borderWidth = 0.5.dp,
            backgroundColor = Color.Transparent,
            showBorder = true,
            maxLength = 4,
            borderType = BorderType.BOTTOM_ONLY,
            textAlignment = TextAlignment.CENTER,
            selectAllOnFocus = true,
            focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            showClearIcon = false
        )
    }
}

@Composable
private fun ProductImage(
    imageUrl: String?,
    productName: String
) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = productName,
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            error = painterResource(Res.drawable.image_not_found)
        )
    } else {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ValidationStatusRow(
    validationStatus: ValidationStatus,
    isInDocument: Boolean,
    documentQuantity: String,
    documentUnitName: String?
) {
    when (validationStatus) {
        is ValidationStatus.Warning -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = validationStatus.message,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 2,
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
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = validationStatus.message,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2,
                )
            }
        }
        else -> {
            if (isInDocument) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Sepette: $documentQuantity ${documentUnitName ?: ""}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun EntryChip(
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = containerColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontWeight = FontWeight.Medium
        )
    }
}