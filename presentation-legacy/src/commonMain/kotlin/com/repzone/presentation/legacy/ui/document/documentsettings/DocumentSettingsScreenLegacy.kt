package com.repzone.presentation.legacy.ui.document.documentsettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.platform.NumberFormatter
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.card.CardVariant
import com.repzone.core.ui.component.card.RepzoneCard
import com.repzone.core.ui.component.dialog.RepzoneDialog
import com.repzone.core.ui.component.floatactionbutton.SmartFabScaffold
import com.repzone.core.ui.component.floatactionbutton.model.FabAction
import com.repzone.core.ui.component.selectiondialog.GenericPopupList
import com.repzone.core.ui.component.selectiondialog.SelectionMode
import com.repzone.core.ui.component.textfield.BorderType
import com.repzone.core.ui.component.textfield.DecimalTextField
import com.repzone.core.ui.component.textfield.TextAlignment
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.ui.platform.HandleBackPress
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toBigDecimalOrNullLocalized
import com.repzone.core.util.extensions.toDateString
import com.repzone.domain.model.PaymentPlanModel
import com.repzone.presentation.legacy.viewmodel.document.documentsettings.DocumentSettingsUiState
import com.repzone.presentation.legacy.viewmodel.document.documentsettings.DocumentSettingsViewModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.*
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

//region Public Method
@OptIn(ExperimentalTime::class)
@Composable
fun DocumentSettingsScreenLegacy(onBasketNavigate: () -> Unit, onNavigateBack: () -> Unit = {}, onElectronicSignatureNavigate: () -> Unit = {}) = ViewModelHost<DocumentSettingsViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val uiState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    // Dialog states
    var showPaymentPlanDialog by rememberSaveable { mutableStateOf(false) }
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showElectronicSignatureDialog by rememberSaveable { mutableStateOf(false) }
    var paymentSearchQuery by rememberSaveable { mutableStateOf("") }

    var discount1Text by remember { mutableStateOf(uiState.invoiceDiscont1.toPlainString()) }
    var discount2Text by remember { mutableStateOf(uiState.invoiceDiscont2.toPlainString()) }
    var discount3Text by remember { mutableStateOf(uiState.invoiceDiscont3.toPlainString()) }

    LaunchedEffect(uiState.invoiceDiscont1, uiState.invoiceDiscont2, uiState.invoiceDiscont3) {
        discount1Text = if (uiState.invoiceDiscont1 == BigDecimal.ZERO) "" else uiState.invoiceDiscont1.toPlainString()
        discount2Text = if (uiState.invoiceDiscont2 == BigDecimal.ZERO) "" else uiState.invoiceDiscont2.toPlainString()
        discount3Text = if (uiState.invoiceDiscont3 == BigDecimal.ZERO) "" else uiState.invoiceDiscont3.toPlainString()
    }

    LaunchedEffect(Unit) {
        viewModel.onStartDocument()
    }

    HandleBackPress {
        viewModel.onEvent(DocumentSettingsViewModel.Event.NavigateToBack)
        onNavigateBack()
    }

    DocumentSettingsContent(
        themeManager = themeManager,
        uiState = uiState,
        discount1Text = discount1Text,
        discount2Text = discount2Text,
        discount3Text = discount3Text,
        onDiscount1Change = { value ->
            discount1Text = value
            scope.launch {
                val bigDecimalValue = value.toBigDecimalOrNullLocalized() ?: BigDecimal.ZERO
                viewModel.onEvent(DocumentSettingsViewModel.Event.SetInvoiceDiscount(1, bigDecimalValue))
            }
        },
        onDiscount2Change = { value ->
            discount2Text = value
            scope.launch {
                val bigDecimalValue = value.toBigDecimalOrNullLocalized() ?: BigDecimal.ZERO
                viewModel.onEvent(DocumentSettingsViewModel.Event.SetInvoiceDiscount(2, bigDecimalValue))
            }
        },
        onDiscount3Change = { value ->
            discount3Text = value
            scope.launch {
                val bigDecimalValue = value.toBigDecimalOrNullLocalized() ?: BigDecimal.ZERO
                viewModel.onEvent(DocumentSettingsViewModel.Event.SetInvoiceDiscount(3, bigDecimalValue))
            }
        },
        onPaymentPlanClick = { showPaymentPlanDialog = true },
        onDatePickerClick = { showDatePickerDialog = true },
        onElectronicSignatureClick = { showElectronicSignatureDialog = true },
        onNavigateBack = onNavigateBack,
        onFabClick = {
            if (uiState.selectedPayment != null) {
                scope.launch {
                    viewModel.onEvent(DocumentSettingsViewModel.Event.NavigateToPreviewDocument)
                }
                onBasketNavigate()
            } else {
                showConfirmDialog = true
            }
        }
    )

        // ===== DIALOGS =====

        // Payment Plan Selection Dialog
        if (showPaymentPlanDialog) {
            PaymentPlanSelectionDialog(
                paymentPlanList = uiState.paymentPlanList,
                selectedPayment = uiState.selectedPayment,
                searchQuery = paymentSearchQuery,
                onSearchQueryChange = { paymentSearchQuery = it },
                onPaymentSelected = { payment ->
                    viewModel.onEvent(DocumentSettingsViewModel.Event.SetSelectedPayment(payment))
                    showPaymentPlanDialog = false
                    paymentSearchQuery = ""
                },
                onDismiss = {
                    showPaymentPlanDialog = false
                    paymentSearchQuery = ""
                },
            )
        }

            // Date Picker Dialog
            if (showDatePickerDialog) {
                DispatchDatePickerDialog(
                    currentDate = uiState.dispatchDate,
                    onDateSelected = { date ->
                        scope.launch {
                            viewModel.onEvent(DocumentSettingsViewModel.Event.SetDispatchDate(date))
                        }
                        showDatePickerDialog = false
                    },
                    onDismiss = { showDatePickerDialog = false }
                )
            }

            // Confirm Dialog - Missing info warning
            RepzoneDialog(
                isOpen = showConfirmDialog,
                title = Res.string.warning.fromResource(),
                message = Res.string.payment_plan_required.fromResource(),
                showYesButton = true,
                yesText = Res.string.dialogok.fromResource(),
                showNoButton = false,
                onYes = { showConfirmDialog = false },
                onNo = { showConfirmDialog = false },
            )

            // Electronic Signature Dialog
            RepzoneDialog(
                isOpen = showElectronicSignatureDialog,
                title = Res.string.electronic_signature.fromResource(),
                message = Res.string.electronic_signature_confirm.fromResource(),
                yesText = Res.string.continue_text.fromResource(),
                noText = Res.string.dialogcancel.fromResource(),
                onYes = {
                    showElectronicSignatureDialog = false
                    onElectronicSignatureNavigate()
                },
                onNo = { showElectronicSignatureDialog = false }
            )
}
//endregion Public Method

