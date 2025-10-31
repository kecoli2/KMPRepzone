package com.repzone.domain.repository

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.domain.util.models.ActionMenuListItem

interface IActionMenuRepository {
    suspend fun getActionMenuList(customer: CustomerItemModel): List<ActionMenuListItem>
    suspend fun getActionButtonList(customer: CustomerItemModel): List<ActionButtonListItem>
}