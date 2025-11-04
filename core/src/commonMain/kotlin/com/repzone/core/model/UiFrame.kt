package com.repzone.core.model

import androidx.compose.runtime.Composable
import com.repzone.core.util.extensions.fromResource
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface HasUiFrame {
    val uiFrame: UiFrame
    fun copyWithUiFrame(newUiFrame: UiFrame): HasUiFrame
}

// UiFrame data class
@OptIn(ExperimentalTime::class)
data class UiFrame(val isLoading: Boolean = false, val error: String? = null, val domainException: Any? = null, val errorStringRes: StringResource? = null, private val formatArgs: List<Any> = emptyList()) {

    @Composable
    fun getError(): String? {
        error?.let { return it }
        val res = errorStringRes ?: return null
        val safeArgs: Array<Any> = formatArgs.map { it.normalizeForFormatting() }.toTypedArray()
        return if (safeArgs.isNotEmpty())
            stringResource(res, *safeArgs)
        else
            res.fromResource()

    }
    val isInteractionEnabled: Boolean get() = !isLoading

    private fun Any?.normalizeForFormatting(): Any = when (this) {
        null -> ""
        is String, is Number, is Boolean -> this
        is Enum<*> -> this.name
        is Instant -> this.toLocalText()
        is Throwable -> (this.message ?: this::class.simpleName ?: "Error")
        else -> {
            this.toString()
        }
    }

    private fun Instant.toLocalText(tz: TimeZone = TimeZone.currentSystemDefault()): String {
        val dt = this.toLocalDateTime(tz)
        return buildString {
            append(dt.year.toString().padStart(4, '0'))
            append('-')
            append(dt.month.number.toString().padStart(2, '0'))
            append('-')
            append(dt.day.toString().padStart(2, '0'))
            append(' ')
            append(dt.hour.toString().padStart(2, '0'))
            append(':')
            append(dt.minute.toString().padStart(2, '0'))
        }
    }

}