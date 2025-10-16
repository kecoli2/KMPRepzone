package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.VisitActivityLogInformationEntity
import com.repzone.domain.model.VisitActivityLogInformationModel

class VisitActivityLogInformationEntityDbMapper : Mapper<VisitActivityLogInformationEntity, VisitActivityLogInformationModel> {
    //region Public Method
    override fun toDomain(from: VisitActivityLogInformationEntity): VisitActivityLogInformationModel {
        return VisitActivityLogInformationModel(
            id = from.Id,
            actionDate = from.ActionDate,
            actionType = from.ActionType,
            visitActivityId = from.VisitActivityId,
            visitId = from.VisitId
        )
    }

    override fun fromDomain(domain: VisitActivityLogInformationModel): VisitActivityLogInformationEntity {
        return VisitActivityLogInformationEntity(
            Id = domain.id,
            ActionDate = domain.actionDate,
            ActionType = domain.actionType,
            VisitActivityId = domain.visitActivityId,
            VisitId = domain.visitId
        )
    }
    //endregion

}
