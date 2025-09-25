package com.repzone.core.model

interface HasUiFrame {
    val uiFrame: UiFrame
    fun copyWithUiFrame(newUiFrame: UiFrame): HasUiFrame
}

// UiFrame data class
data class UiFrame(val isLoading: Boolean = false, val error: String? = null) {
    val isInteractionEnabled: Boolean get() = !isLoading
}