package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncEventReasonEntity
import com.repzone.domain.model.SyncEventReasonModel

class SyncEventReasonEntityDbMapper : Mapper<SyncEventReasonEntity, SyncEventReasonModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncEventReasonEntity): SyncEventReasonModel {
        return SyncEventReasonModel(
            id = from.Id,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            reasonType = from.ReasonType,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tags = from.Tags
        )
    }

    override fun fromDomain(domain: SyncEventReasonModel): SyncEventReasonEntity {
        return SyncEventReasonEntity(
            Id = domain.id,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            ReasonType = domain.reasonType,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            Tags = domain.tags
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
