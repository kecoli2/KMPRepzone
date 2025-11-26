package com.repzone.domain.repository

import com.repzone.core.enums.DocumentActionType
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.pipline.model.pipline.Pipeline
import com.repzone.domain.util.models.VisitActionItem

interface IPipelineRepository {
    fun getPipelineForAction(actionType: DocumentActionType): Pipeline
    fun getStartVisit(customerItemModel: CustomerItemModel): Pipeline
    fun getFinishVisit(customerItemModel: CustomerItemModel): Pipeline

    fun getOrders(customerItemModel: CustomerItemModel, visitActionItem: VisitActionItem): Pipeline


}