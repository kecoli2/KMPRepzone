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
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.enums.ActionButtonType
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.presentation.legacy.viewmodel.actionmenulist.ActionMenuListViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.image_not_found

@Composable
fun VisitScreenLegacy(customer: CustomerItemModel?) = ViewModelHost<ActionMenuListViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val uiState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    //val buttonList = uiState.actionButtonList
    val buttonList = listOf(
        ActionButtonListItem(
            actionType = ActionButtonType.VISITING_START,
        ),
        ActionButtonListItem(
            actionType = ActionButtonType.LOCATION,
        ),
        ActionButtonListItem(
            actionType = ActionButtonType.LIST,
        ),
        ActionButtonListItem(
            actionType = ActionButtonType.FOLDER,
        ),
        ActionButtonListItem(
            actionType = ActionButtonType.REPORT,
        ),
        ActionButtonListItem(
            actionType = ActionButtonType.NOTES,
            badgeCount = 2
        )
    )


    LaunchedEffect(Unit){
        viewModel.prepareActions(customer!!)
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
                        scope.launch {
                        }
                    }
                ),
                rightIcons = listOf(
                    TopBarAction(Icons.Default.Timer, "Timer", Color.Green, {

                    })
                ),
            )
            /*Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .graphicsLayer {
                        shadowElevation = 6f
                        shape = RectangleShape
                        clip = false
                    }
                    .background(Color.Gray.copy(alpha = 0.4f))
            )*/

            //Customer Summary
            CustomerSummary(customer = customer, themeManager = themeManager)

            Box(modifier = Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center
                ){
                LazyRow(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(buttonList) { item ->
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

@Composable
fun CustomerSummary(customer: CustomerItemModel?, themeManager: ThemeManager){
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
                Text(
                    text = "Customer Name : Salih Yücel",
                    style = MaterialTheme.typography.titleSmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Müşteri Kodu : 145778456",
                    style = MaterialTheme.typography.bodySmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Gaziemir Gazikent Mah 678 Sok No: 1 D blok Daire 1",
                    style = MaterialTheme.typography.bodySmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Bakiye: 285.00 / Limit: 215.00",
                    style = MaterialTheme.typography.bodySmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

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
                        text = "Cuma 31 Ekim 08:30-09:00",
                        style = MaterialTheme.typography.bodySmall,
                        color = themeManager.getCurrentColorScheme().colorPalet.white,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = "Rota Açıklaması: Rota Açıklaması",
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Bakiye: 285.00 / Limit: 215.00",
                    style = MaterialTheme.typography.bodySmall,
                    color = themeManager.getCurrentColorScheme().colorPalet.white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

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
        ActionButtonType.LOCATION -> Icons.Default.LocationOn
        ActionButtonType.LIST -> Icons.Default.List
        ActionButtonType.FOLDER -> Icons.Default.Folder
        ActionButtonType.REPORT -> Icons.Default.Assessment
        ActionButtonType.NOTES -> Icons.Default.Note
    }
}