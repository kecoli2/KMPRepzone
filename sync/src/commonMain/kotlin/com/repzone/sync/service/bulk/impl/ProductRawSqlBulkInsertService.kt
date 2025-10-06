package com.repzone.sync.service.bulk.impl

import com.repzone.data.mapper.ProductEntityDtoDbMapper
import com.repzone.database.ProductParameterEntityMetadata
import com.repzone.database.SyncProductEntityMetadata
import com.repzone.database.SyncProductUnitEntityMetadata
import com.repzone.database.toSqlValuesString
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
                tableName = SyncProductEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductEntityMetadata.columns,
                values = productEntities.map { it.toSqlValuesString() },
                recordCount = productEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            ),

            TableOperation(
                tableName = SyncProductUnitEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductUnitEntityMetadata.columns,
                values = unitEntities.map { it.toSqlValuesString() },
                recordCount = unitEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            ),

            TableOperation(
                tableName = ProductParameterEntityMetadata.tableName,
                clearSql = null,
                columns = ProductParameterEntityMetadata.columns,
                values = parameterEntities.map { it.toSqlValuesString() },
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
    //endregion
}