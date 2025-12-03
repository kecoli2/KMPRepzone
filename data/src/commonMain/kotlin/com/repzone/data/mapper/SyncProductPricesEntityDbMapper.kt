package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductPricesEntity
import com.repzone.domain.model.SyncProductPricesModel
import com.repzone.network.dto.SyncProductPricesDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncProductPricesEntityDbMapper : Mapper<SyncProductPricesEntity, SyncProductPricesModel> {
    //region Public Method
    override fun toDomain(from: SyncProductPricesEntity): SyncProductPricesModel {
        return SyncProductPricesModel(
            id = from.Id,
            begin = from.Begin,
            description = from.Description,
            end = from.End,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            parentId = from.ParentId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncProductPricesModel): SyncProductPricesEntity {
        return SyncProductPricesEntity(
            Id = domain.id,
            Begin = domain.begin,
            Description = domain.description,
            End = domain.end,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            ParentId = domain.parentId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }

    fun fromDto(dto:SyncProductPricesDto): SyncProductPricesEntity {
        return SyncProductPricesEntity(
            Id = dto.id.toLong(),
            Begin = dto.begin?.toEpochMilliseconds(),
            Description = dto.description,
            End = dto.end?.toEpochMilliseconds(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Name = dto.name,
            OrganizationId = dto.organizationId.toLong(),
            ParentId = dto.parentId.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.toLong(),
            TenantId = dto.tenantId.toLong()
        )
    }
    //endregion

}
