package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncFormBaseEntity
import com.repzone.domain.model.SyncFormBaseModel

class SyncFormBaseEntityDbMapper : Mapper<SyncFormBaseEntity, SyncFormBaseModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncFormBaseEntity): SyncFormBaseModel {
        return SyncFormBaseModel(
            id = from.Id,
            data = from.Data,
            description = from.Description,
            documentTypeId = from.DocumentTypeId,
            formId = from.FormId,
            formName = from.FormName,
            modificationDateUtc = from.ModificationDateUtc,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            visibleOption = from.VisibleOption
        )
    }

    override fun fromDomain(domain: SyncFormBaseModel): SyncFormBaseEntity {
        return SyncFormBaseEntity(
            Id = domain.id,
            Data = domain.data,
            Description = domain.description,
            DocumentTypeId = domain.documentTypeId,
            FormId = domain.formId,
            FormName = domain.formName,
            ModificationDateUtc = domain.modificationDateUtc,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            VisibleOption = domain.visibleOption
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
