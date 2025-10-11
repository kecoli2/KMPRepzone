package com.repzone.core.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import com.repzone.core.platform.isInPreview
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun String.quote() = "'${replace("'", "''")}'"

/**
 * Preview'da HİÇBİR şekilde stringResource çağırmaz.
 * Gerçekte normal yoldan okur.
 */
@Composable
fun stringResourceOr(res: StringResource): String {
    return if (isInPreview()) res.key else stringResource(res)
}

@Composable
fun painterResourceOr(res: DrawableResource, fallback: Painter = ColorPainter(Color.Gray)): Painter {
    return if (isInPreview()) fallback else painterResource(res)
}