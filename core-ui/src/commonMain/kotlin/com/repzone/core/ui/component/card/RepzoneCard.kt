package com.repzone.core.ui.component.card

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Card varyantları
 */
enum class CardVariant {
    FILLED,
    OUTLINED,
    ELEVATED
}

/**
 * Card renk şemaları
 */
enum class CardColorScheme {
    DEFAULT,        // Surface rengi
    PRIMARY,        // Primary container
    SECONDARY,      // Secondary container
    TERTIARY,       // Tertiary container
    ERROR,          // Error container
    SUCCESS,        // Yeşil tonu
    WARNING         // Sarı tonu
}

/**
 * Header stilleri
 */
enum class CardHeaderStyle {
    DEFAULT,
    COMPACT
}

/**
 * Genel amaçlı Card componenti
 */
@Composable
fun RepzoneCard(
    modifier: Modifier = Modifier,
    variant: CardVariant = CardVariant.FILLED,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 2.dp,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val colors = getCardColors(colorScheme, variant)
    val borderColor = getBorderColor(colorScheme)

    val shadowModifier = modifier.shadow(
        elevation = elevation,
        shape = shape,
        ambientColor = getShadowColor(),
        spotColor = getShadowColor()
    )

    when (variant) {
        CardVariant.FILLED -> {
            if (onClick != null) {
                Card(
                    onClick = onClick,
                    modifier = shadowModifier,
                    enabled = enabled,
                    shape = shape,
                    colors = colors,
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(contentPadding),
                        content = content
                    )
                }
            } else {
                Card(
                    modifier = shadowModifier,
                    shape = shape,
                    colors = colors,
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(contentPadding),
                        content = content
                    )
                }
            }
        }

        CardVariant.OUTLINED -> {
            if (onClick != null) {
                OutlinedCard(
                    onClick = onClick,
                    modifier = shadowModifier,
                    enabled = enabled,
                    shape = shape,
                    colors = colors,
                    border = BorderStroke(borderWidth, borderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(contentPadding),
                        content = content
                    )
                }
            } else {
                OutlinedCard(
                    modifier = shadowModifier,
                    shape = shape,
                    colors = colors,
                    border = BorderStroke(borderWidth, borderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(contentPadding),
                        content = content
                    )
                }
            }
        }

        CardVariant.ELEVATED -> {
            if (onClick != null) {
                ElevatedCard(
                    onClick = onClick,
                    modifier = shadowModifier,
                    enabled = enabled,
                    shape = shape,
                    colors = colors,
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation)
                ) {
                    Column(
                        modifier = Modifier.padding(contentPadding),
                        content = content
                    )
                }
            } else {
                ElevatedCard(
                    modifier = shadowModifier,
                    shape = shape,
                    colors = colors,
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = elevation)
                ) {
                    Column(
                        modifier = Modifier.padding(contentPadding),
                        content = content
                    )
                }
            }
        }
    }
}

/**
 * Header'lı Card - Başlık, ikon ve aksiyonlarla
 * headerStyle parametresi ile kompakt veya default stil seçilebilir
 */
@Composable
fun RepzoneCard(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    variant: CardVariant = CardVariant.FILLED,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    headerStyle: CardHeaderStyle = CardHeaderStyle.COMPACT,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    RepzoneCard(
        modifier = modifier,
        variant = variant,
        colorScheme = colorScheme,
        cornerRadius = cornerRadius,
        elevation = elevation,
        onClick = onClick,
        enabled = enabled,
        contentPadding = PaddingValues(0.dp)
    ) {
        // Header - Kompakt stil (BasketSection tarzı)
        if (headerStyle == CardHeaderStyle.COMPACT) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(getHeaderBackgroundColor())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Leading icon - Doğrudan, background'suz
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Trailing content
                if (trailingContent != null) {
                    trailingContent()
                }
            }
        } else {
            // Header - Default stil (eski büyük stil)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Leading icon with background
                if (leadingIcon != null) {
                    val iconColor = getIconColor(colorScheme)
                    val iconBgColor = getIconBackgroundColor(colorScheme)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = iconBgColor,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Trailing content
                if (trailingContent != null) {
                    trailingContent()
                }
            }

            // DEFAULT stil için header-content ayırıcı divider
            if (content != null) {
                HorizontalDivider(
                    color = getDividerColor()
                )
            }
        }

        // Content
        if (content != null) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

