package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductCustomFieldEntity
import com.repzone.domain.model.SyncProductCustomFieldModel

class SyncProductCustomFieldEntityDbMapper : Mapper<SyncProductCustomFieldEntity, SyncProductCustomFieldModel> {
    //region Public Method
    override fun toDomain(from: SyncProductCustomFieldEntity): SyncProductCustomFieldModel {
        return SyncProductCustomFieldModel(
            id = from.Id,
            entityId = from.EntityId,
            fieldId = from.FieldId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            value = from.Value
        )
    }

    override fun fromDomain(domain: SyncProductCustomFieldModel): SyncProductCustomFieldEntity {
        return SyncProductCustomFieldEntity(
            Id = domain.id,
            EntityId = domain.entityId,
            FieldId = domain.fieldId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            Value = domain.value
        )
    }
    //endregion

}
