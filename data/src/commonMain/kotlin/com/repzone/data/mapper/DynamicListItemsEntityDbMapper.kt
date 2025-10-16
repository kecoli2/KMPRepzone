package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DynamicListItemsEntity
import com.repzone.domain.model.DynamicListItemsModel

class DynamicListItemsEntityDbMapper : Mapper<DynamicListItemsEntity, DynamicListItemsModel> {
    //region Public Method
    override fun toDomain(from: DynamicListItemsEntity): DynamicListItemsModel {
        return DynamicListItemsModel(
            id = from.Id,
            dynamicListId = from.DynamicListId,
            entityId = from.EntityId,
            itemType = from.ItemType,
            modificationDateUtc = from.ModificationDateUtc,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: DynamicListItemsModel): DynamicListItemsEntity {
        return DynamicListItemsEntity(
            Id = domain.id,
            DynamicListId = domain.dynamicListId,
            EntityId = domain.entityId,
            ItemType = domain.itemType,
            ModificationDateUtc = domain.modificationDateUtc,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
