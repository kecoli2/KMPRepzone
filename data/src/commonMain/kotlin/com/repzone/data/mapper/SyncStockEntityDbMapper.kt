package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncStockEntity
import com.repzone.domain.model.SyncStockModel
import com.repzone.network.dto.SyncStockDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
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
            State = domain.state.enumToLong(),
            Stock = domain.stock,
            UnitId = domain.unitId
        )
    }

    fun fromDto(dto: SyncStockDto): SyncStockEntity {
        return SyncStockEntity(
            Id = dto.id.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            OrderStock = dto.orderStock,
            OrganizationId = dto.organizationId.toLong(),
            ProductId = dto.productId.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.enumToLong(),
            Stock = dto.stock,
            UnitId = dto.unitId.toLong()
        )
    }
    //endregion

}
