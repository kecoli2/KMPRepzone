package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DailyOperationLogInformationEntity
import com.repzone.domain.model.DailyOperationLogInformationModel

class DailyOperationLogInformationEntityDbMapper : Mapper<DailyOperationLogInformationEntity, DailyOperationLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: DailyOperationLogInformationEntity): DailyOperationLogInformationModel {
        return DailyOperationLogInformationModel(
            id = from.Id,
            batteryLevel = from.BatteryLevel,
            date = from.Date,
            description = from.Description,
            localTime = from.LocalTime,
            repzoneLeaveRequestId = from.RepzoneLeaveRequestId,
            repzoneLeaveRequestUniqueId = from.RepzoneLeaveRequestUniqueId,
            type = from.Type
        )
    }

    override fun fromDomain(domain: DailyOperationLogInformationModel): DailyOperationLogInformationEntity {
        return DailyOperationLogInformationEntity(
            Id = domain.id,
            BatteryLevel = domain.batteryLevel,
            Date = domain.date,
            Description = domain.description,
            LocalTime = domain.localTime,
            RepzoneLeaveRequestId = domain.repzoneLeaveRequestId,
            RepzoneLeaveRequestUniqueId = domain.repzoneLeaveRequestUniqueId,
            Type = domain.type
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
