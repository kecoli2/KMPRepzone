package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.HotSaleDailyInformationEntity
import com.repzone.domain.model.HotSaleDailyInformationModel

class HotSaleDailyInformationEntityDbMapper : Mapper<HotSaleDailyInformationEntity, HotSaleDailyInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: HotSaleDailyInformationEntity): HotSaleDailyInformationModel {
        return HotSaleDailyInformationModel(
            id = from.Id,
            operationDay = from.OperationDay,
            operationTimeStamp = from.OperationTimeStamp,
            operationType = from.OperationType
        )
    }

    override fun fromDomain(domain: HotSaleDailyInformationModel): HotSaleDailyInformationEntity {
        return HotSaleDailyInformationEntity(
            Id = domain.id,
            OperationDay = domain.operationDay,
            OperationTimeStamp = domain.operationTimeStamp,
            OperationType = domain.operationType
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
