package com.repzone.data.mapper

import com.repzone.database.SyncProductEntity
import com.repzone.domain.model.SyncProductModel

/**
 * Auto-generated mapper for SyncProductEntity
 * Maps between Entity (database) and Model (domain)
 */
object SyncProductEntityDbMapper {

    /**
     * Convert Entity to Model
     */
    fun SyncProductEntity.toModel(): SyncProductModel {
        return SyncProductModel(
            Id = this.Id,
            BrandId = this.BrandId,
            BrandName = this.BrandName,
            BrandPhotoPath = this.BrandPhotoPath,
            CloseToReturns = this.CloseToReturns,
            CloseToSales = this.CloseToSales,
            Color = this.Color,
            Description = this.Description,
            DisplayOrder = this.DisplayOrder,
            GroupId = this.GroupId,
            GroupName = this.GroupName,
            GroupPhotoPath = this.GroupPhotoPath,
            IsVisible = this.IsVisible,
            ManufacturerId = this.ManufacturerId,
            MaximumOrderQuantity = this.MaximumOrderQuantity,
            MinimumOrderQuantity = this.MinimumOrderQuantity,
            ModificationDateUtc = this.ModificationDateUtc,
            Name = this.Name,
            OrderQuantityFactor = this.OrderQuantityFactor,
            OrganizationId = this.OrganizationId,
            OrganizationIds = this.OrganizationIds,
            PhotoPath = this.PhotoPath,
            RecordDateUtc = this.RecordDateUtc,
            Shared = this.Shared,
            Sku = this.Sku,
            State = this.State,
            Tags = this.Tags,
            TenantId = this.TenantId,
            Vat = this.Vat
        )
    }

    /**
     * Convert Model to Entity
     */
    fun SyncProductModel.toEntity(): SyncProductEntity {
        return SyncProductEntity(
            Id = this.Id,
            BrandId = this.BrandId,
            BrandName = this.BrandName,
            BrandPhotoPath = this.BrandPhotoPath,
            CloseToReturns = this.CloseToReturns,
            CloseToSales = this.CloseToSales,
            Color = this.Color,
            Description = this.Description,
            DisplayOrder = this.DisplayOrder,
            GroupId = this.GroupId,
            GroupName = this.GroupName,
            GroupPhotoPath = this.GroupPhotoPath,
            IsVisible = this.IsVisible,
            ManufacturerId = this.ManufacturerId,
            MaximumOrderQuantity = this.MaximumOrderQuantity,
            MinimumOrderQuantity = this.MinimumOrderQuantity,
            ModificationDateUtc = this.ModificationDateUtc,
            Name = this.Name,
            OrderQuantityFactor = this.OrderQuantityFactor,
            OrganizationId = this.OrganizationId,
            OrganizationIds = this.OrganizationIds,
            PhotoPath = this.PhotoPath,
            RecordDateUtc = this.RecordDateUtc,
            Shared = this.Shared,
            Sku = this.Sku,
            State = this.State,
            Tags = this.Tags,
            TenantId = this.TenantId,
            Vat = this.Vat
        )
    }

    /**
     * Convert List of Entities to List of Models
     */
    fun List<SyncProductEntity>.toModelList(): List<SyncProductModel> {
        return map { it.toModel() }
    }

    /**
     * Convert List of Models to List of Entities
     */
    fun List<SyncProductModel>.toEntityList(): List<SyncProductEntity> {
        return map { it.toEntity() }
    }
}
