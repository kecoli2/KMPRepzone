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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warehouse
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.repzone.core.constant.CdnConfig
import com.repzone.core.enums.DocumentTypeGroup
import com.repzone.core.enums.TaskRepeatInterval
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.RepzoneTopAppBar
import com.repzone.core.ui.component.TopBarAction
import com.repzone.core.ui.component.TopBarLeftIcon
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
fun VisitScreenLegacy(customer: CustomerItemModel, onBackClick: () -> Unit ) = ViewModelHost<VisitViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(customer.customerId){
        viewModel.initiliaze(customer)
    }

    HandleBackPress {
        onBackClick()
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

            Box(modifier = Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
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
                            }
                        )

                    }
                }
            }
            HorizontalDivider( // ✅ Box yerine HorizontalDivider kullanın
                modifier = Modifier.padding(top = 4.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            VisitActionList(
                items = uiState.actionMenuList,
                onItemClick = { item ->
                    //viewModel.onActionItemClick(item)
                },
                themeManager = themeManager,
                modifier = Modifier.fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

// YENİ: Sticky Header'lı Liste Composable
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

// YENİ: Sticky Header Composable
@Composable
fun DocumentTypeHeader(
    documentType: DocumentTypeGroup,
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
                if (item.smallIcon != null) {
                    AsyncImage(
                        model = "${CdnConfig.CDN_IMAGE_CONFIG}xs/${item.smallIcon}",
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        error = painterResource(Res.drawable.image_not_found)
                    )
                } else {
                    Icon(
                        imageVector = item.documentType.getIcon(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Orta - İçerik
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Başlık
                Text(
                    text = item.name ?: item.documentName ?: "Unnamed",
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

// YENİ: Extension fonksiyonlar
fun DocumentTypeGroup.getIcon(): ImageVector = when (this) {
    DocumentTypeGroup.ORDER -> Icons.Default.ShoppingCart
    DocumentTypeGroup.INVOICE -> Icons.Default.Receipt
    DocumentTypeGroup.DISPATCH -> Icons.Default.LocalShipping
    DocumentTypeGroup.WAREHOUSERECEIPT -> Icons.Default.Warehouse
    DocumentTypeGroup.COLLECTION -> Icons.Default.Payments
    DocumentTypeGroup.FORM -> Icons.Default.Description
    DocumentTypeGroup.OTHER -> Icons.Default.MoreHoriz
    DocumentTypeGroup.EMPTY -> Icons.Default.RemoveCircleOutline
}

fun DocumentTypeGroup.getDisplayName(): String = when (this) {
    DocumentTypeGroup.ORDER -> "Siparişler"
    DocumentTypeGroup.INVOICE -> "Faturalar"
    DocumentTypeGroup.DISPATCH -> "Sevkiyatlar"
    DocumentTypeGroup.WAREHOUSERECEIPT -> "Depo Fişleri"
    DocumentTypeGroup.COLLECTION -> "Tahsilatlar"
    DocumentTypeGroup.FORM -> "Formlar"
    DocumentTypeGroup.OTHER -> "Diğer"
    DocumentTypeGroup.EMPTY -> "Boş"
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
                        text = "${Res.string.customerbalanceheader.fromResource()} ${customer.customerBalance.toMoney()} / ${customer.customerRiskBalance.toMoney()}",
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
                    color = MaterialTheme.colorScheme.primary,
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

@Composable
fun getIconForActionType(type: ActionButtonType): ImageVector {
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