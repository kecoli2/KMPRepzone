package com.repzone.core.ui.component.floatactionbutton.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed class FabAction {
    abstract val icon: ImageVector
    abstract val contentDescription: String

    data class Single(
        override val icon: ImageVector,
        override val contentDescription: String
    ) : FabAction()

    data class Multiple(
        override val icon: ImageVector,
        override val contentDescription: String,
        val items: List<FabMenuItem>
    ) : FabAction()
}

data class FabMenuItem(
    val type: FabMenuItemType,
    val icon: ImageVector,
    val label: String,
    val typeId: String? = null
)