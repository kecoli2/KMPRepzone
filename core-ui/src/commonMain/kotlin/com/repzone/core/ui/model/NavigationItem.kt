package com.repzone.core.ui.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.repzone.core.ui.util.enum.NavigationItemType

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val navigationItemType: NavigationItemType,
    val badgeCount: Int? = null
) {
}