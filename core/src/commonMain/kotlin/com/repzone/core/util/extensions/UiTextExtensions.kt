package com.repzone.core.util.extensions

import androidx.compose.runtime.Composable
import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.core.util.toComposeResource
import org.jetbrains.compose.resources.stringResource

/**
 * UiText'i Compose'da string'e çevir
 */
@Composable
fun UiText.fromResource(): String {
    return when (this) {
        is UiText.DynamicString -> value
        is UiText.StringResourceId -> {
            val resource = key.toComposeResource()
            when (args.size) {
                0 -> stringResource(resource)
                1 -> stringResource(resource, args[0])
                2 -> stringResource(resource, args[0], args[1])
                3 -> stringResource(resource, args[0], args[1], args[2])
                4 -> stringResource(resource, args[0], args[1], args[2], args[3])
                5 -> stringResource(resource, args[0], args[1], args[2], args[3],args[4])
                else -> stringResource(resource)
            }
        }
    }
}

/**
 * StringResource'u direkt kullan - argümansız
 */
@Composable
fun StringResource.fromResource(): String =
    stringResource(this.toComposeResource())

/**
 * StringResource'u direkt kullan - 1 argüman
 */
@Composable
fun StringResource.fromResource(arg1: Any): String =
    stringResource(this.toComposeResource(), arg1)

/**
 * StringResource'u direkt kullan - 2 argüman
 */
@Composable
fun StringResource.fromResource(arg1: Any, arg2: Any): String =
    stringResource(this.toComposeResource(), arg1, arg2)

/**
 * StringResource'u direkt kullan - 3 argüman
 */
@Composable
fun StringResource.fromResource(arg1: Any, arg2: Any, arg3: Any): String =
    stringResource(this.toComposeResource(), arg1, arg2, arg3)

/**
 * StringResource'u direkt kullan - 4 argüman
 */
@Composable
fun StringResource.fromResource(arg1: Any, arg2: Any, arg3: Any, arg4: Any): String =
    stringResource(this.toComposeResource(), arg1, arg2, arg3, arg4)

/**
 * StringResource'u direkt kullan - 5 argüman
 */
@Composable
fun StringResource.fromResource(arg1: Any, arg2: Any, arg3: Any, arg4: Any, arg5: Any): String =
    stringResource(this.toComposeResource(), arg1, arg2, arg3, arg4, arg5)