//region Private Method
@OptIn(ExperimentalTime::class)
@Composable
private fun DocumentSettingsContent(
    themeManager: ThemeManager,
    uiState: DocumentSettingsUiState,
    discount1Text: String,
    discount2Text: String,
    discount3Text: String,
    onDiscount1Change: (String) -> Unit,
    onDiscount2Change: (String) -> Unit,
    onDiscount3Change: (String) -> Unit,
    onPaymentPlanClick: () -> Unit,
    onDatePickerClick: () -> Unit,
    onElectronicSignatureClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onFabClick: () -> Unit
) {
    val formatter = remember { NumberFormatter() }

    SmartFabScaffold(
        fabAction = FabAction.Single(
            icon = Icons.Default.ArrowForward,
            contentDescription = Res.string.continue_text.fromResource()
        ),
        onFabClick = onFabClick,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            RepzoneTopAppBar(
                themeManager = themeManager,
                leftIconType = TopBarLeftIcon.Back(onClick = onNavigateBack),
                title = Res.string.document_settings.fromResource(),
                subtitle = Res.string.payment_and_delivery_info.fromResource()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ===== CARD 1: Ödeme Planı ve Sevk Tarihi =====
                PaymentInfoCard(
                    selectedPayment = uiState.selectedPayment,
                    dispatchDate = uiState.dispatchDate,
                    onPaymentPlanClick = onPaymentPlanClick,
                    onDatePickerClick = onDatePickerClick
                )

                // ===== CARD 2: Fatura Altı İskontolar =====
                InvoiceDiscountCard(
                    discount1 = discount1Text,
                    discount2 = discount2Text,
                    discount3 = discount3Text,
                    onDiscount1Change = onDiscount1Change,
                    onDiscount2Change = onDiscount2Change,
                    onDiscount3Change = onDiscount3Change
                )

                // ===== CARD 3: Müşteri Finansal Bilgileri =====
                CustomerFinancialCard(
                    customerDebt = uiState.customerDebt,
                    riskyBalance = uiState.riskyBalance,
                    creditLimit = uiState.creditLimit,
                    formatter = formatter
                )

                // ===== CARD 4: Elektronik İmza (Parametrik) =====
                if (uiState.showElectronicSignature) {
                    ElectronicSignatureCard(
                        onClick = onElectronicSignatureClick
                    )
                }

                // Bottom spacing for FAB
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun PaymentInfoCard(
    selectedPayment: PaymentPlanModel?,
    dispatchDate: Instant?,
    onPaymentPlanClick: () -> Unit,
    onDatePickerClick: () -> Unit
) {
    RepzoneCard(
        title = Res.string.payment_info.fromResource(),
        leadingIcon = Icons.Default.Payment,
        variant = CardVariant.ELEVATED
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Ödeme Planı Seçimi
        SelectableRow(
            label = Res.string.payment_plan.fromResource(),
            value = selectedPayment?.name ?: Res.string.select.fromResource(),
            onClick = onPaymentPlanClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sevk Tarihi Seçimi
        SelectableRow(
            label = Res.string.dispatch_date.fromResource(),
            value = dispatchDate?.toEpochMilliseconds()?.toDateString("dd/MM/yyyy") ?: Res.string.select_date.fromResource(),
            onClick = onDatePickerClick,
            trailingIcon = Icons.Default.DateRange
        )
    }
}

@Composable
private fun InvoiceDiscountCard(
    discount1: String,
    discount2: String,
    discount3: String,
    onDiscount1Change: (String) -> Unit,
    onDiscount2Change: (String) -> Unit,
    onDiscount3Change: (String) -> Unit
) {
    RepzoneCard(
        title = Res.string.invoice_discounts.fromResource(),
        leadingIcon = Icons.Default.Discount,
        variant = CardVariant.ELEVATED
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // İskonto 1
        DiscountRow(
            label = "${Res.string.discount.fromResource()} 1",
            value = discount1,
            onValueChange = onDiscount1Change,
            maxValue = BigDecimal.fromInt(100)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // İskonto 2
        DiscountRow(
            label = "${Res.string.discount.fromResource()} 2",
            value = discount2,
            onValueChange = onDiscount2Change,
            maxValue = BigDecimal.fromInt(100)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // İskonto 3
        DiscountRow(
            label = "${Res.string.discount.fromResource()} 3",
            value = discount3,
            onValueChange = onDiscount3Change,
            maxValue = BigDecimal.fromInt(100)
        )
    }
}

@Composable
private fun CustomerFinancialCard(
    customerDebt: BigDecimal,
    riskyBalance: BigDecimal,
    creditLimit: BigDecimal,
    formatter: NumberFormatter
) {
    RepzoneCard(
        title = Res.string.customer_financial_status.fromResource(),
        leadingIcon = Icons.Default.AccountBalance,
        variant = CardVariant.ELEVATED
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Müşteri Borcu
        FinancialInfoRow(
            label = Res.string.customer_debt.fromResource(),
            value = formatter.formatCurrency(customerDebt.doubleValue(false)),
            valueColor = if (customerDebt > BigDecimal.ZERO) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )

        // Riskli Bakiye
        FinancialInfoRow(
            label = Res.string.risky_balance.fromResource(),
            value = formatter.formatCurrency(riskyBalance.doubleValue(false)),
            valueColor = if (riskyBalance > BigDecimal.ZERO) Color(0xFFF59E0B) else MaterialTheme.colorScheme.onSurface
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )

        // Kredi Limiti
        FinancialInfoRow(
            label = Res.string.credit_limit.fromResource(),
            value = formatter.formatCurrency(creditLimit.doubleValue(false)),
            valueColor = Color(0xFF10B981)
        )
    }
}

@Composable
private fun ElectronicSignatureCard(
    onClick: () -> Unit
) {
    RepzoneCard(
        title = Res.string.electronic_signature.fromResource(),
        leadingIcon = Icons.Default.Draw,
        variant = CardVariant.OUTLINED,
        onClick = onClick,
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "null",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    ) {
        Text(
            text = Res.string.electronic_signature_hint.fromResource(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

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
    maxValue: BigDecimal
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
            maxValue = maxValue
        )
    }
}

@Composable
private fun FinancialInfoRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
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
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp,
        scrimColor = Color.Black.copy(alpha = 0.5f)
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
private fun PaymentPlanRow(
    paymentPlan: PaymentPlanModel,
    isSelected: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = paymentPlan.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = paymentPlan.code,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (paymentPlan.isDefault) {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = Res.string.default_text.fromResource(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
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
//endregion  Private Method