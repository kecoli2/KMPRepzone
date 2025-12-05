package com.repzone.data.mapper

import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncUnitEntity
import com.repzone.domain.model.SyncUnitModel
import com.repzone.network.dto.SyncUnitDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncUnitEntityDbMapper : Mapper<SyncUnitEntity, SyncUnitModel> {
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

    fun fromDto(model: SyncUnitDto): SyncUnitEntity {
        return SyncUnitEntity(
            Id = model.id.toLong(),
            IsVisible = model.isVisible.toLong(),
            ModificationDateUtc = model.modificationDateUtc?.toEpochMilliseconds(),
            Name = model.name,
            RecordDateUtc = model.recordDateUtc?.toEpochMilliseconds(),
            State = model.state.enumToLong(),
            TenantId = model.tenantId.toLong()
        )
    }
    //endregion

}
