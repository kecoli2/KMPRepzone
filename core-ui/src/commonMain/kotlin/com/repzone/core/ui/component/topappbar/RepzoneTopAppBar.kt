package com.repzone.core.ui.component.topappbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.repzone.core.ui.manager.theme.ThemeManager
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.img_generic_logo_min

@Composable
fun RepzoneTopAppBar(
    modifier: Modifier = Modifier,
    themeManager: ThemeManager,
    leftIconType: TopBarLeftIcon = TopBarLeftIcon.Menu(onClick = {}),
    logoRes: DrawableResource = Res.drawable.img_generic_logo_min,
    title: String? = null,
    subtitle: String? = null,
    rightIcons: List<TopBarAction> = emptyList(),
    elevation: Dp = 4.dp
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = themeManager.getCurrentColorScheme().colorPalet.secondary20,
        shadowElevation = elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Sol taraf - Hamburger Menu veya Back Button
            when (leftIconType) {
                is TopBarLeftIcon.Menu -> {
                    IconButton(onClick = leftIconType.onClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = leftIconType.tintColor ?: Color.White
                        )
                    }
                }
                is TopBarLeftIcon.Back -> {
                    IconButton(onClick = leftIconType.onClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = leftIconType.tintColor ?: Color.White
                        )
                    }
                }
                TopBarLeftIcon.None -> {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            }

            // Orta - Logo veya Title/Subtitle
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if (title == null && subtitle == null) {
                    Alignment.Center
                } else {
                    Alignment.CenterStart
                }
            ) {
                if (title != null || subtitle != null) {
                    // Title ve Subtitle varsa
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                } else {
                    // Title/Subtitle yoksa Logo göster (ortalı)
                    Surface(
                        modifier = Modifier,
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(logoRes),
                                contentDescription = null,
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Inside
                            )
                        }
                    }
                }
            }

            // Sağ taraf - Maksimum 3 ikon
            rightIcons.take(3).forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription,
                        tint = action.tintColor ?: Color.White
                    )
                }
            }

            // Eğer 3'ten az ikon varsa boşluk ekle (dengelemek için)
            /*repeat(3 - minOf(rightIcons.size, 3)) {
                Spacer(modifier = Modifier.width(48.dp))
            }*/
        }
    }
}

// Sealed class for left icon type
sealed class TopBarLeftIcon {
    data class Menu(val onClick: () -> Unit, val tintColor: Color? = null) : TopBarLeftIcon()
    data class Back(val onClick: () -> Unit, val tintColor: Color? = null) : TopBarLeftIcon()
    data object None : TopBarLeftIcon()
}

// Data class for right side actions
data class TopBarAction(
    val icon: ImageVector,
    val contentDescription: String,
    val tintColor: Color?,
    val onClick: () -> Unit,
)