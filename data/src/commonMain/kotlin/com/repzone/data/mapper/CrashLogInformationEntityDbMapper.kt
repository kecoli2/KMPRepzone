package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.CrashLogInformationEntity
import com.repzone.domain.model.CrashLogInformationModel

class CrashLogInformationEntityDbMapper : Mapper<CrashLogInformationEntity, CrashLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: CrashLogInformationEntity): CrashLogInformationModel {
        return CrashLogInformationModel(
            id = from.Id,
            level = from.Level,
            logDate = from.LogDate,
            logMessage = from.LogMessage
        )
    }

    override fun fromDomain(domain: CrashLogInformationModel): CrashLogInformationEntity {
        return CrashLogInformationEntity(
            Id = domain.id,
            Level = domain.level,
            LogDate = domain.logDate,
            LogMessage = domain.logMessage
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
