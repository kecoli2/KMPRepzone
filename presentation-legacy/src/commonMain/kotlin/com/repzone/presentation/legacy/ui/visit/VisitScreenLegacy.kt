package com.repzone.presentation.legacy.ui.visit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.repzone.core.constant.CdnConfig
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
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.presentation.legacy.viewmodel.visit.VisitUiState
import com.repzone.presentation.legacy.viewmodel.visit.VisitViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.customerbalanceheader
import repzonemobile.core.generated.resources.ecustomerinfo
import repzonemobile.core.generated.resources.image_not_found
import repzonemobile.core.generated.resources.routedescriptionheader
import repzonemobile.core.generated.resources.routetoday
import repzonemobile.core.generated.resources.routetomorrow
import kotlin.time.ExperimentalTime

@Composable
fun VisitScreenLegacy(customer: CustomerItemModel, onBackClick: () -> Unit ) = ViewModelHost<VisitViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val uiState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        viewModel.initiliaze(customer)
    }

    HandleBackPress {
        onBackClick()
    }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues))
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(2.dp)
                    .graphicsLayer {
                        shadowElevation = 6f
                        shape = RectangleShape
                        clip = false
                    }
                    .background(Color.Gray.copy(alpha = 0.4f))
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun CustomerSummary(customer: CustomerItemModel, themeManager: ThemeManager, visitUiState: VisitUiState){
    val iModuleParameters: IMobileModuleParameterRepository = koinInject()

    val formattedDateStart = remember(customer.date) {
        customer.date?.toEpochMilliseconds()?.toDateString("HH:mm") ?: ""
    }

    val formattedDate2 = remember(customer.date) {
        customer.endDate?.toEpochMilliseconds()?.toDateString("HH:mm") ?: ""
    }

    val dayDesc =
        when {
            customer.date?.toEpochMilliseconds()?.isToday() == true -> repzonemobile.core.generated.resources.Res.string.routetoday.fromResource()
            customer.date?.toEpochMilliseconds()?.isTomorrow() == true -> repzonemobile.core.generated.resources.Res.string.routetomorrow.fromResource()
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
            if (customer?.imageUri != null) {
                AsyncImage(
                    model = "${CdnConfig.CDN_IMAGE_CONFIG}xs/${customer.imageUri}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    error = painterResource(repzonemobile.core.generated.resources.Res.drawable.image_not_found),
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
                    tint = themeManager.getCurrentColorScheme().colorPalet.primary50
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
                        text = "${Res.string.customerbalanceheader.fromResource()} ${visitUiState.customerBalance.toMoney()} / ${visitUiState.customerRiskBalance.toMoney()}",
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
                        text = "${formattedDateStart}-${formattedDate2}",
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
                }
            }
        }

    }
}

@Composable
fun ActionButton(item: ActionButtonListItem, themeManager: ThemeManager ,onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(48.dp)
    ) {
        // Icon Button
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = themeManager.getCurrentColorScheme().colorPalet.primary50,
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