/**
 * Badge'li Card Header için yardımcı composable
 * BasketSection'daki sayı badge'i gibi kullanılabilir
 */
@Composable
fun CardBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    ) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/**
 * Navigasyon oklu Card
 */
@Composable
fun NavigationCard(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    headerStyle: CardHeaderStyle = CardHeaderStyle.COMPACT,
    onClick: () -> Unit
) {
    RepzoneCard(
        title = title,
        subtitle = subtitle,
        leadingIcon = leadingIcon,
        colorScheme = colorScheme,
        headerStyle = headerStyle,
        modifier = modifier,
        variant = CardVariant.OUTLINED,
        onClick = onClick,
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "İlerle",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

/**
 * Stat Card - İstatistik gösterimi için (Dashboard tarzı)
 */
@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    trend: String? = null,
    trendPositive: Boolean = true,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    onClick: (() -> Unit)? = null
) {
    RepzoneCard(
        modifier = modifier,
        variant = CardVariant.ELEVATED,
        colorScheme = colorScheme,
        cornerRadius = 16.dp,
        elevation = 4.dp,
        onClick = onClick
    ) {
        if (icon != null) {
            val iconColor = getIconColor(colorScheme)
            val iconBgColor = getIconBackgroundColor(colorScheme)

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = iconBgColor,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (trend != null) {
                Text(
                    text = trend,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = if (trendPositive) {
                        Color(0xFF10B981) // Yeşil
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

/**
 * Expandable Card - Açılır/kapanır içerik
 */
@Composable
fun ExpandableCard(
    title: String,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    variant: CardVariant = CardVariant.OUTLINED,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    headerStyle: CardHeaderStyle = CardHeaderStyle.COMPACT,
    content: @Composable ColumnScope.() -> Unit
) {
    RepzoneCard(
        modifier = modifier.animateContentSize(),
        variant = variant,
        colorScheme = colorScheme,
        cornerRadius = 12.dp,
        elevation = 2.dp,
        onClick = { onExpandChange(!expanded) },
        contentPadding = PaddingValues(0.dp)
    ) {
        // Header
        if (headerStyle == CardHeaderStyle.COMPACT) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(getHeaderBackgroundColor())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = if (expanded) "Kapat" else "Aç",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (leadingIcon != null) {
                    val iconColor = getIconColor(colorScheme)
                    val iconBgColor = getIconBackgroundColor(colorScheme)

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = iconBgColor,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = if (expanded) "Kapat" else "Aç",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Expandable content
        if (expanded) {
            HorizontalDivider(
                color = getDividerColor()
            )
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

// ============================================
// Helper Functions
// ============================================

@Composable
private fun getCardColors(
    colorScheme: CardColorScheme,
    variant: CardVariant
): CardColors {
    val isDark = isSystemInDarkTheme()

    val containerColor = when (colorScheme) {
        CardColorScheme.DEFAULT -> {
            if (isDark) {
                MaterialTheme.colorScheme.surfaceContainerHigh
            } else {
                MaterialTheme.colorScheme.surface
            }
        }
        CardColorScheme.PRIMARY -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.4f else 0.3f)
        CardColorScheme.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isDark) 0.4f else 0.3f)
        CardColorScheme.TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = if (isDark) 0.4f else 0.3f)
        CardColorScheme.ERROR -> MaterialTheme.colorScheme.errorContainer.copy(alpha = if (isDark) 0.4f else 0.3f)
        CardColorScheme.SUCCESS -> Color(0xFF10B981).copy(alpha = if (isDark) 0.2f else 0.1f)
        CardColorScheme.WARNING -> Color(0xFFF59E0B).copy(alpha = if (isDark) 0.2f else 0.1f)
    }

    return when (variant) {
        CardVariant.FILLED -> CardDefaults.cardColors(containerColor = containerColor)
        CardVariant.OUTLINED -> CardDefaults.outlinedCardColors(containerColor = containerColor)
        CardVariant.ELEVATED -> CardDefaults.elevatedCardColors(containerColor = containerColor)
    }
}

@Composable
private fun getBorderColor(colorScheme: CardColorScheme): Color {
    val isDark = isSystemInDarkTheme()

    return when (colorScheme) {
        CardColorScheme.DEFAULT -> {
            if (isDark) {
                // Dark mode'da çok daha belirgin border
                MaterialTheme.colorScheme.outlineVariant
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        }
        CardColorScheme.PRIMARY -> {
            val alpha = if (isDark) 0.8f else 0.5f
            MaterialTheme.colorScheme.primary.copy(alpha = alpha)
        }
        CardColorScheme.SECONDARY -> {
            val alpha = if (isDark) 0.8f else 0.5f
            MaterialTheme.colorScheme.secondary.copy(alpha = alpha)
        }
        CardColorScheme.TERTIARY -> {
            val alpha = if (isDark) 0.8f else 0.5f
            MaterialTheme.colorScheme.tertiary.copy(alpha = alpha)
        }
        CardColorScheme.ERROR -> {
            val alpha = if (isDark) 0.8f else 0.5f
            MaterialTheme.colorScheme.error.copy(alpha = alpha)
        }
        CardColorScheme.SUCCESS -> {
            val alpha = if (isDark) 0.8f else 0.5f
            Color(0xFF10B981).copy(alpha = alpha)
        }
        CardColorScheme.WARNING -> {
            val alpha = if (isDark) 0.8f else 0.5f
            Color(0xFFF59E0B).copy(alpha = alpha)
        }
    }
}

@Composable
private fun getDividerColor(): Color {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        MaterialTheme.colorScheme.outlineVariant
    } else {
        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }
}

@Composable
private fun getHeaderBackgroundColor(): Color {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    }
}

@Composable
private fun getShadowColor(): Color {
    val isDark = isSystemInDarkTheme()
    return if (isDark) {
        Color.Black.copy(alpha = 0.6f)
    } else {
        Color.Black.copy(alpha = 0.3f)
    }
}

@Composable
private fun getIconColor(colorScheme: CardColorScheme): Color {
    return when (colorScheme) {
        CardColorScheme.DEFAULT -> MaterialTheme.colorScheme.primary
        CardColorScheme.PRIMARY -> MaterialTheme.colorScheme.primary
        CardColorScheme.SECONDARY -> MaterialTheme.colorScheme.secondary
        CardColorScheme.TERTIARY -> MaterialTheme.colorScheme.tertiary
        CardColorScheme.ERROR -> MaterialTheme.colorScheme.error
        CardColorScheme.SUCCESS -> Color(0xFF10B981)
        CardColorScheme.WARNING -> Color(0xFFF59E0B)
    }
}

@Composable
private fun getIconBackgroundColor(colorScheme: CardColorScheme): Color {
    val isDark = isSystemInDarkTheme()
    val alpha = if (isDark) 0.3f else 0.5f

    return when (colorScheme) {
        CardColorScheme.DEFAULT -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha)
        CardColorScheme.PRIMARY -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.5f else 1f)
        CardColorScheme.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isDark) 0.5f else 1f)
        CardColorScheme.TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = if (isDark) 0.5f else 1f)
        CardColorScheme.ERROR -> MaterialTheme.colorScheme.errorContainer.copy(alpha = if (isDark) 0.5f else 1f)
        CardColorScheme.SUCCESS -> Color(0xFF10B981).copy(alpha = if (isDark) 0.25f else 0.15f)
        CardColorScheme.WARNING -> Color(0xFFF59E0B).copy(alpha = if (isDark) 0.25f else 0.15f)
    }
}