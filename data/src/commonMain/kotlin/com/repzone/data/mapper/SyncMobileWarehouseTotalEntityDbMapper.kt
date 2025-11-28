package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncMobileWarehouseTotalEntity
import com.repzone.domain.model.SyncMobileWarehouseTotalModel

class SyncMobileWarehouseTotalEntityDbMapper : Mapper<SyncMobileWarehouseTotalEntity, SyncMobileWarehouseTotalModel> {
    //region Public Method
    override fun toDomain(from: SyncMobileWarehouseTotalEntity): SyncMobileWarehouseTotalModel {
        return SyncMobileWarehouseTotalModel(
            productId = from.ProductId,
            stock = from.Stock,
            orderStock = from.OrderStock
        )
    }

    override fun fromDomain(domain: SyncMobileWarehouseTotalModel): SyncMobileWarehouseTotalEntity {
        return SyncMobileWarehouseTotalEntity(
            ProductId = domain.productId,
            Stock = domain.stock,
            OrderStock = domain.orderStock
        )
    }
    //endregion

}
