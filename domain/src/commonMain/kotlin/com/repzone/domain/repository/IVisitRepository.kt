package com.repzone.domain.repository

import com.repzone.domain.model.VisitInformation

interface IVisitRepository {
    suspend fun getActiveVisit(): VisitInformation?
    suspend fun hasAnyForm(guid: String, appointmentId: Long, formName: String): Boolean
}