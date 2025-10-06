package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncDocumentMapProcessEntity
import com.repzone.domain.model.SyncDocumentMapProcessModel

class SyncDocumentMapProcessEntityDbMapper : Mapper<SyncDocumentMapProcessEntity, SyncDocumentMapProcessModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncDocumentMapProcessEntity): SyncDocumentMapProcessModel {
        return SyncDocumentMapProcessModel(
            id = from.Id,
            docProcessType = from.DocProcessType,
            documentMapName = from.DocumentMapName,
            name = from.Name
        )
    }

    override fun fromDomain(domain: SyncDocumentMapProcessModel): SyncDocumentMapProcessEntity {
        return SyncDocumentMapProcessEntity(
            Id = domain.id,
            DocProcessType = domain.docProcessType,
            DocumentMapName = domain.documentMapName,
            Name = domain.name
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
