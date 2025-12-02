package com.repzone.core.ui.component.rowtemplate

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

/**
 * Default content padding for RepzoneRowItemTemplate
 */
val RepzoneRowItemDefaultPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)

/**
 * Image shape options for the leading image
 */
enum class ImageShapeType {
    CIRCLE,
    ROUNDED,
    SQUARE
}

/**
 * Badge configuration for the trailing badge
 */
data class BadgeConfig(
    val text: String,
    val icon: ImageVector? = null,
    val iconPosition: BadgeIconPosition = BadgeIconPosition.START,
    val backgroundColor: Color = Color(0xFFE8F5E9),
    val textColor: Color = Color(0xFF2E7D32),
    val iconTint: Color = Color(0xFF2E7D32)
)

enum class BadgeIconPosition {
    START,
    END
}

/**
 * Leading image configuration - supports both URL and Painter
 */
sealed class LeadingImageConfig {
    data class Url(
        val url: String,
        val placeholder: Painter? = null,
        val error: Painter? = null
    ) : LeadingImageConfig()

    data class Resource(
        val painter: Painter
    ) : LeadingImageConfig()

    data class Icon(
        val imageVector: ImageVector,
        val tint: Color = Color.Gray,
        val backgroundColor: Color = Color(0xFFF5F5F5)
    ) : LeadingImageConfig()
}

/**
 * RepzoneRowItemTemplate - A flexible row item component
 *
 * @param title Main title text
 * @param modifier Modifier for the row
 * @param subtitle Optional subtitle text
 * @param titleSuffix Optional text shown after the title (e.g., date, status)
 * @param titleSuffixColor Color for the title suffix
 * @param height Optional fixed height for the row (null = wrap content)
 * @param contentPadding Padding inside the row
 * @param leadingImage Image configuration (URL, Resource, or Icon)
 * @param leadingImageSize Size of the leading image
 * @param imageShapeType Shape type for the leading image (CIRCLE, ROUNDED, SQUARE)
 * @param imageCornerRadius Corner radius for ROUNDED shape type
 * @param badge Optional badge configuration (shown next to title)
 * @param trailingBadge Optional trailing badge configuration (shown at the end)
 * @param leadingContent Custom leading content (alternative to leadingImage)
 * @param trailingContent Custom trailing content (alternative to trailingBadge)
 * @param onClick Optional click handler
 */
@Composable
fun RepzoneRowItemTemplate(
    title: String,
    titleFontWeight : FontWeight = FontWeight.Normal,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    subTitleFontWeight : FontWeight = FontWeight.Light,
    titleSuffix: String? = null,
    titleSuffixColor: Color = Color(0xFF888888),
    height: Dp? = null,
    contentPadding: PaddingValues = RepzoneRowItemDefaultPadding,
    leadingImage: LeadingImageConfig? = null,
    leadingImageSize: Dp = 48.dp,
    imageShapeType: ImageShapeType = ImageShapeType.ROUNDED,
    imageCornerRadius: Dp = 8.dp,
    badge: BadgeConfig? = null,
    trailingBadge: BadgeConfig? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = modifier
        .fillMaxWidth()
        .then(
            if (height != null) Modifier.height(height)
            else Modifier
        )
        .then(
            if (onClick != null) Modifier.clickable { onClick() }
            else Modifier
        )
        .padding(contentPadding)

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Image
        if (leadingImage != null) {
            val imageShape = when (imageShapeType) {
                ImageShapeType.CIRCLE -> CircleShape
                ImageShapeType.ROUNDED -> RoundedCornerShape(imageCornerRadius)
                ImageShapeType.SQUARE -> RoundedCornerShape(0.dp)
            }

            when (leadingImage) {
                is LeadingImageConfig.Url -> {
                    AsyncImage(
                        model = leadingImage.url,
                        contentDescription = null,
                        modifier = Modifier
                            .size(leadingImageSize)
                            .clip(imageShape),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center,
                        error = leadingImage.error,
                        placeholder = leadingImage.placeholder
                    )
                }
                is LeadingImageConfig.Resource -> {
                    Image(
                        painter = leadingImage.painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(leadingImageSize)
                            .clip(imageShape),
                        contentScale = ContentScale.Crop
                    )
                }
                is LeadingImageConfig.Icon -> {
                    Icon(
                        imageVector = leadingImage.imageVector,
                        contentDescription = null,
                        modifier = Modifier
                            .size(leadingImageSize)
                            .clip(imageShape)
                            .background(leadingImage.backgroundColor)
                            .padding(8.dp),
                        tint = leadingImage.tint
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }

        if (leadingContent != null && leadingImage == null) {
            leadingContent()
            Spacer(modifier = Modifier.width(12.dp))
        }

        // Title & Subtitle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Title Row (Title + Badge + Suffix)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = titleFontWeight,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                // Inline Badge (next to title)
                if (badge != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    RowItemBadge(config = badge, compact = true)
                }

                // Title Suffix (e.g., "Bugün", "Yarın")
                if (titleSuffix != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = titleSuffix,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light,
                        color = titleSuffixColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Subtitle
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontWeight = subTitleFontWeight,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Trailing Badge
        if (trailingBadge != null) {
            Spacer(modifier = Modifier.width(8.dp))
            RowItemBadge(config = trailingBadge)
        }

        // Custom Trailing Content (alternative to trailingBadge)
        if (trailingContent != null && trailingBadge == null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingContent()
        }
    }
}

@Composable
private fun RowItemBadge(
    config: BadgeConfig,
    compact: Boolean = false
) {
    val horizontalPadding = if (compact) 6.dp else 10.dp
    val verticalPadding = if (compact) 2.dp else 6.dp
    val fontSize = if (compact) 10.sp else 12.sp

    Row(
        modifier = Modifier
            .background(
                color = config.backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (config.icon != null && config.iconPosition == BadgeIconPosition.START) {
            Icon(
                imageVector = config.icon,
                contentDescription = null,
                modifier = Modifier.size(if (compact) 10.dp else 14.dp),
                tint = config.iconTint
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = config.text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            color = config.textColor
        )

        if (config.icon != null && config.iconPosition == BadgeIconPosition.END) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = config.icon,
                contentDescription = null,
                modifier = Modifier.size(if (compact) 10.dp else 14.dp),
                tint = config.iconTint
            )
        }
    }
}