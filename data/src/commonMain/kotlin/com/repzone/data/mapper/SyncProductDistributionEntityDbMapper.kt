package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductDistributionEntity
import com.repzone.domain.model.SyncProductDistributionModel

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
            state = from.State,
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
            State = domain.state,
            TenantId = domain.tenantId
        )
    }
    //endregion

}
