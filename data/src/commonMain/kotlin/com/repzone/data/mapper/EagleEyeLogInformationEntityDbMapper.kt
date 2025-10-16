package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.EagleEyeLogInformationEntity
import com.repzone.domain.model.EagleEyeLogInformationModel

class EagleEyeLogInformationEntityDbMapper : Mapper<EagleEyeLogInformationEntity, EagleEyeLogInformationModel> {
    //region Public Method
    override fun toDomain(from: EagleEyeLogInformationEntity): EagleEyeLogInformationModel {
        return EagleEyeLogInformationModel(
            id = from.Id,
            data = from.Data,
            locDate = from.LocDate,
            status = from.Status
        )
    }

    override fun fromDomain(domain: EagleEyeLogInformationModel): EagleEyeLogInformationEntity {
        return EagleEyeLogInformationEntity(
            Id = domain.id,
            Data = domain.data,
            LocDate = domain.locDate,
            Status = domain.status
        )
    }
    //endregion

}
