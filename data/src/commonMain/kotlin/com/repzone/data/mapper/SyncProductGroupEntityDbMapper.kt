package com.repzone.data.mapper

import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductGroupEntity
import com.repzone.domain.model.SyncProductGroupModel
import com.repzone.network.dto.ServiceProductGroupDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncProductGroupEntityDbMapper: Mapper<SyncProductGroupEntity, SyncProductGroupModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncProductGroupEntity): SyncProductGroupModel {
        return SyncProductGroupModel(
            id = from.Id,
            iconIndex = from.IconIndex,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            parentId = from.ParentId,
            photoPath = from.PhotoPath,
            recordDateUtc = from.RecordDateUtc,
            shared = from.Shared,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncProductGroupModel): SyncProductGroupEntity {
        return SyncProductGroupEntity(
            Id = domain.id,
            IconIndex = domain.iconIndex,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            ParentId = domain.parentId,
            PhotoPath = domain.photoPath,
            RecordDateUtc = domain.recordDateUtc,
            Shared = domain.shared,
            State = domain.state
        )
    }

    fun fromDto(dto: ServiceProductGroupDto): SyncProductGroupEntity {
        return SyncProductGroupEntity(
            Id = dto.id.toLong(),
            IconIndex = dto.iconIndex.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            OrganizationId = dto.organizationId?.toLong(),
            ParentId = dto.parentId?.toLong(),
            PhotoPath = dto.photoPath,
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            Shared = dto.shared.toLong(),
            State = dto.state.toLong()
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}