package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.VisitLogInformationEntity
import com.repzone.domain.model.VisitLogInformationModel

class VisitLogInformationEntityDbMapper : Mapper<VisitLogInformationEntity, VisitLogInformationModel> {
    //region Public Method
    override fun toDomain(from: VisitLogInformationEntity): VisitLogInformationModel {
        return VisitLogInformationModel(
            id = from.Id,
            customerName = from.CustomerName,
            recordDate = from.RecordDate,
            restServiceTaskId = from.RestServiceTaskId,
            routeAppointmentId = from.RouteAppointmentId,
            visitUniqueId = from.VisitUniqueId
        )
    }

    override fun fromDomain(domain: VisitLogInformationModel): VisitLogInformationEntity {
        return VisitLogInformationEntity(
            Id = domain.id,
            CustomerName = domain.customerName,
            RecordDate = domain.recordDate,
            RestServiceTaskId = domain.restServiceTaskId,
            RouteAppointmentId = domain.routeAppointmentId,
            VisitUniqueId = domain.visitUniqueId
        )
    }
    //endregion

}
