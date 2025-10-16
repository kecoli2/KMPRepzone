package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductUnitParameterEntity
import com.repzone.domain.model.SyncProductUnitParameterModel

class SyncProductUnitParameterEntityDbMapper : Mapper<SyncProductUnitParameterEntity, SyncProductUnitParameterModel> {
    //region Public Method
    override fun toDomain(from: SyncProductUnitParameterEntity): SyncProductUnitParameterModel {
        return SyncProductUnitParameterModel(
            id = from.Id,
            entityId = from.EntityId,
            entityType = from.EntityType,
            maxOrderQuantity = from.MaxOrderQuantity,
            minOrderQuantity = from.MinOrderQuantity,
            modificationDateUtc = from.ModificationDateUtc,
            productId = from.ProductId,
            productUnitId = from.ProductUnitId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncProductUnitParameterModel): SyncProductUnitParameterEntity {
        return SyncProductUnitParameterEntity(
            Id = domain.id,
            EntityId = domain.entityId,
            EntityType = domain.entityType,
            MaxOrderQuantity = domain.maxOrderQuantity,
            MinOrderQuantity = domain.minOrderQuantity,
            ModificationDateUtc = domain.modificationDateUtc,
            ProductId = domain.productId,
            ProductUnitId = domain.productUnitId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
