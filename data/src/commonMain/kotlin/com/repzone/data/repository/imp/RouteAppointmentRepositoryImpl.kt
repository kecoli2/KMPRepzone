package com.repzone.data.repository.imp

import com.repzone.database.AppDatabase
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.util.models.SprintInformation

class RouteAppointmentRepositoryImpl(private val database: AppDatabase): IRouteAppointmentRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getActiveSprintInformation(): SprintInformation {
        var maxId = 0
        var minRec: Long? = 0L
        var maxRec: Long? = 0L
        val list = database.syncRouteAppointmentEntityQueries.selectByMaxSprintIdOrderStartDate().executeAsList()

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
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}