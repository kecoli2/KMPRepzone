package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductDistributionLineEntity
import com.repzone.domain.model.SyncProductDistributionLineModel

class SyncProductDistributionLineEntityDbMapper : Mapper<SyncProductDistributionLineEntity, SyncProductDistributionLineModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

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
            state = from.State
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
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
