package com.repzone.domain.manager.visitmanager

import com.repzone.domain.common.Result
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.models.VisitButtonItem
import com.repzone.domain.util.models.VisitActionItem

interface IVisitManager {
    suspend fun prepareVisitMenu(): Result<Pair<List<VisitActionItem>, List<VisitButtonItem>>>
    suspend fun initiliaze(customer: CustomerItemModel): Result<Unit>
}