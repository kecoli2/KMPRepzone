package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.DynamicListHeadersEntity
import com.repzone.domain.model.DynamicListHeadersModel

class DynamicListHeadersEntityDbMapper : Mapper<DynamicListHeadersEntity, DynamicListHeadersModel> {
    //region Public Method
    override fun toDomain(from: DynamicListHeadersEntity): DynamicListHeadersModel {
        return DynamicListHeadersModel(
            id = from.Id,
            description = from.Description,
            listType = from.ListType,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            shareStatus = from.ShareStatus,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: DynamicListHeadersModel): DynamicListHeadersEntity {
        return DynamicListHeadersEntity(
            Id = domain.id,
            Description = domain.description,
            ListType = domain.listType,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            ShareStatus = domain.shareStatus,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
