package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncReservedStockEntity
import com.repzone.domain.model.SyncReservedStockModel

class SyncReservedStockEntityDbMapper : Mapper<SyncReservedStockEntity, SyncReservedStockModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncReservedStockEntity): SyncReservedStockModel {
        return SyncReservedStockModel(
            uniqueId = from.UniqueId,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            stock = from.Stock,
            unitId = from.UnitId
        )
    }

    override fun fromDomain(domain: SyncReservedStockModel): SyncReservedStockEntity {
        return SyncReservedStockEntity(
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
