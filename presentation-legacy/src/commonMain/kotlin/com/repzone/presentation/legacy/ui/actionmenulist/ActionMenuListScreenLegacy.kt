package com.repzone.presentation.legacy.ui.actionmenulist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.repzone.core.constant.CdnConfig
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.domain.model.CustomerItemModel
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.image_not_found

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
                    style = MaterialTheme.typography.titleMedium,
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
                    style = MaterialTheme.typography.titleMedium,
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