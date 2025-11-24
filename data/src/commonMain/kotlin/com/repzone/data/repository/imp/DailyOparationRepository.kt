package com.repzone.data.repository.imp

import com.repzone.data.mapper.DailyOperationLogInformationEntityDbMapper
import com.repzone.database.DailyOperationLogInformationEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.insert
import com.repzone.database.runtime.select
import com.repzone.domain.model.DailyOperationLogInformationModel
import com.repzone.domain.repository.IDailyOparationRepository

class DailyOparationRepository(private val iDatabaseManager: IDatabaseManager, private val mapper: DailyOperationLogInformationEntityDbMapper): IDailyOparationRepository {
    //region Field
    //endregion

    //region Public Method
    override suspend fun getLasLog(): DailyOperationLogInformationModel? {
        val ss = iDatabaseManager.getSqlDriver().select<DailyOperationLogInformationEntity>{
            orderBy {
                order("Id", true)
            }
            limit(1)
        }.firstOrNull()?.let {
            mapper.toDomain(it)
        }
        return ss
    }

    override suspend fun audit(log: DailyOperationLogInformationModel) {
        val entity = mapper.fromDomain(log)
        iDatabaseManager.getSqlDriver().insert(entity)
    }
    //endregion
}