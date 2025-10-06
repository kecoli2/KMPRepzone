package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncUnitEntity
import com.repzone.domain.model.SyncUnitModel

class SyncUnitEntityDbMapper : Mapper<SyncUnitEntity, SyncUnitModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncUnitEntity): SyncUnitModel {
        return SyncUnitModel(
            id = from.Id,
            isVisible = from.IsVisible,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncUnitModel): SyncUnitEntity {
        return SyncUnitEntity(
            Id = domain.id,
            IsVisible = domain.isVisible,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
