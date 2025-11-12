package com.repzone.domain.repository

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.model.VisitReasonInformation

interface IVisitRepository {
    suspend fun getActiveVisit(): VisitInformation?
    suspend fun hasAnyForm(guid: String, appointmentId: Long, formName: String): Boolean
    suspend fun startVisit(customerItemModel: CustomerItemModel, info: VisitReasonInformation? = null)
    suspend fun complateVisit(info: VisitReasonInformation? = null)
}