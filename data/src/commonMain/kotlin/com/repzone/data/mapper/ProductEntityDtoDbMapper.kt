package com.repzone.data.mapper

import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductEntity
import com.repzone.network.dto.MobileProductDto

class ProductEntityDtoDbMapper: Mapper<SyncProductEntity, MobileProductDto> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override fun toDomain(from: SyncProductEntity): MobileProductDto {
        return MobileProductDto(
            id = from.Id.toInt(),
            state = from.State?.toInt() ?: 0,
            sku = from.Sku,
            name = from.Name,
            description = from.Description,
            tags = from.Tags,
            brandId = from.BrandId?.toInt(),
            brandName = from.BrandName,
            displayOrder = from.DisplayOrder?.toInt(),
            orderQuantityFactor = from.OrderQuantityFactor?.toInt(),
            minimumOrderQuantity = from.MinimumOrderQuantity?.toInt(),
            maximumOrderQuantity = from.MaximumOrderQuantity?.toInt(),
            groupId = from.GroupId?.toInt(),
            groupName = from.GroupName,
            photoPath = from.PhotoPath,
            vat = from.Vat,
            organizationId = from.OrganizationId?.toInt(),
            organizationIds = from.OrganizationIds,
            units = emptyList(),
            color = from.Color,
            brandPhotoPath = from.BrandPhotoPath,
            groupPhotoPath = from.GroupPhotoPath,
            parameters = emptyList(),
            modificationDateUtc = from.ModificationDateUtc,
            closeToSales = from.CloseToSales?.toBoolean(true),
            closeToReturns = from.CloseToReturns.toBoolean(true),
        )
    }

    override fun fromDomain(domain: MobileProductDto): SyncProductEntity {
        return SyncProductEntity(
            Id = domain.id.toLong(),
            BrandId = domain.brandId?.toLong(),
            BrandName = domain.brandName,
            BrandPhotoPath = domain.brandPhotoPath,
            CloseToReturns = domain.closeToReturns?.toLong(),
            CloseToSales = domain.closeToSales?.toLong(),
            Color = domain.color,
            Description = domain.description,
            DisplayOrder = domain.displayOrder?.toLong(),
            GroupId = domain.groupId?.toLong(),
            GroupName = domain.groupName,
            GroupPhotoPath = domain.groupPhotoPath,
            IsVisible = false.toLong(),
            ManufacturerId = null,
            MaximumOrderQuantity = domain.maximumOrderQuantity?.toLong(),
            MinimumOrderQuantity = domain.minimumOrderQuantity?.toLong(),
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrderQuantityFactor = domain.orderQuantityFactor?.toLong(),
            OrganizationId = domain.organizationId?.toLong(),
            OrganizationIds = domain.organizationIds,
            PhotoPath = domain.photoPath,
            RecordDateUtc = null,
            Shared = null,
            Sku = domain.sku,
            State = domain.state.toLong(),
            Tags = domain.tags,
            TenantId = null,
            Vat = domain.vat
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}