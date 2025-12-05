package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductDistributionEntity
import com.repzone.domain.model.SyncProductDistributionModel
import com.repzone.network.dto.SyncProductDistributionDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncProductDistributionEntityDbMapper : Mapper<SyncProductDistributionEntity, SyncProductDistributionModel> {
    //region Public Method
    override fun toDomain(from: SyncProductDistributionEntity): SyncProductDistributionModel {
        return SyncProductDistributionModel(
            id = from.Id,
            description = from.Description,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            parentId = from.ParentId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncProductDistributionModel): SyncProductDistributionEntity {
        return SyncProductDistributionEntity(
            Id = domain.id,
            Description = domain.description,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            ParentId = domain.parentId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong(),
            TenantId = domain.tenantId
        )
    }

    fun fromDto(dto: SyncProductDistributionDto): SyncProductDistributionEntity {
        return SyncProductDistributionEntity(
            Id = dto.id.toLong(),
            Description = dto.description,
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            OrganizationId = dto.organizationId.toLong(),
            ParentId = dto.parentId.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.enumToLong(),
            TenantId = dto.tenantId.toLong()
        )
    }
    //endregion

}
