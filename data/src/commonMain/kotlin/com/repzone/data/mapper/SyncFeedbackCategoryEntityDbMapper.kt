package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncFeedbackCategoryEntity
import com.repzone.domain.model.SyncFeedbackCategoryModel

class SyncFeedbackCategoryEntityDbMapper : Mapper<SyncFeedbackCategoryEntity, SyncFeedbackCategoryModel> {
    //region Public Method
    override fun toDomain(from: SyncFeedbackCategoryEntity): SyncFeedbackCategoryModel {
        return SyncFeedbackCategoryModel(
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

    override fun fromDomain(domain: SyncFeedbackCategoryModel): SyncFeedbackCategoryEntity {
        return SyncFeedbackCategoryEntity(
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
    //endregion

}
