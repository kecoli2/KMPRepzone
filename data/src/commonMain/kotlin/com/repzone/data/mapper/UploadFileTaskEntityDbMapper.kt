package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.UploadFileTaskEntity
import com.repzone.domain.model.UploadFileTaskModel

class UploadFileTaskEntityDbMapper : Mapper<UploadFileTaskEntity, UploadFileTaskModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: UploadFileTaskEntity): UploadFileTaskModel {
        return UploadFileTaskModel(
            id = from.Id,
            azurePath = from.AzurePath,
            filePath = from.FilePath,
            status = from.Status,
            storeFileName = from.StoreFileName
        )
    }

    override fun fromDomain(domain: UploadFileTaskModel): UploadFileTaskEntity {
        return UploadFileTaskEntity(
            Id = domain.id,
            AzurePath = domain.azurePath,
            FilePath = domain.filePath,
            Status = domain.status,
            StoreFileName = domain.storeFileName
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
