package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.VisitEntity
import com.repzone.domain.model.VisitModel

class VisitEntityDbMapper : Mapper<VisitEntity, VisitModel> {
    //region Public Method
    override fun toDomain(from: VisitEntity): VisitModel {
        return VisitModel(
            id = from.Id,
            appointmentExceptionId = from.AppointmentExceptionId,
            appointmentId = from.AppointmentId,
            distanceFromCustomerLocation = from.DistanceFromCustomerLocation,
            finish = from.Finish,
            formIdsRaw = from.FormIdsRaw,
            guid = from.Guid,
            isItOnLocation = from.IsItOnLocation,
            isItOnRoute = from.IsItOnRoute,
            isItOnTime = from.IsItOnTime,
            latitude = from.Latitude,
            longitude = from.Longitude,
            orderIdsRaw = from.OrderIdsRaw,
            selectedCustomerOrganizationId = from.SelectedCustomerOrganizationId,
            start = from.Start,
            visitNote = from.VisitNote,
            visitType = from.VisitType
        )
    }

    override fun fromDomain(domain: VisitModel): VisitEntity {
        return VisitEntity(
            Id = domain.id,
            AppointmentExceptionId = domain.appointmentExceptionId,
            AppointmentId = domain.appointmentId,
            DistanceFromCustomerLocation = domain.distanceFromCustomerLocation,
            Finish = domain.finish,
            FormIdsRaw = domain.formIdsRaw,
            Guid = domain.guid,
            IsItOnLocation = domain.isItOnLocation,
            IsItOnRoute = domain.isItOnRoute,
            IsItOnTime = domain.isItOnTime,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            OrderIdsRaw = domain.orderIdsRaw,
            SelectedCustomerOrganizationId = domain.selectedCustomerOrganizationId,
            Start = domain.start,
            VisitNote = domain.visitNote,
            VisitType = domain.visitType
        )
    }
    //endregion

}
