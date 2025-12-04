package com.repzone.presentation.legacy.ui.document.basket

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.constant.CdnConfig
import com.repzone.core.model.StringResource
import com.repzone.core.platform.NumberFormatter
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.card.CardHeaderStyle
import com.repzone.core.ui.component.card.ExpandableCard
import com.repzone.core.ui.component.dialog.RepzoneDialog
import com.repzone.core.ui.component.rowtemplate.BadgeConfig
import com.repzone.core.ui.component.rowtemplate.RepzoneRowItemTemplate
import com.repzone.core.ui.component.selectiondialog.GenericPopupList
import com.repzone.core.ui.component.selectiondialog.SelectionMode
import com.repzone.core.ui.component.textfield.BorderType
import com.repzone.core.ui.component.textfield.DecimalTextField
import com.repzone.core.ui.component.textfield.NumberTextField
import com.repzone.core.ui.component.textfield.TextAlignment
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarAction
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.ui.platform.HandleBackPress
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toBigDecimalOrNull
import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.document.base.IDocumentLine
import com.repzone.domain.document.model.ProductUnit
import com.repzone.domain.model.PaymentPlanModel
import com.repzone.presentation.legacy.viewmodel.document.basket.DocumentBasketUiState
import com.repzone.presentation.legacy.viewmodel.document.basket.DocumentBasketViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

