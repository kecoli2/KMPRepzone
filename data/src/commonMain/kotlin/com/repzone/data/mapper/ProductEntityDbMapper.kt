package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductEntity
import com.repzone.domain.model.SyncProductModel

class ProductEntityDbMapper: Mapper<SyncProductEntity, SyncProductModel> {
    //region Public Method
    override fun toDomain(from: SyncProductEntity) : SyncProductModel {
        return SyncProductModel(
            id = from.Id,
            brandId = from.BrandId,
            brandName = from.BrandName,
            brandPhotoPath = from.BrandPhotoPath,
            closeToReturns = from.CloseToReturns?.let { it != 0L } ?: false,
            closeToSales = from.CloseToSales?.let { it != 0L } ?: false,
            color = from.Color,
            description = from.Description,
            displayOrder = from.DisplayOrder,
            groupId = from.GroupId,
            groupName = from.GroupName,
            groupPhotoPath = from.GroupPhotoPath,
            isVisible = from.IsVisible?.let { it != 0L },
            manufacturerId = from.ManufacturerId,
            maximumOrderQuantity = from.MaximumOrderQuantity,
            minimumOrderQuantity = from.MinimumOrderQuantity,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            orderQuantityFactor = from.OrderQuantityFactor,
            organizationId = from.OrganizationId,
            organizationIds = from.OrganizationIds,
            photoPath = from.PhotoPath,
            recordDateUtc = from.RecordDateUtc,
            shared = from.Shared,
            sku = from.Sku,
            state = from.State,
            tags = from.Tags,
            tenantId = from.TenantId,
            vat = from.Vat
        )
    }

    override fun fromDomain(domain: SyncProductModel) : SyncProductEntity {
        return SyncProductEntity(
            Id = domain.id,
            BrandId = domain.brandId,
            BrandName = domain.brandName,
            BrandPhotoPath = domain.brandPhotoPath,
            CloseToReturns = domain.closeToReturns.let { if (it) 1L else 0L },
            CloseToSales = domain.closeToSales.let { if (it) 1L else 0L },
            Color = domain.color,
            Description = domain.description,
            DisplayOrder = domain.displayOrder,
            GroupId = domain.groupId,
            GroupName = domain.groupName,
            GroupPhotoPath = domain.groupPhotoPath,
            IsVisible = domain.isVisible?.let { if (it) 1L else 0L },
            ManufacturerId = domain.manufacturerId,
            MaximumOrderQuantity = domain.maximumOrderQuantity,
            MinimumOrderQuantity = domain.minimumOrderQuantity,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrderQuantityFactor = domain.orderQuantityFactor,
            OrganizationId = domain.organizationId,
            OrganizationIds = domain.organizationIds,
            PhotoPath = domain.photoPath,
            RecordDateUtc = domain.recordDateUtc,
            Shared = domain.shared,
            Sku = domain.sku,
            State = domain.state,
            Tags = domain.tags,
            TenantId = domain.tenantId,
            Vat = domain.vat
        )
    }
    //endregion

}