package com.repzone.domain.util.models

import com.repzone.domain.util.enums.ActionButtonType

data class ActionButtonListItem(
    val actionType: ActionButtonType,
    val badgeCount: Int = 0)
