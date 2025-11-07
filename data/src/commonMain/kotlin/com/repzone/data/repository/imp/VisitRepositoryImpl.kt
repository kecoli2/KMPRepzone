package com.repzone.data.repository.imp

import com.repzone.data.mapper.VisitEntityDbMapper
import com.repzone.database.VisitEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.count
import com.repzone.database.runtime.select
import com.repzone.database.runtime.selectAsFlow
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.repository.IVisitRepository

class VisitRepositoryImpl(private val iDatabaseManager: IDatabaseManager, private val mapper: VisitEntityDbMapper): IVisitRepository {

    //region Public Method
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

    override suspend fun hasAnyForm(guid: String, appointmentId: Long, formName: String): Boolean {
        return iDatabaseManager.getSqlDriver().count<VisitEntity> {
            where {
                criteria("Guid", guid)
                criteria("AppointmentId", appointmentId)
                criteria("FormIdsRaw", isNull = false)
                criteria("FormIdsRaw", like = formName )
                criteria("Finish", isNull = true)
            }
        } > 0
    }
    //endregion

    //region Private Method
    //endregion

}