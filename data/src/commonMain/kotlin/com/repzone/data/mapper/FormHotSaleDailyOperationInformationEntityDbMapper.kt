package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.FormHotSaleDailyOperationInformationEntity
import com.repzone.domain.model.FormHotSaleDailyOperationInformationModel

class FormHotSaleDailyOperationInformationEntityDbMapper : Mapper<FormHotSaleDailyOperationInformationEntity, FormHotSaleDailyOperationInformationModel> {
    //region Public Method
    override fun toDomain(from: FormHotSaleDailyOperationInformationEntity): FormHotSaleDailyOperationInformationModel {
        return FormHotSaleDailyOperationInformationModel(
            id = from.Id,
            formId = from.FormId,
            hotSaleDailyInformationId = from.HotSaleDailyInformationId,
            logTimeStamp = from.LogTimeStamp,
            operationDay = from.OperationDay
        )
    }

    override fun fromDomain(domain: FormHotSaleDailyOperationInformationModel): FormHotSaleDailyOperationInformationEntity {
        return FormHotSaleDailyOperationInformationEntity(
            Id = domain.id,
            FormId = domain.formId,
            HotSaleDailyInformationId = domain.hotSaleDailyInformationId,
            LogTimeStamp = domain.logTimeStamp,
            OperationDay = domain.operationDay
        )
    }
    //endregion

}
