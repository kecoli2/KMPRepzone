package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncPackageCustomFieldEntity
import com.repzone.domain.model.SyncPackageCustomFieldModel

class SyncPackageCustomFieldEntityDbMapper : Mapper<SyncPackageCustomFieldEntity, SyncPackageCustomFieldModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncPackageCustomFieldEntity): SyncPackageCustomFieldModel {
        return SyncPackageCustomFieldModel(
            id = from.Id,
            description = from.Description,
            imageUrl = from.ImageUrl,
            isActive = from.IsActive,
            name = from.Name,
            packageId = from.PackageId,
            packageName = from.PackageName
        )
    }

    override fun fromDomain(domain: SyncPackageCustomFieldModel): SyncPackageCustomFieldEntity {
        return SyncPackageCustomFieldEntity(
            Id = domain.id,
            Description = domain.description,
            ImageUrl = domain.imageUrl,
            IsActive = domain.isActive,
            Name = domain.name,
            PackageId = domain.packageId,
            PackageName = domain.packageName
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
