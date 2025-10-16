package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
import com.repzone.data.mapper.ProductEntityDtoDbMapper
import com.repzone.database.ProductParameterEntityMetadata
import com.repzone.database.SyncProductEntityMetadata
import com.repzone.database.SyncProductUnitEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.ProductDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_parameters

class ProductRawSqlBulkInsertService(private val dbMapper: ProductEntityDtoDbMapper,
                                     coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<ProductDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor

    //endregion

    //region Public Method

    override fun buildCompositeOperation(items: List<ProductDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
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
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_product_parameters)
            )
        )

        return compositeOperation
    }
    //endregion

}