package com.repzone.data.mapper

import com.repzone.core.util.extensions.enumToLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductPriceLinesEntity
import com.repzone.domain.model.SyncProductPriceLinesModel
import com.repzone.network.dto.SyncProductPriceLinesDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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

    fun fromDto(dto: SyncProductPriceLinesDto): SyncProductPriceLinesEntity {
        return SyncProductPriceLinesEntity(
            Id = dto.id.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            Price = dto.price,
            PriceListId = dto.priceListId.toLong(),
            ProductId = dto.productId.toLong(),
            ProductUnitId = dto.productUnitId.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.enumToLong(),
            Vat = dto.vat
        )
    }
    //endregion

}
