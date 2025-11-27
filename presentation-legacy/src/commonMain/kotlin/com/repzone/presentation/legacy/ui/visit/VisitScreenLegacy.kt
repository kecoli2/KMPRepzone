package com.repzone.presentation.legacy.ui.visit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.outlined.TripOrigin
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.repzone.core.constant.CdnConfig
import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.model.StringResource
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.dialog.RepzoneDialog
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarAction
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.ui.platform.HandleBackPress
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.isToday
import com.repzone.core.util.extensions.isTomorrow
import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.extensions.toDayName
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.models.VisitActionItem
import com.repzone.domain.util.models.VisitButtonItem
import com.repzone.presentation.legacy.viewmodel.visit.VisitUiState
import com.repzone.presentation.legacy.viewmodel.visit.VisitViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.*
import kotlin.time.ExperimentalTime

@Composable
fun VisitScreenLegacy(customer: CustomerItemModel, onBackClick: () -> Unit, onOpenDocument: () -> Unit ) = ViewModelHost<VisitViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(customer.customerId) {
        viewModel.initiliaze(customer)
    }

    HandleBackPress {
        onBackClick()
    }

    LaunchedEffect(Unit){
        viewModel.events.collect { event ->
            when(event){
                is VisitViewModel.Event.OnActionVisitItem ->{
                    when(event.visitActionItem.documentType){
                        DocumentActionType.INVOICE, DocumentActionType.ORDER, DocumentActionType.WAYBILL -> {
                            onOpenDocument()
                        }
                        DocumentActionType.WAREHOUSERECEIPT -> {
                        }
                        DocumentActionType.COLLECTION -> {
                        }
                        DocumentActionType.FORM -> {
                        }
                        else -> {}
                    }
                }
                else -> {

                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
        {
            // Custom Top Bar
            RepzoneTopAppBar(
                modifier = Modifier,
                themeManager = themeManager,
                leftIconType = TopBarLeftIcon.Back(
                    onClick = {
                        onBackClick()
                    }
                ),
                rightIcons = listOf(
                    TopBarAction(Icons.Default.Timer, "Timer", Color.Green, {

                    })
                ),
            )

            //Customer Summary
            CustomerSummary(customer = customer, themeManager = themeManager, uiState)
            val backgroundColor = MaterialTheme.colorScheme.background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawRect(
                            color = themeManager.getCurrentColorScheme().colorPalet.secondary20,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height / 2)
                        )
                        drawRect(
                            color = backgroundColor,
                            topLeft = Offset(0f, size.height / 2),
                            size = Size(size.width, size.height / 2)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(uiState.actionButtonList) { item ->
                        ActionButton(
                            item = item,
                            themeManager = themeManager,
                            onClick = {
                                viewModel.onEvent(VisitViewModel.Event.OnActionButton(item.actionType))
                            }
                        )
                    }
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(top = 4.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            VisitActionList(
                items = uiState.actionMenuList,
                onItemClick = { item ->
                    viewModel.onEvent(VisitViewModel.Event.OnActionVisitItem(visitActionItem = item))
                },
                themeManager = themeManager,
                modifier = Modifier.fillMaxSize()
                    .weight(1f)
            )
        }
    }

    uiState.showDecisionDialog?.let { dialogState ->
        RepzoneDialog(
            isOpen = true,
            title = dialogState.title.fromResource(),
            message = dialogState.message.fromResource(),
            yesText = dialogState.options.firstOrNull()?.label?.fromResource() ?: "",
            onYes = {
                viewModel.onEvent(VisitViewModel.Event.OnDecisionMade(
                    ruleId = dialogState.ruleId,
                    selectedOptions = dialogState.options.first(),
                    sessionId = dialogState.sessionId
                ))
            },
            showNoButton = dialogState.options.size > 1,
            noText = dialogState.options.getOrNull(1)?.label?.fromResource() ?: "",
            onNo = {
                viewModel.onEvent(VisitViewModel.Event.OnDecisionMade(
                    ruleId = dialogState.ruleId,
                    selectedOptions = dialogState.options[1],
                    sessionId = dialogState.sessionId
                ))
            }
        )
    }

}
// Sticky Header'lı Liste Composable
@Composable
fun VisitActionList(
    items: List<VisitActionItem>,
    onItemClick: (VisitActionItem) -> Unit,
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    // Gruplama: DocumentTypeGroup'a göre
    val groupedItems = remember(items) {
        items.groupBy { it.documentType }
            .toList()
            .sortedBy { it.first.ordinal }
            .toMap()
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedItems.forEach { (documentType, groupItems) ->
            // Sticky Header
            stickyHeader(key = documentType.name) {
                DocumentTypeHeader(
                    documentType = documentType,
                    itemCount = groupItems.size,
                    themeManager = themeManager
                )
            }

            // Grup içindeki itemlar
            items(
                items = groupItems,
                key = { it.name ?: it.documentName ?: it.hashCode() }
            ) { item ->
                VisitActionItemCard(
                    item = item,
                    themeManager = themeManager,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
fun DocumentTypeHeader(
    documentType: DocumentActionType,
    itemCount: Int,
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // İkon - renkli
            Icon(
                imageVector = documentType.getIcon(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(20.dp)
            )

            // Başlık - siyah/koyu
            Text(
                text = documentType.getDisplayName(),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.weight(1f))

            // Badge - grup item sayısı
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.45f)
            ) {
                Text(
                    text = itemCount.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = themeManager.getCurrentColorScheme().colorPalet.secondary100,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VisitActionItemCard(
    item: VisitActionItem,
    themeManager: ThemeManager,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .background(
                    if (item.hasDone) {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    } else {
                        Color.Transparent
                    }
                )
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Sol taraf - İkon/Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.getIcon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Orta - İçerik
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Başlık
                Text(
                    text = item.getMenuName(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Açıklama
                if (!item.description.isNullOrEmpty()) {
                    Text(
                        text = item.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Alt bilgi (badges)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mandatory badge
                    if (item.isMandatory) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Text(
                                text = Res.string.necessary.fromResource(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Interval badge
                    if (item.interval != TaskRepeatInterval.NONE) {
                        Badge(
                            containerColor = themeManager.getCurrentColorScheme().colorPalet.secondary60
                        ) {
                            Text(
                                text = item.interval.getDisplayName(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Fulfillment badge
                    if (item.isFulfillment) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "Sevkiyat",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Sağ taraf - Status/Action
            Icon(
                imageVector = if (item.hasDone) Icons.Default.CheckCircle else Icons.Default.ChevronRight,
                contentDescription = "Tamamlandı",
                tint = if (item.hasDone) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(20.dp)
            )
        }

        // Divider
        HorizontalDivider(
            modifier = Modifier.padding(start = 68.dp), // İkon genişliği + padding
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun CustomerSummary(customer: CustomerItemModel, themeManager: ThemeManager, visitUiState: VisitUiState){
    val formattedDateStart = remember(customer.date) {
        customer.date?.toEpochMilliseconds()?.toDateString("HH:mm") ?: ""
    }

    val formattedDate2 = remember(customer.endDate) {
        customer.endDate?.toEpochMilliseconds()?.toDateString("HH:mm") ?: ""
    }

    val dayDesc =
        when {
            customer.date?.toEpochMilliseconds()?.isToday() == true -> Res.string.routetoday.fromResource()
            customer.date?.toEpochMilliseconds()?.isTomorrow() == true -> Res.string.routetomorrow.fromResource()
            else -> customer.date?.toDayName() ?: ""
        }

    Box(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .background(themeManager.getCurrentColorScheme().colorPalet.secondary20),
        contentAlignment = Alignment.Center

    ){

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            //CUSTOMER ICON
            if (customer.imageUri != null) {
                AsyncImage(
                    model = "${CdnConfig.CDN_IMAGE_CONFIG}xs/${customer.imageUri}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    error = painterResource(Res.drawable.image_not_found),
                    onError = {
                        println("Error: ${it.result.throwable.message}")
                    },
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(modifier =
                Modifier.fillMaxWidth()
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start)
            {
                Row(modifier = Modifier.fillMaxWidth()){
                    customer.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleSmall,
                            color = themeManager.getCurrentColorScheme().colorPalet.white,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if(customer.customerBlocked){
                        Badge(containerColor = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 4.dp)) {
                            Text(
                                text = StringResource.CUSTOMERBLOCKEDTITLE.fromResource(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                customer.customerCode?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                customer.address?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if(visitUiState.visibleBalanceText){
                    Text(
                        text = "${Res.string.customerbalanceheader.fromResource()} ${customer.balance.toMoney()} / ${customer.customerRiskBalance.toMoney()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp),
                        tint = themeManager.getCurrentColorScheme().colorPalet.white
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "$dayDesc ${formattedDateStart}-${formattedDate2}",
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = Res.string.routedescriptionheader.fromResource() + " ${visitUiState.appoinmentDescription}",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )

                if(customer.isECustomer){
                    Text(
                        text = Res.string.ecustomerinfo.fromResource(),
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }else{
                    Text(
                        text = Res.string.ecustomerinfo.fromResource(),
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

    }
}
@Composable
fun ActionButton(item: VisitButtonItem, themeManager: ThemeManager, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(48.dp)
    ) {
        // Icon Button
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = getIconButtonTypeBackgroundColor(item.actionType, themeManager),
                    shape = RoundedCornerShape(50.dp)
                )
        ) {
            Icon(
                imageVector = getIconForActionType(item.actionType),
                contentDescription = item.actionType.name,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Badge (eğer varsa)
        if (item.badgeCount > 0) {
            Badge(
                modifier = Modifier.align(Alignment.TopEnd),
                containerColor = themeManager.getCurrentColorScheme().colorPalet.secondary60,
                contentColor = Color.White
            ) {
                Text(
                    text = if (item.badgeCount > 99) "99+" else item.badgeCount.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//region -------------- PRIVATE METHOD --------------
private fun VisitActionItem.getIcon(): ImageVector = when (this.documentType) {
    DocumentActionType.ORDER -> Icons.Default.ShoppingCart
    DocumentActionType.INVOICE -> Icons.Default.Receipt
    DocumentActionType.WAYBILL -> Icons.Default.LocalShipping
    DocumentActionType.WAREHOUSERECEIPT -> Icons.Default.Warehouse
    DocumentActionType.COLLECTION -> Icons.Default.Payments
    DocumentActionType.FORM -> {
        when(this.smallIcon){
            "0" ->{
                Icons.Default.Settings
            }
            "1"->{
                Icons.Default.Flight
            }
            "2" -> {
                Icons.Default.Menu
            }
            "3" ->{
                Icons.Default.ArrowDropDown
            }
            "4"->{
                Icons.AutoMirrored.Default.Sort
            }
            "5"->{
                Icons.Default.ClearAll
            }
            "6"->{
                Icons.Default.ArrowUpward
            }
            "7"->{
                Icons.Default.AttachFile
            }
            "8"->{
                Icons.Default.Mood
            }
            "9"->{
                Icons.Default.Stars
            }
            "10"->{
                Icons.Default.Bookmark
            }
            "11"->{
                Icons.Outlined.TripOrigin
            }
            "12"->{
                Icons.Default.Mode
            }
            else -> {
                Icons.Default.Description
            }
        }

    }
    DocumentActionType.OTHER -> Icons.Default.MoreHoriz
    else -> Icons.Default.Description
}

private fun DocumentActionType.getIcon(): ImageVector = when (this) {
    DocumentActionType.ORDER -> Icons.Default.ShoppingCart
    DocumentActionType.INVOICE -> Icons.Default.Receipt
    DocumentActionType.WAYBILL -> Icons.Default.LocalShipping
    DocumentActionType.WAREHOUSERECEIPT -> Icons.Default.Warehouse
    DocumentActionType.COLLECTION -> Icons.Default.Payments
    DocumentActionType.FORM ->  Icons.Default.Description
    DocumentActionType.OTHER -> Icons.Default.MoreHoriz
    else -> Icons.Default.Description
}

@Composable
private fun DocumentActionType.getDisplayName(): String = when (this) {
    DocumentActionType.ORDER -> Res.string.orders.fromResource()
    DocumentActionType.INVOICE -> Res.string.invoices.fromResource()
    DocumentActionType.WAYBILL -> Res.string.dispatches.fromResource()
    DocumentActionType.WAREHOUSERECEIPT -> Res.string.warehousereceipts.fromResource()
    DocumentActionType.COLLECTION -> Res.string.collections.fromResource()
    DocumentActionType.FORM -> Res.string.forms.fromResource()
    DocumentActionType.OTHER -> Res.string.other.fromResource()
    else -> "empty"
}

@Composable
private fun TaskRepeatInterval.getDisplayName(): String = when (this) {
    TaskRepeatInterval.NONE -> ""
    TaskRepeatInterval.ATVISITSTART -> Res.string.task_repeat_at_visit_start.fromResource()
    TaskRepeatInterval.ONE_TIME -> Res.string.task_repeat_one_time.fromResource()
    TaskRepeatInterval.WEEK -> Res.string.task_repeat_week.fromResource()
    TaskRepeatInterval.MONTH -> Res.string.task_repeat_month.fromResource()
    TaskRepeatInterval.TWO_WEEK -> Res.string.task_repeat_two_week.fromResource()
    TaskRepeatInterval.EVERY_VISIT -> Res.string.task_repeat_every_visit.fromResource()
}

@Composable
private fun VisitActionItem.getMenuName(): String {
    return when (this.documentType) {
        DocumentActionType.ORDER,DocumentActionType.WAREHOUSERECEIPT, DocumentActionType.INVOICE, DocumentActionType.WAYBILL, DocumentActionType.COLLECTION -> {
            when(this.name){
                "ReceivedOrder" -> Res.string.receivedorder.fromResource()
                "ElectronicOrder" -> Res.string.electronicorder.fromResource()
                "SalesReturnsOrder" -> Res.string.salesreturnsorder.fromResource()
                "DamagedReturnOrder" -> Res.string.damagedreturnorder.fromResource()
                "ReturnElectronicOrder" -> Res.string.returnelectronicorder.fromResource()
                "ReturnWholesaleInvoice" -> Res.string.returnwholesaleinvoice.fromResource()
                "WholesaleInvoice" -> Res.string.wholesaleinvoice.fromResource()
                "ElectronicInvoice" -> Res.string.electronicinvoice.fromResource()
                "ReturnElectronicInvoice" -> Res.string.returnelectronicinvoice.fromResource()
                "WholesaleDispatch" -> Res.string.wholesaledispatch.fromResource()
                "ReturnWholesaleDispatch" -> Res.string.returnwholesaledispatch.fromResource()
                "ElectronicDispatch" -> Res.string.electronicdispatch.fromResource()
                "ReturnElectronicDispatch" -> Res.string.returnelectronicdispatch.fromResource()
                "CollectionBill" -> Res.string.collectionbill.fromResource()
                "CollectionCash" -> Res.string.collectioncash.fromResource()
                "CollectionCheque" -> Res.string.collectioncheque.fromResource()
                "CollectionCreditCard" -> Res.string.collectioncreditcard.fromResource()
                "CollectionMoneyOrder" -> Res.string.collectionmoneyorder.fromResource()
                "DebitAdvice" -> Res.string.debitadvice.fromResource()
                "CreditAdvice" -> Res.string.creditadvice.fromResource()
                "ReturnAssetsPurchaseInvoice" -> Res.string.returnassetspurchaseinvoice.fromResource()
                "AssetsPurchaseInvoice" -> Res.string.assetspurchaseinvoice.fromResource()
                "AssetsPurchaseElectronicInvoice" -> Res.string.assetspurchaseelectronicinvoice.fromResource()
                "AssetsPurchaseReturnElectronicInvoice" -> Res.string.assetspurchasereturnelectronicinvoice.fromResource()
                "GivenOrder" -> Res.string.givenorder.fromResource()
                "AssetsPurchaseElectronicOrder" -> Res.string.assetspurchaseelectronicorder.fromResource()
                "AssetsPurchaseReturnElectronicOrder" -> Res.string.assetspurchasereturnelectronicorder.fromResource()
                "AssetsPurchaseReturnOrder" -> Res.string.assetspurchasereturnorder.fromResource()
                "AssetsPurchaseReturnDispatch" -> Res.string.assetspurchasereturndispatch.fromResource()
                "AssetsPurchaseDispatch" -> Res.string.assetspurchasedispatch.fromResource()
                "AssetsPurchaseElectronicDispatch" -> Res.string.assetspurchaseelectronicdispatch.fromResource()
                "AssetsPurchaseReturnElectronicDispatch" -> Res.string.assetspurchasereturnelectronicdispatch.fromResource()
                "InvoiceVehicleReturn" -> Res.string.invoicevehiclereturn.fromResource()
                "InvoiceOneToOneReturn" -> Res.string.invoiceonetoonereturn.fromResource()
                "InvoiceDamagedReturn" -> Res.string.invoicedamagedreturn.fromResource()
                "InvoiceOneToOneDamagedReturn" -> Res.string.invoiceonetoonedamagedreturn.fromResource()
                "NamedDeliveryOrder" -> Res.string.nameddeliveryorder.fromResource()
                "DamagedReturnElectronicOrder" -> Res.string.damaged_return_electronic_order.fromResource()
                "EInvoiceVehicleReturn" -> Res.string.e_invoice_vehicle_return.fromResource()
                "EInvoiceDamagedReturn" -> Res.string.e_invoice_damaged_return.fromResource()
                "EInvoiceOneToOneDamagedReturn" -> Res.string.e_invoice_one_to_one_damaged_return.fromResource()
                "DamagedReturnElectronicDispatch" -> Res.string.damaged_return_electronic_dispatch.fromResource()

                else -> {
                    this.name ?: this.documentName ?: ""
                }
            }
        }
        else -> {
            this.name ?: this.documentName ?: ""
        }
    }
}
@Composable
private fun getIconForActionType(type: ActionButtonType): ImageVector {
    return when (type) {
        ActionButtonType.VISITING_START -> Icons.Default.PlayArrow
        ActionButtonType.VISITING_END -> Icons.Default.Stop
        ActionButtonType.MAP -> Icons.Default.LocationOn
        ActionButtonType.ORDER_LOG -> Icons.Default.List
        ActionButtonType.DRIVE -> Icons.Default.Folder
        ActionButtonType.REPORT -> Icons.Default.Assessment
        ActionButtonType.NOTES -> Icons.Default.Note
    }
}
@Composable
private fun getIconButtonTypeBackgroundColor(type: ActionButtonType, themeManager: ThemeManager): Color {
    return when (type) {
        ActionButtonType.VISITING_START -> {
            themeManager.getCurrentColorScheme().colorPalet.startVisitIconBackGround
        }
        ActionButtonType.VISITING_END -> {
            themeManager.getCurrentColorScheme().colorPalet.stopVisitIconBackGround
        }
        else -> {
            MaterialTheme.colorScheme.primary
        }

    }
}
//endregion -------------- PRIVATE METHOD --------------