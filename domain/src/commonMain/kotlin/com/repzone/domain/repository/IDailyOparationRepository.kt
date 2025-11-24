package com.repzone.domain.repository

import com.repzone.domain.model.DailyOperationLogInformationModel

interface IDailyOparationRepository {
    suspend fun getLasLog(): DailyOperationLogInformationModel?
    suspend fun audit(log: DailyOperationLogInformationModel)
}