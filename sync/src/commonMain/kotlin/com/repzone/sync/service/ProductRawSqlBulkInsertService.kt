package com.repzone.sync.service

import com.repzone.core.util.extensions.quote
import com.repzone.data.mapper.ProductEntityDtoDbMapper
import com.repzone.database.ProductParameterEntity
import com.repzone.database.SyncProductEntity
import com.repzone.database.SyncProductUnitEntity
import com.repzone.network.dto.MobileProductDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class ProductRawSqlBulkInsertService(private val dbMapper: ProductEntityDtoDbMapper,
                                     coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<MobileProductDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor

    //endregion

    //region Public Method

    override fun buildCompositeOperation(items: List<MobileProductDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val productEntities = items.map { dbMapper.fromDomain(it) }
        val unitEntities = items.flatMap { dto ->
            dbMapper.toUnitEntities(dto.id.toLong(), dto.units)
        }

        val parameterEntities = items.flatMap { dto ->
            dbMapper.toParametersEnties(dto.id.toLong(), dto.parameters)
        }

        val operations = listOf(
            TableOperation(
                tableName = "SyncProductEntity",
                clearSql = "DELETE FROM SyncProductUnitEntity",
                columns = listOf("Id", "BrandId", "BrandName", "BrandPhotoPath", "CloseToReturns", "CloseToSales", "Color", "Description", "DisplayOrder", "GroupId","GroupName",
                    "GroupPhotoPath", "IsVisible", "ManufacturerId", "MaximumOrderQuantity", "MinimumOrderQuantity", "ModificationDateUtc", "Name", "OrderQuantityFactor", "OrganizationId",
                    "OrganizationIds", "PhotoPath", "RecordDateUtc", "Shared", "Sku", "State", "Tags", "TenantId", "Vat"),
                values = buildProductValues(productEntities),
                recordCount = productEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            ),

            TableOperation(
                tableName = "SyncProductUnitEntity",
                clearSql = "DELETE FROM SyncProductUnitEntity",
                columns = listOf("Id", "Barcode", "DisplayOrder", "IsVisible", "MaxOrderQuantity", "MinimumOrderQuantity", "ModificationDateUtc", "Multiplier", "OrderQuantityFactor", "Price",
                    "PriceId", "ProductId", "PurchaseDamagedReturnPrice", "PurchasePrice", "PurchaseReturnPrice", "RecordDateUtc", "SalesDamagedReturnPrice", "SalesReturnPrice", "Selected", "State",
                    "TenantId", "UnitId", "Weight"),
                values = buildUnitValues(unitEntities),
                recordCount = unitEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            ),

            TableOperation(
                tableName = "ProductParameterEntity",
                clearSql = "DELETE FROM ProductParameterEntity",
                columns = listOf("Color", "Id", "IsVisible", "Order", "OrganizationId", "ProductId"),
                values = buildParameterValues(parameterEntities),
                recordCount = parameterEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            )
        )

        val compositeOperation = CompositeOperation(
            operations = operations,
            description = "Tüm Product Sync yapıldı Unity,Parameters,Product"
        )

        return compositeOperation
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun buildProductValues(entities: List<SyncProductEntity>): List<String> {
        return entities.map { e ->
            listOf(
                e.Id,
                e.BrandId,
                e.BrandName?.quote(),
                e.BrandPhotoPath?.quote(),
                e.CloseToReturns,
                e.CloseToSales,
                e.Color?.quote(),
                e.Description?.quote(),
                e.DisplayOrder,
                e.GroupId,
                e.GroupName?.quote(),
                e.GroupPhotoPath?.quote(),
                e.IsVisible,
                e.ManufacturerId,
                e.MaximumOrderQuantity,
                e.MinimumOrderQuantity,
                e.ModificationDateUtc,
                e.Name?.quote(),
                e.OrderQuantityFactor,
                e.OrganizationId,
                e.OrganizationIds?.quote(),
                e.PhotoPath?.quote(),
                e.RecordDateUtc,
                e.Shared,
                e.Sku?.quote(),
                e.State,
                e.Tags?.quote(),
                e.TenantId,
                e.Vat
            ).joinToString(", ", prefix = "(", postfix = ")")
        }
    }
    private fun buildUnitValues(entities: List<SyncProductUnitEntity>): List<String> {
        return entities.map { e ->
            listOf(
                e.Id,
                e.Barcode?.quote(),
                e.DisplayOrder,
                e.IsVisible,
                e.MaxOrderQuantity,
                e.MinimumOrderQuantity,
                e.ModificationDateUtc,
                e.Multiplier,
                e.OrderQuantityFactor,
                e.Price,
                e.PriceId,
                e.ProductId,
                e.PurchaseDamagedReturnPrice,
                e.PurchasePrice,
                e.PurchaseReturnPrice,
                e.RecordDateUtc,
                e.SalesDamagedReturnPrice,
                e.SalesReturnPrice,
                e.Selected,
                e.State,
                e.TenantId,
                e.UnitId,
                e.Weight
            ).map { it ?: "NULL" }
                .joinToString(", ", prefix = "(", postfix = ")")
        }
    }
    private fun buildParameterValues(entities: List<ProductParameterEntity>): List<String> {
        return entities.map { e ->
            listOf(
                e.Color?.quote(),
                e.Id,
                e.IsVisible,
                e.Order,
                e.OrganizationId,
                e.ProductId
            ).map { it ?: "NULL" }
                .joinToString(", ", prefix = "(", postfix = ")")
        }
    }
    //endregion
}