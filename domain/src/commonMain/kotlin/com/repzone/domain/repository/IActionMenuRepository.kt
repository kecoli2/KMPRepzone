package com.repzone.domain.repository

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.models.ActionMenuListItem

interface IActionMenuRepository {
    suspend fun getActionMenuList(customer: CustomerItemModel): List<ActionMenuListItem>
}