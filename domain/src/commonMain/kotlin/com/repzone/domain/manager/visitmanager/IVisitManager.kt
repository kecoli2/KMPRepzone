package com.repzone.domain.manager.visitmanager

import com.repzone.domain.common.Result
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.models.ActionButtonListItem
import com.repzone.domain.util.models.ActionMenuListItem

interface IVisitManager {
    suspend fun prepareVisitMenu(): Result<Pair<List<ActionMenuListItem>, List<ActionButtonListItem>>>
    suspend fun initiliaze(customer: CustomerItemModel): Result<Unit>
}