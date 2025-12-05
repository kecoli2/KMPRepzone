package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductDistributionLineEntity
import com.repzone.domain.model.SyncProductDistributionLineModel
import com.repzone.network.dto.SyncProductDistributionLineDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SyncProductDistributionLineEntityDbMapper : Mapper<SyncProductDistributionLineEntity, SyncProductDistributionLineModel> {
    //region Public Method
    override fun toDomain(from: SyncProductDistributionLineEntity): SyncProductDistributionLineModel {
        return SyncProductDistributionLineModel(
            id = from.Id,
            color = from.Color,
            displayOrder = from.DisplayOrder,
            distributionId = from.DistributionId,
            modificationDateUtc = from.ModificationDateUtc,
            mustHave = from.MustHave,
            productId = from.ProductId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncProductDistributionLineModel): SyncProductDistributionLineEntity {
        return SyncProductDistributionLineEntity(
            Id = domain.id,
            Color = domain.color,
            DisplayOrder = domain.displayOrder,
            DistributionId = domain.distributionId,
            ModificationDateUtc = domain.modificationDateUtc,
            MustHave = domain.mustHave,
            ProductId = domain.productId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state.enumToLong()
        )
    }

    fun fromDto(dto: SyncProductDistributionLineDto): SyncProductDistributionLineEntity {
        return SyncProductDistributionLineEntity(
            Id = dto.id.toLong(),
            Color = dto.color,
            DisplayOrder = dto.displayOrder.toLong(),
            DistributionId = dto.distributionId.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            MustHave = dto.mustHave.toLong(),
            ProductId = dto.productId.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.enumToLong()
        )
    }
    //endregion

}
