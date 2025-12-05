package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.domain.model.SyncRouteAppointmentModel
import com.repzone.network.dto.RouteDto
import kotlin.time.ExperimentalTime

class SyncRouteAppointmentEntityDbMapper: MapperDto<SyncRouteAppointmentEntity, SyncRouteAppointmentModel, RouteDto> {
    //region Public Method
    override fun toDomain(from: SyncRouteAppointmentEntity): SyncRouteAppointmentModel {
        return SyncRouteAppointmentModel(
            id = from.Id,
            customerId = from.CustomerId,
            description = from.Description,
            endDate = from.EndDate,
            sprintId = from.SprintId,
            startDate = from.StartDate,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncRouteAppointmentModel): SyncRouteAppointmentEntity {
        return SyncRouteAppointmentEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            Description = domain.description,
            EndDate = domain.endDate,
            SprintId = domain.sprintId,
            StartDate = domain.startDate,
            State = domain.state.enumToLong()
        )
    }

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: RouteDto): SyncRouteAppointmentEntity {
        return SyncRouteAppointmentEntity(
            Id = dto.id.toLong(),
            CustomerId = dto.customerId.toLong(),
            Description = dto.description,
            EndDate = dto.appointmentEnd?.toEpochMilliseconds(),
            SprintId = dto.sprintId.toLong(),
            StartDate = dto.appointmentDue?.toEpochMilliseconds(),
            State = dto.state.toLong()
        )
    }


    //endregion

}