package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DynamicListOrganizationEntity
import com.repzone.domain.model.DynamicListOrganizationModel

class DynamicListOrganizationEntityDbMapper : Mapper<DynamicListOrganizationEntity, DynamicListOrganizationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: DynamicListOrganizationEntity): DynamicListOrganizationModel {
        return DynamicListOrganizationModel(
            id = from.Id,
            canUse = from.CanUse,
            dynamicListId = from.DynamicListId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: DynamicListOrganizationModel): DynamicListOrganizationEntity {
        return DynamicListOrganizationEntity(
            Id = domain.id,
            CanUse = domain.canUse,
            DynamicListId = domain.dynamicListId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
