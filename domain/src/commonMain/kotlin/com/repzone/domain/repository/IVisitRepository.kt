package com.repzone.domain.repository

import com.repzone.domain.model.VisitInformation

interface IVisitRepository {
    suspend fun getActiveVisit(): VisitInformation?
}