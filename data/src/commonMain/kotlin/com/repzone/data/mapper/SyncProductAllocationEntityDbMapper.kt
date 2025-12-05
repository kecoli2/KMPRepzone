package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductAllocationEntity
import com.repzone.domain.model.SyncProductAllocationModel

class SyncProductAllocationEntityDbMapper : Mapper<SyncProductAllocationEntity, SyncProductAllocationModel> {
    //region Public Method
    override fun toDomain(from: SyncProductAllocationEntity): SyncProductAllocationModel {
        return SyncProductAllocationModel(
            id = from.Id,
            begin = from.Begin,
            end = from.End,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            quantity = from.Quantity,
            recordDateUtc = from.RecordDateUtc,
            scope = from.Scope,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            unitId = from.UnitId
        )
    }

    override fun fromDomain(domain: SyncProductAllocationModel): SyncProductAllocationEntity {
        return SyncProductAllocationEntity(
            Id = domain.id,
            Begin = domain.begin,
            End = domain.end,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId,
            Quantity = domain.quantity,
            RecordDateUtc = domain.recordDateUtc,
            Scope = domain.scope,
            State = domain.state.enumToLong(),
            UnitId = domain.unitId
        )
    }
    //endregion

}
