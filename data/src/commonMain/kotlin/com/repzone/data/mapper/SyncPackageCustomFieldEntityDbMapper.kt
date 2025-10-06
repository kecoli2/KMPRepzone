package com.repzone.data.mapper

import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncPackageCustomFieldEntity
import com.repzone.domain.model.SyncPackageCustomFieldModel
import com.repzone.network.dto.PackageCustomFieldDto

class SyncPackageCustomFieldEntityDbMapper : MapperDto<SyncPackageCustomFieldEntity, SyncPackageCustomFieldModel, PackageCustomFieldDto> {
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

    override fun fromDto(dto: PackageCustomFieldDto): SyncPackageCustomFieldEntity {
        return SyncPackageCustomFieldEntity(
            Id = dto.id.toLong(),
            Description = dto.description,
            ImageUrl = dto.imageUrl,
            IsActive = dto.isActive.toLong(),
            Name = dto.name,
            PackageId = dto.packageId?.toLong(),
            PackageName = dto.packageName
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
