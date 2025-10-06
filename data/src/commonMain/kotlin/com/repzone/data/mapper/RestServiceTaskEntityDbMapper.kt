package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.RestServiceTaskEntity
import com.repzone.domain.model.RestServiceTaskModel

class RestServiceTaskEntityDbMapper : Mapper<RestServiceTaskEntity, RestServiceTaskModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: RestServiceTaskEntity): RestServiceTaskModel {
        return RestServiceTaskModel(
            id = from.Id,
            actionName = from.ActionName,
            callType = from.CallType,
            createdTime = from.CreatedTime,
            methodPath = from.MethodPath,
            requestObject = from.RequestObject,
            requestObjectUniqueID = from.RequestObjectUniqueID,
            requestType = from.RequestType,
            result = from.Result,
            status = from.Status
        )
    }

    override fun fromDomain(domain: RestServiceTaskModel): RestServiceTaskEntity {
        return RestServiceTaskEntity(
            Id = domain.id,
            ActionName = domain.actionName,
            CallType = domain.callType,
            CreatedTime = domain.createdTime,
            MethodPath = domain.methodPath,
            RequestObject = domain.requestObject,
            RequestObjectUniqueID = domain.requestObjectUniqueID,
            RequestType = domain.requestType,
            Result = domain.result,
            Status = domain.status
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
