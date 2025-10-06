package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncRepresentativeAllocationEntity
import com.repzone.domain.model.SyncRepresentativeAllocationModel

class SyncRepresentativeAllocationEntityDbMapper : Mapper<SyncRepresentativeAllocationEntity, SyncRepresentativeAllocationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncRepresentativeAllocationEntity): SyncRepresentativeAllocationModel {
        return SyncRepresentativeAllocationModel(
            id = from.Id,
            amount = from.Amount,
            begin = from.Begin,
            end = from.End,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            representativeId = from.RepresentativeId,
            scope = from.Scope,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncRepresentativeAllocationModel): SyncRepresentativeAllocationEntity {
        return SyncRepresentativeAllocationEntity(
            Id = domain.id,
            Amount = domain.amount,
            Begin = domain.begin,
            End = domain.end,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            RepresentativeId = domain.representativeId,
            Scope = domain.scope,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
