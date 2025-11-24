package com.repzone.data.mapper

import com.repzone.core.enums.DailyOperationType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.getLocalDateTime
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toEpochMillis
import com.repzone.core.util.extensions.toInstant
import com.repzone.data.util.Mapper
import com.repzone.database.DailyOperationLogInformationEntity
import com.repzone.domain.model.DailyOperationLogInformationModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class DailyOperationLogInformationEntityDbMapper : Mapper<DailyOperationLogInformationEntity, DailyOperationLogInformationModel> {
    //region Public Method
    override fun toDomain(from: DailyOperationLogInformationEntity): DailyOperationLogInformationModel {
        return DailyOperationLogInformationModel(
            id = from.Id,
            batteryLevel = from.BatteryLevel,
            date = from.Date?.toInstant(),
            description = from.Description,
            localTime = from.LocalTime?.toInstant(),
            repzoneLeaveRequestId = from.RepzoneLeaveRequestId,
            repzoneLeaveRequestUniqueId = from.RepzoneLeaveRequestUniqueId,
            type = from.Type?.toEnum<DailyOperationType>()?: DailyOperationType.CONTINUE
        )
    }

    override fun fromDomain(domain: DailyOperationLogInformationModel): DailyOperationLogInformationEntity {
        return DailyOperationLogInformationEntity(
            Id = domain.id,
            BatteryLevel = domain.batteryLevel,
            Date = domain.date?.toEpochMilliseconds(),
            Description = domain.description,
            LocalTime = domain.localTime?.getLocalDateTime()?.toEpochMillis(),
            RepzoneLeaveRequestId = domain.repzoneLeaveRequestId,
            RepzoneLeaveRequestUniqueId = domain.repzoneLeaveRequestUniqueId,
            Type = domain.type.enumToLong()
        )
    }
    //endregion

}
