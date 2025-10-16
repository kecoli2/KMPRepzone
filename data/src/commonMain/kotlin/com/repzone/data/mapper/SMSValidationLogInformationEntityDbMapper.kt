package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SMSValidationLogInformationEntity
import com.repzone.domain.model.SMSValidationLogInformationModel

class SMSValidationLogInformationEntityDbMapper : Mapper<SMSValidationLogInformationEntity, SMSValidationLogInformationModel> {
    //region Public Method
    override fun toDomain(from: SMSValidationLogInformationEntity): SMSValidationLogInformationModel {
        return SMSValidationLogInformationModel(
            id = from.Id,
            data = from.Data,
            requestTime = from.RequestTime,
            responseTime = from.ResponseTime,
            result = from.Result,
            success = from.Success,
            verified = from.Verified
        )
    }

    override fun fromDomain(domain: SMSValidationLogInformationModel): SMSValidationLogInformationEntity {
        return SMSValidationLogInformationEntity(
            Id = domain.id,
            Data = domain.data,
            RequestTime = domain.requestTime,
            ResponseTime = domain.responseTime,
            Result = domain.result,
            Success = domain.success,
            Verified = domain.verified
        )
    }
    //endregion

}
