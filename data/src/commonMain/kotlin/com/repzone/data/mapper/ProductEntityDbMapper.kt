package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductEntity
import com.repzone.domain.model.SyncProductModel

class ProductEntityDbMapper: Mapper<SyncProductEntity, SyncProductModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncProductEntity) = SyncProductModel(
        Id = from.Id,
        BrandId = from.BrandId,
        BrandName = from.BrandName,
        BrandPhotoPath = from.BrandPhotoPath,
        CloseToReturns = from.CloseToReturns,
        CloseToSales = from.CloseToSales,
        Color = from.Color,
        Description = from.Description,
        DisplayOrder = from.DisplayOrder,
        GroupId = from.GroupId,
        GroupName = from.GroupName,
        GroupPhotoPath = from.GroupPhotoPath,
        IsVisible = from.IsVisible,
        ManufacturerId = from.ManufacturerId,
        MaximumOrderQuantity = from.MaximumOrderQuantity,
        MinimumOrderQuantity = from.MinimumOrderQuantity,
        ModificationDateUtc = from.ModificationDateUtc,
        Name = from.Name,
        OrderQuantityFactor = from.OrderQuantityFactor,
        OrganizationId = from.OrganizationId,
        OrganizationIds = from.OrganizationIds,
        PhotoPath = from.PhotoPath,
        RecordDateUtc = from.RecordDateUtc,
        Shared = from.Shared,
        Sku = from.Sku,
        State = from.State,
        Tags = from.Tags,
        TenantId = from.TenantId,
        Vat = from.Vat
    )

    override fun fromDomain(domain: SyncProductModel) = SyncProductEntity(
        Id = domain.Id,
        BrandId = domain.BrandId,
        BrandName = domain.BrandName,
        BrandPhotoPath = domain.BrandPhotoPath,
        CloseToReturns = domain.CloseToReturns,
        CloseToSales = domain.CloseToSales,
        Color = domain.Color,
        Description = domain.Description,
        DisplayOrder = domain.DisplayOrder,
        GroupId = domain.GroupId,
        GroupName = domain.GroupName,
        GroupPhotoPath = domain.GroupPhotoPath,
        IsVisible = domain.IsVisible,
        ManufacturerId = domain.ManufacturerId,
        MaximumOrderQuantity = domain.MaximumOrderQuantity,
        MinimumOrderQuantity = domain.MinimumOrderQuantity,
        ModificationDateUtc = domain.ModificationDateUtc,
        Name = domain.Name,
        OrderQuantityFactor = domain.OrderQuantityFactor,
        OrganizationId = domain.OrganizationId,
        OrganizationIds = domain.OrganizationIds,
        PhotoPath = domain.PhotoPath,
        RecordDateUtc = domain.RecordDateUtc,
        Shared = domain.Shared,
        Sku = domain.Sku,
        State = domain.State,
        Tags = domain.Tags,
        TenantId = domain.TenantId,
        Vat = domain.Vat
    )
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}