package com.repzone.domain.util.models

import com.repzone.domain.util.enums.ActionButtonType

data class VisitButtonItem(
    val actionType: ActionButtonType,
    val badgeCount: Int = 0)
