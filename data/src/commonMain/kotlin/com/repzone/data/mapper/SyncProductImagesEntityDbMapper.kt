package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductImagesEntity
import com.repzone.domain.model.SyncProductImagesModel

class SyncProductImagesEntityDbMapper : Mapper<SyncProductImagesEntity, SyncProductImagesModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncProductImagesEntity): SyncProductImagesModel {
        return SyncProductImagesModel(
            id = from.Id,
            fileId = from.FileId,
            imageUrl = from.ImageUrl,
            modificationDateUtc = from.ModificationDateUtc,
            productId = from.ProductId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncProductImagesModel): SyncProductImagesEntity {
        return SyncProductImagesEntity(
            Id = domain.id,
            FileId = domain.fileId,
            ImageUrl = domain.imageUrl,
            ModificationDateUtc = domain.modificationDateUtc,
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
