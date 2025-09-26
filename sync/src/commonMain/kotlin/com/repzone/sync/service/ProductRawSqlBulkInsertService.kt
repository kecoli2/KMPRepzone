package com.repzone.sync.service

import com.repzone.data.mapper.ProductEntityDbMapper
import com.repzone.domain.model.SyncProductModel
import com.repzone.sync.service.bulk.base.RawSqlBulkInsertService
import com.repzone.sync.transaction.TransactionCoordinator

class ProductRawSqlBulkInsertService(private val dbMapper: ProductEntityDbMapper,
                                     coordinator: TransactionCoordinator): RawSqlBulkInsertService<SyncProductModel>(coordinator) {
    //region Field
    override val tableName = "SyncProductEntity"
    override val insertColumns = listOf(
        "Id",
        "BrandId",
        "BrandName",
        "BrandPhotoPath",
        "CloseToReturns",
        "CloseToSales",
        "Color",
        "Description",
        "DisplayOrder",
        "GroupId",
        "GroupName",
        "GroupPhotoPath",
        "IsVisible",
        "ManufacturerId",
        "MaximumOrderQuantity",
        "MinimumOrderQuantity",
        "ModificationDateUtc",
        "Name",
        "OrderQuantityFactor",
        "OrganizationId",
        "OrganizationIds",
        "PhotoPath",
        "RecordDateUtc",
        "Shared",
        "Sku",
        "State",
        "Tags",
        "TenantId",
        "Vat")

    override val clearSql = "DELETE FROM products"
    //endregion

    //region Properties
    //endregion

    //region Constructor

    //endregion

    //region Public Method
    override fun getValues(item: SyncProductModel): List<Any?>{
        val dbItem = dbMapper.fromDomain(item)
        return listOf(
            dbItem.Id,
            dbItem.BrandId,
            dbItem.BrandName,
            dbItem.BrandPhotoPath,
            dbItem.CloseToReturns,
            dbItem.CloseToSales,
            dbItem.Color,
            dbItem.Description,
            dbItem.DisplayOrder,
            dbItem.GroupId,
            dbItem.GroupName,
            dbItem.GroupPhotoPath,
            dbItem.IsVisible,
            dbItem.ManufacturerId,
            dbItem.MaximumOrderQuantity,
            dbItem.MinimumOrderQuantity,
            dbItem.ModificationDateUtc,
            dbItem.Name,
            dbItem.OrderQuantityFactor,
            dbItem.OrganizationId,
            dbItem.OrganizationIds,
            dbItem.PhotoPath,
            dbItem.RecordDateUtc,
            dbItem.Shared,
            dbItem.Sku,
            dbItem.State,
            dbItem.Tags,
            dbItem.TenantId,
            dbItem.Vat
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}