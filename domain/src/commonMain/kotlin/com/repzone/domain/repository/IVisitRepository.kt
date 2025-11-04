package com.repzone.domain.repository

import com.repzone.domain.model.VisitModel

interface IVisitRepository {
    suspend fun getActiveVisit(): VisitModel?
}