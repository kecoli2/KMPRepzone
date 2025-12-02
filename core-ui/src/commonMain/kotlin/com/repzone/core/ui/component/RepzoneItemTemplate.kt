package com.repzone.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * RepzoneRowItemTemplate - A flexible row item component
 *
 * @param title Main title text
 * @param subtitle Optional subtitle text
 * @param modifier Modifier for the row
 * @param leadingImage Optional leading image painter
 * @param leadingImageSize Size of the leading image
 * @param imageShapeType Shape type for the leading image (CIRCLE, ROUNDED, SQUARE)
 * @param imageCornerRadius Corner radius for ROUNDED shape type
 * @param badge Optional badge configuration
 * @param onClick Optional click handler
 */
@Composable
fun RepzoneRowItemTemplate(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingImage: Painter? = null,
    leadingImageSize: Dp = 48.dp,
    imageShapeType: ImageShapeType = ImageShapeType.ROUNDED,
    imageCornerRadius: Dp = 8.dp,
    badge: BadgeConfig? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = if (onClick != null) {
        modifier.fillMaxWidth()
    } else {
        modifier.fillMaxWidth()
    }

    Row(
        modifier = rowModifier
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Image
        if (leadingImage != null) {
            val imageShape = when (imageShapeType) {
                ImageShapeType.CIRCLE -> CircleShape
                ImageShapeType.ROUNDED -> RoundedCornerShape(imageCornerRadius)
                ImageShapeType.SQUARE -> RoundedCornerShape(0.dp)
            }

            Image(
                painter = leadingImage,
                contentDescription = null,
                modifier = Modifier
                    .size(leadingImageSize)
                    .clip(imageShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))
        }

        // Custom Leading Content (alternative to image)
        if (leadingContent != null && leadingImage == null) {
            leadingContent()
            Spacer(modifier = Modifier.width(12.dp))
        }

        // Title & Subtitle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF666666),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Badge
        if (badge != null) {
            Spacer(modifier = Modifier.width(8.dp))
            RowItemBadge(config = badge)
        }

        // Custom Trailing Content (alternative to badge)
        if (trailingContent != null && badge == null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailingContent()
        }
    }
}

@Composable
private fun RowItemBadge(
    config: BadgeConfig
) {
    Row(
        modifier = Modifier
            .background(
                color = config.backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (config.icon != null && config.iconPosition == BadgeIconPosition.START) {
            Icon(
                imageVector = config.icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = config.iconTint
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = config.text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = config.textColor
        )

        if (config.icon != null && config.iconPosition == BadgeIconPosition.END) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = config.icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = config.iconTint
            )
        }
    }
}