//region Public Method
@OptIn(ExperimentalTime::class)
@Composable
fun DocumentBasketScreenLegacy(
    onNavigateBack: () -> Unit = {},
    onNavigateToSuccess: () -> Unit = {}
) = ViewModelHost<DocumentBasketViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val uiState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    // Expandable states
    var isPaymentInfoExpanded by rememberSaveable { mutableStateOf(false) }
    var isDiscountExpanded by rememberSaveable { mutableStateOf(false) }

    // Dialog states
    var showPaymentPlanDialog by rememberSaveable { mutableStateOf(false) }
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var paymentSearchQuery by rememberSaveable { mutableStateOf("") }

    // Local state for discount fields
    var discount1Text by remember { mutableStateOf("") }
    var discount2Text by remember { mutableStateOf("") }
    var discount3Text by remember { mutableStateOf("") }

    // Update local state when uiState changes
    LaunchedEffect(uiState.invoiceDiscount1, uiState.invoiceDiscount2, uiState.invoiceDiscount3) {
        discount1Text = if (uiState.invoiceDiscount1 == BigDecimal.ZERO) "" else uiState.invoiceDiscount1.toPlainString()
        discount2Text = if (uiState.invoiceDiscount2 == BigDecimal.ZERO) "" else uiState.invoiceDiscount2.toPlainString()
        discount3Text = if (uiState.invoiceDiscount3 == BigDecimal.ZERO) "" else uiState.invoiceDiscount3.toPlainString()
    }

    // Start document on launch
    LaunchedEffect(Unit) {
        viewModel.onStartDocument()
    }

    HandleBackPress {
        onNavigateBack()
    }

    BasketScreenContent(
        themeManager = themeManager,
        uiState = uiState,
        isPaymentInfoExpanded = isPaymentInfoExpanded,
        onPaymentInfoExpandChange = { isPaymentInfoExpanded = it },
        isDiscountExpanded = isDiscountExpanded,
        onDiscountExpandChange = { isDiscountExpanded = it },
        discount1Text = discount1Text,
        discount2Text = discount2Text,
        discount3Text = discount3Text,
        onDiscount1Change = { value ->
            discount1Text = value
            scope.launch {
                val bigDecimalValue = value.toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.onEvent(DocumentBasketViewModel.Event.SetInvoiceDiscount(1, bigDecimalValue))
            }
        },
        onDiscount2Change = { value ->
            discount2Text = value
            scope.launch {
                val bigDecimalValue = value.toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.onEvent(DocumentBasketViewModel.Event.SetInvoiceDiscount(2, bigDecimalValue))
            }
        },
        onDiscount3Change = { value ->
            discount3Text = value
            scope.launch {
                val bigDecimalValue = value.toBigDecimalOrNull() ?: BigDecimal.ZERO
                viewModel.onEvent(DocumentBasketViewModel.Event.SetInvoiceDiscount(3, bigDecimalValue))
            }
        },
        onPaymentPlanClick = { showPaymentPlanDialog = true },
        onDatePickerClick = { showDatePickerDialog = true },
        onLineEditClick = { line ->
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.OpenEditDialog(line)) }
        },
        onLineDeleteClick = { line ->
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.OpenDeleteDialog(line)) }
        },
        onQuantityChange = { lineId, quantity ->
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.UpdateLineQuantity(lineId, quantity)) }
        },
        onUnitCycle = { lineId ->
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.CycleLineUnit(lineId)) }
        },
        onNavigateBack = onNavigateBack,
        onSaveClick = {
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.SaveDocument) }
        }
    )

    //region ===== DIALOGS =====

    // Payment Plan Selection Dialog
    if (showPaymentPlanDialog) {
        PaymentPlanSelectionDialog(
            paymentPlanList = uiState.paymentPlanList,
            selectedPayment = uiState.selectedPayment,
            searchQuery = paymentSearchQuery,
            onSearchQueryChange = { paymentSearchQuery = it },
            onPaymentSelected = { payment ->
                scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.SetPaymentPlan(payment)) }
                showPaymentPlanDialog = false
                paymentSearchQuery = ""
            },
            onDismiss = {
                showPaymentPlanDialog = false
                paymentSearchQuery = ""
            }
        )
    }

    // Date Picker Dialog
    if (showDatePickerDialog) {
        DispatchDatePickerDialog(
            currentDate = uiState.dispatchDate,
            onDateSelected = { date ->
                scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.SetDispatchDate(date)) }
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }

    // Line Edit Dialog
    if (uiState.editingLine != null) {
        LineEditDialog(
            line = uiState.editingLine!!,
            availableUnits = uiState.editingLineUnits,
            selectedUnit = uiState.editingSelectedUnit,
            quantity = uiState.editingQuantity,
            onQuantityChange = { quantity ->
                scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.UpdateEditingQuantity(line = uiState.editingLine!! ,quantity)) }
            },
            onUnitChange = { unit ->
                scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.UpdateEditingUnit(line = uiState.editingLine!! ,unit)) }
            },
            onConfirm = {
                scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.ConfirmLineEdit) }
            },
            onDismiss = {
                scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.CloseEditDialog) }
            }
        )
    }

    // Delete Confirmation Dialog
    RepzoneDialog(
        isOpen = uiState.lineToDelete != null,
        title = Res.string.delete_product.fromResource(),
        message = "${uiState.lineToDelete?.productName ?: ""} ${Res.string.delete_product_confirm.fromResource()}",
        yesText = Res.string.dialogyes.fromResource(),
        noText = Res.string.dialogcancel.fromResource(),
        onYes = {
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.ConfirmDelete) }
        },
        onNo = {
            scope.launch { viewModel.onEvent(DocumentBasketViewModel.Event.CloseDeleteDialog) }
        }
    )
    //endregion ===== DIALOGS =====
}
//endregion Public Method

