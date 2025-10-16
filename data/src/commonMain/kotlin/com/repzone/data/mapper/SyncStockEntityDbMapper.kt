package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncStockEntity
import com.repzone.domain.model.SyncStockModel

class SyncStockEntityDbMapper : Mapper<SyncStockEntity, SyncStockModel> {
    //region Public Method
    override fun toDomain(from: SyncStockEntity): SyncStockModel {
        return SyncStockModel(
            id = from.Id,
            modificationDateUtc = from.ModificationDateUtc,
            orderStock = from.OrderStock,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            stock = from.Stock,
            unitId = from.UnitId
        )
    }

    override fun fromDomain(domain: SyncStockModel): SyncStockEntity {
        return SyncStockEntity(
            Id = domain.id,
            ModificationDateUtc = domain.modificationDateUtc,
            OrderStock = domain.orderStock,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            Stock = domain.stock,
            UnitId = domain.unitId
        )
    }
    //endregion

}
