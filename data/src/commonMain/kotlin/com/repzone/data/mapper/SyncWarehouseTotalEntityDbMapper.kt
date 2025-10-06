package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncWarehouseTotalEntity
import com.repzone.domain.model.SyncWarehouseTotalModel

class SyncWarehouseTotalEntityDbMapper : Mapper<SyncWarehouseTotalEntity, SyncWarehouseTotalModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncWarehouseTotalEntity): SyncWarehouseTotalModel {
        return SyncWarehouseTotalModel(
            orderStock = from.OrderStock,
            productId = from.ProductId,
            stock = from.Stock
        )
    }

    override fun fromDomain(domain: SyncWarehouseTotalModel): SyncWarehouseTotalEntity {
        return SyncWarehouseTotalEntity(
            OrderStock = domain.orderStock,
            ProductId = domain.productId,
            Stock = domain.stock
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
