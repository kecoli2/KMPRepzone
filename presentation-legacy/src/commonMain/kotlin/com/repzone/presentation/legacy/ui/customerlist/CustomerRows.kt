package com.repzone.presentation.legacy.ui.customerlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.repzone.core.constant.CdnConfig
import com.repzone.core.model.StringResource
import com.repzone.core.ui.component.rowtemplate.BadgeConfig
import com.repzone.core.ui.component.rowtemplate.ImageShapeType
import com.repzone.core.ui.component.rowtemplate.LeadingImageConfig
import com.repzone.core.ui.component.rowtemplate.RepzoneRowItemTemplate
import com.repzone.core.ui.component.timerbadge.TimerBadge
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.isToday
import com.repzone.core.util.extensions.isTomorrow
import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.extensions.toDayName
import com.repzone.domain.model.CustomerItemModel
import com.repzone.presentation.legacy.viewmodel.customerlist.CustomerListScreenUiState
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.customerblockedtitle
import repzonemobile.core.generated.resources.image_not_found
import repzonemobile.core.generated.resources.routetoday
import repzonemobile.core.generated.resources.routetomorrow
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
@Composable
fun CustomerCard(customer: CustomerItemModel,
                 modifier: Modifier = Modifier,
                 themeManager: ThemeManager,
                 onCustomerClick: (CustomerItemModel) -> Unit = {}, uiState : CustomerListScreenUiState
) {
    Surface(
        modifier = modifier.height(80.dp).clickable {
            if (uiState.customerListState != CustomerListScreenUiState.CustomerListState.Loading){
                onCustomerClick(customer)
            }
        },
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            if (customer.imageUri != null) {
                AsyncImage(
                    model = "${CdnConfig.CDN_IMAGE_CONFIG}xs/${customer.imageUri}",
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
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
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp),
                    tint = themeManager.getCurrentColorScheme().colorPalet.primary50
                )
            }

            // Orta metinler
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()){
                    Text(
                        text = customer.name ?: "",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if(customer.customerBlocked){
                        Badge(containerColor = MaterialTheme.colorScheme.error) {
                            Text(
                                text = Res.string.customerblockedtitle.fromResource(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Text(
                    text = customer.address ?: "",
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = customer.customerGroupName ?: "",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Sağ blok: sabit genişlik → daha az ölçüm maliyeti
            Column(
                modifier = Modifier
                    .widthIn(min = 120.dp) // sabitleyerek weight ölçümünü azalt
                    .padding(start = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                val formattedDateStart = remember(customer.date) {
                    customer.date?.toEpochMilliseconds()?.toDateString("HH:mm") ?: ""
                }

                val formattedDate2 = remember(customer.date) {
                    customer.endDate?.toEpochMilliseconds()?.toDateString("HH:mm") ?: ""
                }

                val dayDesc =
                    when {
                        customer.date?.toEpochMilliseconds()?.isToday() == true -> Res.string.routetoday.fromResource()
                        customer.date?.toEpochMilliseconds()?.isTomorrow() == true -> Res.string.routetomorrow.fromResource()
                        else -> customer.date?.toDayName() ?: ""
                    }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 6.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "${formattedDateStart}-${formattedDate2}",
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = AppTheme.dimens.textSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = dayDesc,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if(customer.showVisitTimer()){
                    TimerBadge(customer.visitStartDate!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun CustomerCardParent(
    customer: CustomerItemModel,
    modifier: Modifier = Modifier,
    themeManager: ThemeManager,
    onCustomerClick: (CustomerItemModel) -> Unit = {}
) {
    val dayDesc = when {
        customer.date?.toEpochMilliseconds()?.isToday() == true -> StringResource.ROUTETODAY.fromResource()
        customer.date?.toEpochMilliseconds()?.isTomorrow() == true -> StringResource.ROUTETOMORROW.fromResource()
        else -> customer.date?.toDayName() ?: ""
    }

    Surface(
        modifier = modifier.height(60.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        RepzoneRowItemTemplate(
            title = customer.name ?: "",
            subtitle = customer.address,
            titleSuffix = dayDesc.takeIf { it.isNotEmpty() },
            leadingImage = if (customer.imageUri != null) {
                LeadingImageConfig.Url(
                    url = "${CdnConfig.CDN_IMAGE_CONFIG}xs/${customer.imageUri}",
                    error = painterResource(Res.drawable.image_not_found)
                )
            } else {
                LeadingImageConfig.Icon(
                    imageVector = Icons.Default.Person,
                    tint = themeManager.getCurrentColorScheme().colorPalet.primary50,
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )
            },
            leadingImageSize = 48.dp,
            imageShapeType = ImageShapeType.CIRCLE,
            badge = if (customer.customerBlocked) {
                BadgeConfig(
                    text = Res.string.customerblockedtitle.fromResource(),
                    backgroundColor = MaterialTheme.colorScheme.error,
                    textColor = MaterialTheme.colorScheme.onError
                )
            } else null,
            onClick = { onCustomerClick(customer) }
        )
    }
}