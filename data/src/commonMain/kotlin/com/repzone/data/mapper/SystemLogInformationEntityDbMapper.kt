package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SystemLogInformationEntity
import com.repzone.domain.model.SystemLogInformationModel

class SystemLogInformationEntityDbMapper : Mapper<SystemLogInformationEntity, SystemLogInformationModel> {
    //region Public Method
    override fun toDomain(from: SystemLogInformationEntity): SystemLogInformationModel {
        return SystemLogInformationModel(
            id = from.Id,
            message = from.Message,
            recordDate = from.RecordDate,
            region = from.Region
        )
    }

    override fun fromDomain(domain: SystemLogInformationModel): SystemLogInformationEntity {
        return SystemLogInformationEntity(
            Id = domain.id,
            Message = domain.message,
            RecordDate = domain.recordDate,
            Region = domain.region
        )
    }
    //endregion

}
