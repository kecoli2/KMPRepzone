package com.repzone.data.repository.imp

import com.repzone.data.mapper.RouteInformationViewEntityDbMapper
import com.repzone.data.util.Mapper
import com.repzone.database.RouteInformationViewEntity
import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.rawQueryToEntity
import com.repzone.database.runtime.select
import com.repzone.domain.model.RouteInformationModel
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.util.models.SprintInformation

class RouteAppointmentRepositoryImpl(
    private val databaseManager: IDatabaseManager,
    private val mapper: RouteInformationViewEntityDbMapper

): IRouteAppointmentRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getActiveSprintInformation(): SprintInformation {
        var maxId = 0
        var minRec: Long? = 0L
        var maxRec: Long? = 0L
        val list = databaseManager.getSqlDriver().rawQueryToEntity<SyncRouteAppointmentEntity>("SELECT *\n" +
                "FROM SyncRouteAppointmentEntity\n" +
                "WHERE SprintId = (SELECT MAX(SprintId) FROM SyncRouteAppointmentEntity)\n" +
                "ORDER BY StartDate ASC;")

        if(list.isNotEmpty()){
            maxId = list.first().SprintId?.toInt() ?: 0
            minRec =  list.firstOrNull()?.StartDate
            maxRec = list.lastOrNull()?.EndDate
        }


        return SprintInformation(
            id = maxId,
            startDate = minRec ?: 0L,
            endDate = maxRec ?: 0L
        )

    }


    override suspend fun getRouteInformation(appointmentId: Long): RouteInformationModel? {
        val db = databaseManager.getSqlDriver().select<RouteInformationViewEntity> {
            where {
                criteria("AppointmentId", appointmentId)
            }
        }.firstOrNull()

        return if (db == null){
            null
        }else{
            mapper.toDomain(db)
        }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}