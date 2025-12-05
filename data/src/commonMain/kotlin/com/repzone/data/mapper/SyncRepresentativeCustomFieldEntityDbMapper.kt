package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncRepresentativeCustomFieldEntity
import com.repzone.domain.model.SyncRepresentativeCustomFieldModel

class SyncRepresentativeCustomFieldEntityDbMapper : Mapper<SyncRepresentativeCustomFieldEntity, SyncRepresentativeCustomFieldModel> {
    //region Public Method
    override fun toDomain(from: SyncRepresentativeCustomFieldEntity): SyncRepresentativeCustomFieldModel {
        return SyncRepresentativeCustomFieldModel(
            id = from.Id,
            entityId = from.EntityId,
            fieldId = from.FieldId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
            value = from.Value
        )
    }

    override fun fromDomain(domain: SyncRepresentativeCustomFieldModel): SyncRepresentativeCustomFieldEntity {
        return SyncRepresentativeCustomFieldEntity(
            Id = domain.id,
            EntityId = domain.entityId,
            FieldId = domain.fieldId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong(),
            Value = domain.value
        )
    }
    //endregion

}
