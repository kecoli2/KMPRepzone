package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.DownedDriveItemEntity
import com.repzone.domain.model.DownedDriveItemModel

class DownedDriveItemEntityDbMapper : Mapper<DownedDriveItemEntity, DownedDriveItemModel> {
    //region Public Method
    override fun toDomain(from: DownedDriveItemEntity): DownedDriveItemModel {
        return DownedDriveItemModel(
            uniqueId = from.UniqueId,
            downloadedPath = from.DownloadedPath,
            driveItemJson = from.DriveItemJson
        )
    }

    override fun fromDomain(domain: DownedDriveItemModel): DownedDriveItemEntity {
        return DownedDriveItemEntity(
            UniqueId = domain.uniqueId,
            DownloadedPath = domain.downloadedPath,
            DriveItemJson = domain.driveItemJson
        )
    }
    //endregion

}
