package com.repzone.core.ui.component.card

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
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
 * Genel amaçlı Card componenti
 */
@Composable
fun RepzoneCard(
    modifier: Modifier = Modifier,
    variant: CardVariant = CardVariant.FILLED,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 1.dp,
    borderWidth: Dp = 1.dp,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val colors = getCardColors(colorScheme, variant)

    when (variant) {
        CardVariant.FILLED -> {
            if (onClick != null) {
                Card(
                    onClick = onClick,
                    modifier = modifier,
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
                    modifier = modifier,
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
            val borderColor = getBorderColor(colorScheme)
            if (onClick != null) {
                OutlinedCard(
                    onClick = onClick,
                    modifier = modifier,
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
                    modifier = modifier,
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
                    modifier = modifier,
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
                    modifier = modifier,
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
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 1.dp,
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
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Leading icon
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
            } else if (onClick != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Content
        if (content != null) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

/**
 * Info Card - Bilgi gösterimi için
 */
@Composable
fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colorScheme: CardColorScheme = CardColorScheme.DEFAULT,
    valueColor: Color = MaterialTheme.colorScheme.primary,
    onClick: (() -> Unit)? = null
) {
    RepzoneCard(
        modifier = modifier,
        variant = CardVariant.FILLED,
        colorScheme = colorScheme,
        cornerRadius = 12.dp,
        onClick = onClick,
        contentPadding = PaddingValues(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = valueColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            }
        }
    }
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
        elevation = 2.dp,
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
    content: @Composable ColumnScope.() -> Unit
) {
    RepzoneCard(
        modifier = modifier.animateContentSize(),
        variant = variant,
        colorScheme = colorScheme,
        cornerRadius = 12.dp,
        onClick = { onExpandChange(!expanded) },
        contentPadding = PaddingValues(0.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    style = MaterialTheme.typography.titleSmall,
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

        // Expandable content
        if (expanded) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
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
    val containerColor = when (colorScheme) {
        CardColorScheme.DEFAULT -> MaterialTheme.colorScheme.surface
        CardColorScheme.PRIMARY -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        CardColorScheme.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        CardColorScheme.TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        CardColorScheme.ERROR -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        CardColorScheme.SUCCESS -> Color(0xFF10B981).copy(alpha = 0.1f)
        CardColorScheme.WARNING -> Color(0xFFF59E0B).copy(alpha = 0.1f)
    }

    return when (variant) {
        CardVariant.FILLED -> CardDefaults.cardColors(containerColor = containerColor)
        CardVariant.OUTLINED -> CardDefaults.outlinedCardColors(containerColor = containerColor)
        CardVariant.ELEVATED -> CardDefaults.elevatedCardColors(containerColor = containerColor)
    }
}

@Composable
private fun getBorderColor(colorScheme: CardColorScheme): Color {
    return when (colorScheme) {
        CardColorScheme.DEFAULT -> MaterialTheme.colorScheme.outlineVariant
        CardColorScheme.PRIMARY -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        CardColorScheme.SECONDARY -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        CardColorScheme.TERTIARY -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
        CardColorScheme.ERROR -> MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
        CardColorScheme.SUCCESS -> Color(0xFF10B981).copy(alpha = 0.3f)
        CardColorScheme.WARNING -> Color(0xFFF59E0B).copy(alpha = 0.3f)
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
    return when (colorScheme) {
        CardColorScheme.DEFAULT -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        CardColorScheme.PRIMARY -> MaterialTheme.colorScheme.primaryContainer
        CardColorScheme.SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
        CardColorScheme.TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer
        CardColorScheme.ERROR -> MaterialTheme.colorScheme.errorContainer
        CardColorScheme.SUCCESS -> Color(0xFF10B981).copy(alpha = 0.15f)
        CardColorScheme.WARNING -> Color(0xFFF59E0B).copy(alpha = 0.15f)
    }
}