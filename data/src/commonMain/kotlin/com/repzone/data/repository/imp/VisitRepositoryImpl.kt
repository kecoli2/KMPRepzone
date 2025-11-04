package com.repzone.data.repository.imp

import com.repzone.data.mapper.VisitEntityDbMapper
import com.repzone.database.VisitEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.repository.IVisitRepository

class VisitRepositoryImpl(private val iDatabaseManager: IDatabaseManager, private val mapper: VisitEntityDbMapper): IVisitRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    //endregion

    //region Private Method
    //endregion
    override suspend fun getActiveVisit(): VisitInformation? {
        val visit = iDatabaseManager.getSqlDriver().select<VisitEntity> {
            where {
                criteria("Finish", isNull = true)
            }
        }.firstOrNull()

        return if(visit == null){
            null
        }else{
            mapper.toDomainVisitInformation(visit)
        }
    }
}