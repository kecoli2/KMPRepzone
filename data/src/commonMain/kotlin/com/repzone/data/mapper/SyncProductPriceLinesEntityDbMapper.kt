package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductPriceLinesEntity
import com.repzone.domain.model.SyncProductPriceLinesModel

class SyncProductPriceLinesEntityDbMapper : Mapper<SyncProductPriceLinesEntity, SyncProductPriceLinesModel> {
    //region Public Method
    override fun toDomain(from: SyncProductPriceLinesEntity): SyncProductPriceLinesModel {
        return SyncProductPriceLinesModel(
            id = from.Id,
            modificationDateUtc = from.ModificationDateUtc,
            price = from.Price,
            priceListId = from.PriceListId,
            productId = from.ProductId,
            productUnitId = from.ProductUnitId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            vat = from.Vat
        )
    }

    override fun fromDomain(domain: SyncProductPriceLinesModel): SyncProductPriceLinesEntity {
        return SyncProductPriceLinesEntity(
            Id = domain.id,
            ModificationDateUtc = domain.modificationDateUtc,
            Price = domain.price,
            PriceListId = domain.priceListId,
            ProductId = domain.productId,
            ProductUnitId = domain.productUnitId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            Vat = domain.vat
        )
    }
    //endregion

}