//region Private Method
@OptIn(ExperimentalTime::class)
@Composable
private fun BasketScreenContent(
    themeManager: ThemeManager,
    uiState: DocumentBasketUiState,
    isPaymentInfoExpanded: Boolean,
    onPaymentInfoExpandChange: (Boolean) -> Unit,
    isDiscountExpanded: Boolean,
    onDiscountExpandChange: (Boolean) -> Unit,
    discount1Text: String,
    discount2Text: String,
    discount3Text: String,
    onDiscount1Change: (String) -> Unit,
    onDiscount2Change: (String) -> Unit,
    onDiscount3Change: (String) -> Unit,
    onPaymentPlanClick: () -> Unit,
    onDatePickerClick: () -> Unit,
    onLineEditClick: (IDocumentLine) -> Unit,
    onLineDeleteClick: (IDocumentLine) -> Unit,
    onQuantityChange: (String, String) -> Unit,
    onUnitCycle: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    val formatter = remember { NumberFormatter() }
    val focusManager = LocalFocusManager.current

    Scaffold(
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                //TOP BAR
                RepzoneTopAppBar(
                    themeManager = themeManager,
                    leftIconType = TopBarLeftIcon.Back(onClick = onNavigateBack),
                    title = Res.string.basket.fromResource(),
                    subtitle = "${uiState.totalItemCount} ${Res.string.product.fromResource()}",
                    rightIcons = listOf(
                        TopBarAction(
                            icon = Icons.Default.Save,
                            contentDescription = "",
                            tintColor = Color.White,
                            onClick = {

                            },
                        ),
                        TopBarAction(
                            icon = Icons.Default.Share,
                            contentDescription = "",
                            tintColor = Color.White,
                            onClick = {

                            },
                        )
                    )

                )
                // Main Content with LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // ===== SECTION 1: Ödeme Bilgileri =====
                    item {
                        ExpandableCard(
                            title = Res.string.payment_info.fromResource(),
                            expanded = isPaymentInfoExpanded,
                            onExpandChange = onPaymentInfoExpandChange,
                            subtitle = StringResource.PAYMENT_SECTION_SUBTITLE.fromResource(uiState.selectedPayment?.name ?: "",uiState.dispatchDate?.toEpochMilliseconds()?.toDateString("dd/MM/yyyy") ?: "") ,
                            leadingIcon = Icons.Default.Payment,
                            headerStyle = CardHeaderStyle.COMPACT,


                        ) {
                            PaymentInfoContent(
                                selectedPayment = uiState.selectedPayment,
                                dispatchDate = uiState.dispatchDate,
                                onPaymentPlanClick = onPaymentPlanClick,
                                onDatePickerClick = onDatePickerClick
                            )
                        }
                    }

                    // ===== SECTION 2: Sepet Ürünleri =====
                    item {
                        BasketProductsSection(
                            lines = uiState.lines,
                            isEmpty = uiState.isBasketEmpty,
                            formatter = formatter,
                            onLineEditClick = onLineEditClick,
                            onLineDeleteClick = onLineDeleteClick,
                            onQuantityChange = onQuantityChange,
                            onUnitCycle = onUnitCycle
                        )
                    }

                    // ===== Görsel Ayıraç =====
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    // ===== SECTION 3: Fatura Altı İskontolar (Collapsible) =====
                    item {
                        val hasDiscounts = discount1Text.isNotEmpty() || discount2Text.isNotEmpty() || discount3Text.isNotEmpty()
                        ExpandableCard(
                            title = Res.string.invoice_discounts.fromResource(),
                            expanded = isDiscountExpanded,
                            onExpandChange = onDiscountExpandChange,
                            subtitle = if (hasDiscounts) Res.string.discount_applied.fromResource() else null,
                            leadingIcon = Icons.Default.Discount,
                            headerStyle = CardHeaderStyle.COMPACT
                        ) {
                            InvoiceDiscountContent(
                                discount1 = discount1Text,
                                discount2 = discount2Text,
                                discount3 = discount3Text,
                                onDiscount1Change = onDiscount1Change,
                                onDiscount2Change = onDiscount2Change,
                                onDiscount3Change = onDiscount3Change,
                                focusManager = focusManager
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }

                // ===== BOTTOM: Tutar Özeti =====
                TotalsSummaryCard(
                    grossTotal = uiState.grossTotal,
                    discountTotal = uiState.discountTotal,
                    netTotal = uiState.netTotal,
                    vatTotal = uiState.vatTotal,
                    grandTotal = uiState.grandTotal,
                    formatter = formatter
                )
            }

            // ===== TotalsSummaryCard =====
            FloatingActionButton(
                onClick = onSaveClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 180.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = Res.string.save.fromResource()
                )
            }
        }
    }
}

