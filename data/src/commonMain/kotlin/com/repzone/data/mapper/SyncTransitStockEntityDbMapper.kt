package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncTransitStockEntity
import com.repzone.domain.model.SyncTransitStockModel

class SyncTransitStockEntityDbMapper : Mapper<SyncTransitStockEntity, SyncTransitStockModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncTransitStockEntity): SyncTransitStockModel {
        return SyncTransitStockModel(
            uniqueId = from.UniqueId,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            stock = from.Stock,
            unitId = from.UnitId
        )
    }

    override fun fromDomain(domain: SyncTransitStockModel): SyncTransitStockEntity {
        return SyncTransitStockEntity(
            UniqueId = domain.uniqueId,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId,
            Stock = domain.stock,
            UnitId = domain.unitId
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
