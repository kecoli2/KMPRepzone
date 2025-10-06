package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerGroupEntity
import com.repzone.domain.model.SyncCustomerGroupModel

class SyncCustomerGroupEntityDbMapper : Mapper<SyncCustomerGroupEntity, SyncCustomerGroupModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncCustomerGroupEntity): SyncCustomerGroupModel {
        return SyncCustomerGroupModel(
            id = from.Id,
            iconIndex = from.IconIndex,
            isDefault = from.IsDefault,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            parentId = from.ParentId,
            recordDateUtc = from.RecordDateUtc,
            shared = from.Shared,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncCustomerGroupModel): SyncCustomerGroupEntity {
        return SyncCustomerGroupEntity(
            Id = domain.id,
            IconIndex = domain.iconIndex,
            IsDefault = domain.isDefault,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            ParentId = domain.parentId,
            RecordDateUtc = domain.recordDateUtc,
            Shared = domain.shared,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
