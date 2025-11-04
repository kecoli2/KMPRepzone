package com.repzone.data.mapper

import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toInstant
import com.repzone.data.util.Mapper
import com.repzone.database.VisitEntity
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.model.VisitModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class VisitEntityDbMapper : Mapper<VisitEntity, VisitModel> {
    //region Public Method
    override fun toDomain(from: VisitEntity): VisitModel {
        return VisitModel(
            id = from.Id,
            appointmentExceptionId = from.AppointmentExceptionId,
            appointmentId = from.AppointmentId,
            distanceFromCustomerLocation = from.DistanceFromCustomerLocation,
            finish = from.Finish?.toInstant(),
            formIdsRaw = from.FormIdsRaw,
            guid = from.Guid,
            isItOnLocation = from.IsItOnLocation,
            isItOnRoute = from.IsItOnRoute,
            isItOnTime = from.IsItOnTime,
            latitude = from.Latitude,
            longitude = from.Longitude,
            orderIdsRaw = from.OrderIdsRaw,
            selectedCustomerOrganizationId = from.SelectedCustomerOrganizationId,
            start = from.Start?.toInstant(),
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
            Finish = domain.finish?.toEpochMilliseconds(),
            FormIdsRaw = domain.formIdsRaw,
            Guid = domain.guid,
            IsItOnLocation = domain.isItOnLocation,
            IsItOnRoute = domain.isItOnRoute,
            IsItOnTime = domain.isItOnTime,
            Latitude = domain.latitude,
            Longitude = domain.longitude,
            OrderIdsRaw = domain.orderIdsRaw,
            SelectedCustomerOrganizationId = domain.selectedCustomerOrganizationId,
            Start = domain.start?.toEpochMilliseconds(),
            VisitNote = domain.visitNote,
            VisitType = domain.visitType
        )
    }

    fun toDomainVisitInformation(from: VisitEntity): VisitInformation{
        return VisitInformation(
            appointmentId = from.AppointmentId ?: 0L,
            visitId = from.Id,
            guid = from.Guid,
            longitude = from.Longitude,
            latitude = from.Latitude,
            start = from.Start?.toInstant(),
            orderIds = from.OrderIdsRaw,
            formIds = from.FormIdsRaw,
            isItOnRoute = from.IsItOnRoute?.toBoolean() ?: false,
            isItOnTime = from.IsItOnTime?.toBoolean() ?: false,
            isItOnLocation = from.IsItOnLocation?.toBoolean() ?: false,
            distanceFromCustomerLocation = from.DistanceFromCustomerLocation?.toInt() ?: 0,
            visitType = from.VisitType?.toInt() ?: 0,
            selectedCustomerOrganizationId = from.SelectedCustomerOrganizationId?.toInt() ?: 0
        )
    }
    //endregion

}
