package com.repzone.domain.util.models

import com.repzone.domain.util.enums.ActionMenuGroup
import com.repzone.domain.util.enums.ActionType

data class ActionMenuListItem(
    val actionType: ActionType,
    val title: String,
    val subTitle: String,
    val groupType: ActionMenuGroup,
    val order: Int? = 0
    )