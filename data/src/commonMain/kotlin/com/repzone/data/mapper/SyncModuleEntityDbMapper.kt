package com.repzone.data.mapper

import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncModuleEntity
import com.repzone.domain.model.RequestType
import com.repzone.domain.model.SyncModuleModel

class SyncModuleEntityDbMapper: Mapper<SyncModuleEntity, SyncModuleModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncModuleEntity): SyncModuleModel {
        return SyncModuleModel(
            syncType = from.SyncType,
            requestUrl = from.RequestUrl,
            requestFilter = from.RequestFilter,
            lastSyncDate = from.LastSyncDate,
            requestType =  from.RequestType?.toEnum<RequestType>()
        )
    }

    override fun fromDomain(domain: SyncModuleModel): SyncModuleEntity {
        return SyncModuleEntity(
            SyncType = domain.syncType,
            RequestUrl = domain.requestUrl!!,
            RequestFilter = domain.requestFilter,
            LastSyncDate = domain.lastSyncDate,
            RequestType = domain.requestType?.toLong()
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}