// ===== BASKET PRODUCTS SECTION =====

@Composable
private fun BasketProductsSection(
    lines: List<IDocumentLine>,
    isEmpty: Boolean,
    formatter: NumberFormatter,
    onLineEditClick: (IDocumentLine) -> Unit,
    onLineDeleteClick: (IDocumentLine) -> Unit,
    onQuantityChange: (String, String) -> Unit,
    onUnitCycle: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.animateContentSize()
        ) {
            // Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = Res.string.basket_products.fromResource(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = "${lines.size}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Content
            if (isEmpty) {
                EmptyBasketView()
            } else {
                Column {
                    lines.forEachIndexed { index, line ->
                        BasketLineRow(
                            line = line,
                            formatter = formatter,
                            onEditClick = { onLineEditClick(line) },
                            onDeleteClick = { onLineDeleteClick(line) },
                            onQuantityChange = { quantity -> onQuantityChange(line.id, quantity) },
                            onUnitCycle = { onUnitCycle(line.id) }
                        )
                        if (index < lines.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 72.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ===== BASKET LINE ROW =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasketLineRow(
    line: IDocumentLine,
    formatter: NumberFormatter,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitCycle: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDeleteClick()
            }
            false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            DeleteSwipeBackground()
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        BasketLineRowContent(
            line = line,
            formatter = formatter,
            onEditClick = onEditClick,
            onQuantityChange = onQuantityChange,
            onUnitCycle = onUnitCycle
        )
    }
}

@Composable
private fun DeleteSwipeBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier.padding(end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = Res.string.delete.fromResource(),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = Res.string.delete.fromResource(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun BasketLineRowContent(
    line: IDocumentLine,
    formatter: NumberFormatter,
    onEditClick: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitCycle: () -> Unit
) {
    val priceText = line.unitPrice.doubleValue(false).toMoney()
    val netPriceText = line.netUnitPrice.doubleValue(false).toMoney()
    val hasDiscount = line.hasDiscount()
    val lineTotalText = line.lineTotal.doubleValue(false).toMoney()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onEditClick)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Color Indicator Bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            // Product Image
            ProductImage(
                imageUrl = (line as? com.repzone.domain.document.model.DocumentLine)?.productInfo?.photoPath,
                productName = line.productName
            )

            // Product Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Product Name
                Text(
                    text = line.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Price Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (hasDiscount) {
                        Text(
                            text = priceText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                        Text(
                            text = netPriceText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = priceText,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Line Total
                Text(
                    text = "${Res.string.total.fromResource()}: $lineTotalText",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            // Quantity Controls
            QuantityControls(
                unitName = line.unitName,
                quantity = line.quantity.toPlainString(),
                onUnitCycle = onUnitCycle,
                onQuantityChanged = onQuantityChange
            )
        }
    }
}

@Composable
private fun QuantityControls(
    unitName: String,
    quantity: String,
    onUnitCycle: () -> Unit,
    onQuantityChanged: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Unit Button
        TextButton(
            onClick = onUnitCycle,
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = Modifier.height(28.dp),
        ) {
            Text(
                text = unitName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                imageVector = Icons.Outlined.Sync,
                contentDescription = Res.string.change_unit.fromResource(),
                modifier = Modifier.size(14.dp)
            )
        }

        // Quantity Input
        NumberTextField(
            value = quantity,
            onValueChange = onQuantityChanged,
            modifier = Modifier.width(56.dp),
            placeholder = "0",
            height = 32.dp,
            cornerRadius = 0.dp,
            borderWidth = 1.dp,
            backgroundColor = Color.Transparent,
            showBorder = true,
            maxLength = 4,
            borderType = BorderType.BOTTOM_ONLY,
            textAlignment = TextAlignment.CENTER,
            selectAllOnFocus = true,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
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
            model = "${CdnConfig.CDN_IMAGE_CONFIG}xs/${imageUrl}",
            contentDescription = productName,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            error = painterResource(Res.drawable.image_not_found)
        )
    } else {
        Box(
            modifier = Modifier
                .size(48.dp)
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

// ===== EMPTY STATE =====

@Composable
private fun EmptyBasketView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Text(
            text = Res.string.basket_empty.fromResource(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

// ===== EXPANDABLE CARD CONTENTS =====

@OptIn(ExperimentalTime::class)
@Composable
private fun PaymentInfoContent(
    selectedPayment: PaymentPlanModel?,
    dispatchDate: Instant?,
    onPaymentPlanClick: () -> Unit,
    onDatePickerClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SelectableRow(
            label = Res.string.payment_plan.fromResource(),
            value = selectedPayment?.name ?: Res.string.select.fromResource(),
            onClick = onPaymentPlanClick
        )

        SelectableRow(
            label = Res.string.dispatch_date.fromResource(),
            value = dispatchDate?.toEpochMilliseconds()?.toDateString("dd/MM/yyyy") ?: Res.string.select_date.fromResource(),
            onClick = onDatePickerClick,
            trailingIcon = Icons.Default.DateRange
        )
    }
}

@Composable
private fun InvoiceDiscountContent(
    discount1: String,
    discount2: String,
    discount3: String,
    onDiscount1Change: (String) -> Unit,
    onDiscount2Change: (String) -> Unit,
    onDiscount3Change: (String) -> Unit,
    focusManager: FocusManager
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DiscountRow(
            label = "${Res.string.discount.fromResource()} 1",
            value = discount1,
            onValueChange = onDiscount1Change,
            focusManager = focusManager
        )

        DiscountRow(
            label = "${Res.string.discount.fromResource()} 2",
            value = discount2,
            onValueChange = onDiscount2Change,
            focusManager = focusManager
        )

        DiscountRow(
            label = "${Res.string.discount.fromResource()} 3",
            value = discount3,
            onValueChange = onDiscount3Change,
            focusManager = focusManager
        )
    }
}

// ===== TOTALS SUMMARY =====

@Composable
private fun TotalsSummaryCard(
    grossTotal: BigDecimal,
    discountTotal: BigDecimal,
    netTotal: BigDecimal,
    vatTotal: BigDecimal,
    grandTotal: BigDecimal,
    formatter: NumberFormatter
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TotalRow(
                label = Res.string.gross_total.fromResource(),
                value = formatter.formatCurrency(grossTotal.doubleValue(false)),
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            TotalRow(
                label = Res.string.discount_total.fromResource(),
                value = "-${formatter.formatCurrency(discountTotal.doubleValue(false))}",
                valueColor = Color(0xFFF59E0B)
            )

            TotalRow(
                label = Res.string.net_total.fromResource(),
                value = formatter.formatCurrency(netTotal.doubleValue(false)),
                valueColor = MaterialTheme.colorScheme.onSurface
            )

            TotalRow(
                label = Res.string.vat_total.fromResource(),
                value = "+${formatter.formatCurrency(vatTotal.doubleValue(false))}",
                valueColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Res.string.grand_total.fromResource(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = formatter.formatCurrency(grandTotal.doubleValue(false)),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TotalRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

// ===== HELPER COMPOSABLES =====

@Composable
private fun SelectableRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    trailingIcon: ImageVector = Icons.Default.KeyboardArrowDown
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun DiscountRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        DecimalTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(120.dp),
            placeholder = "0.00",
            suffix = "%",
            decimalPlaces = 2,
            showBorder = true,
            borderType = BorderType.FULL,
            cornerRadius = 8.dp,
            textAlignment = TextAlignment.END,
            selectAllOnFocus = true,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onNext = {  },
                onDone = { focusManager.clearFocus() }
            ),
            backgroundColor = Color.Transparent
        )
    }
}

// ===== DIALOGS =====

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LineEditDialog(
    line: IDocumentLine,
    availableUnits: List<ProductUnit>,
    selectedUnit: ProductUnit?,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (ProductUnit) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var showUnitSelector by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = Res.string.edit_product.fromResource(),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = line.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = Res.string.quantity.fromResource(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DecimalTextField(
                        value = quantity,
                        onValueChange = onQuantityChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "0",
                        decimalPlaces = 2,
                        showBorder = true,
                        borderType = BorderType.FULL,
                        cornerRadius = 8.dp,
                        height = 48.dp,
                        textAlignment = TextAlignment.START,
                        selectAllOnFocus = true
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = Res.string.unit.fromResource(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedCard(
                        onClick = { showUnitSelector = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = selectedUnit?.unitName ?: Res.string.select_unit.fromResource(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(Res.string.save.fromResource())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Res.string.dialogcancel.fromResource())
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )

    if (showUnitSelector) {
        ModalBottomSheet(
            onDismissRequest = { showUnitSelector = false },
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = Color.Transparent,
            dragHandle = null
        ) {
            GenericPopupList(
                items = availableUnits,
                selectionMode = SelectionMode.SINGLE,
                selectedItems = selectedUnit?.let { listOf(it) },
                itemContent = { unit, isSelected ->
                    UnitRow(unit = unit, isSelected = isSelected)
                },
                itemKey = { it.unitId },
                searchEnabled = false,
                searchPredicate = { _, _ -> true },
                confirmButtonText = Res.string.select.fromResource(),
                cancelButtonText = Res.string.dialogcancel.fromResource(),
                onConfirm = { selected ->
                    selected.firstOrNull()?.let { onUnitChange(it) }
                    showUnitSelector = false
                },
                onDismiss = { showUnitSelector = false }
            )
        }
    }
}

@Composable
private fun UnitRow(
    unit: ProductUnit,
    isSelected: Boolean
) {
    val formatter = remember { NumberFormatter() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = unit.unitName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = "${Res.string.multiplier.fromResource()}: ${unit.multiplier.toPlainString()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = formatter.formatCurrency(unit.price.doubleValue(false)),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentPlanSelectionDialog(
    paymentPlanList: List<PaymentPlanModel>,
    selectedPayment: PaymentPlanModel?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onPaymentSelected: (PaymentPlanModel) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        dragHandle = null,
    ) {
        GenericPopupList(
            items = paymentPlanList,
            selectionMode = SelectionMode.SINGLE,
            selectedItems = selectedPayment?.let { listOf(it) },
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            itemContent = { paymentPlan, isSelected ->
                PaymentPlanRow(paymentPlan = paymentPlan, isSelected = isSelected)
            },
            itemKey = { it.id },
            searchEnabled = true,
            searchPredicate = { paymentPlan, query ->
                paymentPlan.name.contains(query, ignoreCase = true) ||
                        paymentPlan.code.contains(query, ignoreCase = true)
            },
            searchPlaceholder = Res.string.search_payment_plan.fromResource(),
            confirmButtonText = Res.string.select.fromResource(),
            cancelButtonText = Res.string.dialogcancel.fromResource(),
            onConfirm = { selected ->
                selected.firstOrNull()?.let { onPaymentSelected(it) }
            },
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun PaymentPlanRow(paymentPlan: PaymentPlanModel, isSelected: Boolean) {
    RepzoneRowItemTemplate(
        title = paymentPlan.name,
        titleFontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        height = 40.dp,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        subtitle = paymentPlan.code,
        trailingBadge = if (paymentPlan.isDefault) {
            BadgeConfig(
                text = Res.string.default_text.fromResource(),
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else null
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun DispatchDatePickerDialog(
    currentDate: Instant?,
    onDateSelected: (Instant) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDate?.toEpochMilliseconds()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.fromEpochMilliseconds(millis)
                        onDateSelected(instant)
                    }
                }
            ) {
                Text(Res.string.dialogok.fromResource())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Res.string.dialogcancel.fromResource())
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            showModeToggle = false
        )
    }
}
//endregion Private Method