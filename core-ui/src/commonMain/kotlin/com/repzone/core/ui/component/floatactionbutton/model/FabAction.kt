package com.repzone.core.ui.component.floatactionbutton.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.repzone.core.model.UiText

sealed class FabAction {
    abstract val icon: ImageVector
    abstract val contentDescription: String
    data class Single(
        override val icon: ImageVector,
        override val contentDescription: String,
        val badgeCount: Int? = null,
        val badgeColor: Color? = null,
        val badgeContentColor: Color? = null
    ) : FabAction()

    data class Multiple(
        override val icon: ImageVector,
        override val contentDescription: String,
        val items: List<FabMenuItem>,
    ) : FabAction()
}

data class FabMenuItem(
    val type: FabMenuItemType,
    val icon: ImageVector,
    val label: UiText,
    val typeId: String? = null
)