package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncOrganizationEntity
import com.repzone.domain.model.SyncOrganizationModel

class SyncOrganizationEntityDbMapper : Mapper<SyncOrganizationEntity, SyncOrganizationModel> {
    //region Public Method
    override fun toDomain(from: SyncOrganizationEntity): SyncOrganizationModel {
        return SyncOrganizationModel(
            id = from.Id,
            isParent = from.IsParent,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            parentId = from.ParentId,
            recordDateUtc = from.RecordDateUtc,
            relationId = from.RelationId,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncOrganizationModel): SyncOrganizationEntity {
        return SyncOrganizationEntity(
            Id = domain.id,
            IsParent = domain.isParent,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            ParentId = domain.parentId,
            RecordDateUtc = domain.recordDateUtc,
            RelationId = domain.relationId,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
