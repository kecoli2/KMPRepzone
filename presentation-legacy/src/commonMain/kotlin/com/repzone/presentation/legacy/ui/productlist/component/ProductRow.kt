package com.repzone.presentation.legacy.ui.productlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.repzone.core.ui.component.textfield.NumberTextField
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.document.model.Product
import com.repzone.domain.document.model.ValidationStatus
import com.repzone.domain.model.product.ProductRowState
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.image_not_found

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRow(
    product: Product,
    state: ProductRowState,
    hasDiscountPermission: Boolean,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    onDiscountClick: () -> Unit,
    modifier: Modifier = Modifier,
    themeManager: ThemeManager
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

        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                SwipeBackground(
                    hasDiscount = state.hasDiscount,
                    discountCount = state.discountSlots.count { it.value.isNotEmpty() }
                )
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true,
            modifier = modifier
        ) {
            ProductRowContent(
                product = product,
                state = state,
                onUnitCycle = onUnitCycle,
                onQuantityChanged = onQuantityChanged,
                themeManager = themeManager
            )
        }
    } else {
        ProductRowContent(
            product = product,
            state = state,
            onUnitCycle = onUnitCycle,
            onQuantityChanged = onQuantityChanged,
            modifier = modifier,
            themeManager = themeManager
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
    product: Product,
    state: ProductRowState,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    themeManager: ThemeManager
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image
            ProductImage(imageUrl = "https://www.sourcewatch.org/images/thumb/6/65/Nestle-logo.jpg/300px-Nestle-logo.jpg", productName = product.name)

            // Product Info (Orta)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.code.isNotEmpty()) {
                        Text("•", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text =  product.code,
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

                // Brand & Code
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (product.brand.isNotEmpty()) {
                        Text(
                            text = product.brand,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }

                // Price
                Text(
                    text = state.currentUnit?.price?.doubleValue(false)?.toMoney() ?: "-",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Stock
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val stockQty = product.stockQuantity.doubleValue(false)
                    val stockColor = if (stockQty > 0) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error

                    Icon(
                        imageVector = Icons.Outlined.Inventory2,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = stockColor
                    )
                    Text(
                        text = "${stockQty.toInt()} ${product.baseUnit.unitName}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = stockColor
                    )
                }

                if ( 1 == 1|| state.isInDocument) {
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

                // Validation / Document Status
               /* ValidationStatusRow(
                    validationStatus = state.validationStatus,
                    isInDocument = state.isInDocument,
                    documentQuantity = state.documentQuantity.toPlainString(),
                    documentUnitName = state.documentUnitName
                )*/

                ValidationStatusRow(
                    validationStatus = ValidationStatus.Empty,
                    isInDocument = true,
                    documentQuantity = "15",
                    documentUnitName = "Koli"
                )
            }

            // Sağ taraf: Birim + Miktar
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Unit Button
                state.currentUnit?.let { unit ->
                    TextButton(
                        onClick = onUnitCycle,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = unit.unitName,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Outlined.Sync,
                            contentDescription = "Birim değiştir",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                // Quantity TextField
                NumberTextField(
                    value = state.quantityText,
                    onValueChange = onQuantityChanged,
                    modifier = Modifier.width(60.dp),
                    placeholder = "0",
                    height = 36.dp,
                    cornerRadius = 8.dp,
                    borderWidth = 0.5.dp,
                    backgroundColor = themeManager.getCurrentColorScheme().colorPalet.neutral95,
                    showBorder = true,
                    focusedBorderColor = when (state.validationStatus) {
                        is ValidationStatus.Error -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.primary
                    },
                    unfocusedBorderColor = when (state.validationStatus) {
                        is ValidationStatus.Error -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    },
                    showClearIcon = false
                )
            }
        }
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
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            error = painterResource(Res.drawable.image_not_found)
        )
    } else {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(8.dp))
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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