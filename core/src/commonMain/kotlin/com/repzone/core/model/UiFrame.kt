package com.repzone.core.model

import androidx.compose.runtime.Composable
import com.repzone.core.util.extensions.fromResource
import org.jetbrains.compose.resources.StringResource

interface HasUiFrame {
    val uiFrame: UiFrame
    fun copyWithUiFrame(newUiFrame: UiFrame): HasUiFrame
}

// UiFrame data class
data class UiFrame(val isLoading: Boolean = false, val error: String? = null, val errorStringRes: StringResource? = null, private val formatArgs: List<Any?> = emptyList()) {

    @Composable
    fun getError(): String? {
        if(error != null) return error
        if(errorStringRes != null && formatArgs.isNotEmpty()) return errorStringRes.fromResource(formatArgs.toTypedArray())
        if(errorStringRes != null) return errorStringRes.fromResource()
        return null
    }
    val isInteractionEnabled: Boolean get() = !isLoading
}