package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.ProductEntityDtoDbMapper
import com.repzone.database.ProductParameterEntity
import com.repzone.database.SyncProductEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.metadata.ProductParameterEntityMetadata
import com.repzone.database.metadata.SyncProductEntityMetadata
import com.repzone.database.metadata.SyncProductUnitEntityMetadata
import com.repzone.database.runtime.CriteriaBuilder
import com.repzone.database.runtime.delete
import com.repzone.database.runtime.select
import com.repzone.database.runtime.toSqlCriteriaWithParams
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.ProductDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_parameters


@Suppress("UNCHECKED_CAST")
class ProductRawSqlBulkInsertService(private val dbMapper: ProductEntityDtoDbMapper,
                                     coordinator: TransactionCoordinator, private val iDatabaseManager: IDatabaseManager): CompositeRawSqlBulkInsertService<List<ProductDto>>(coordinator) {

    //region Public Method
    override suspend fun buildCompositeOperation(items: List<ProductDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val productEntities = items.map { dbMapper.fromDomain(it) }
        val unitEntities = items.flatMap { dto ->
            dbMapper.toUnitEntities(dto.id.toLong(), dto.units)
        }

        val parameterEntities = items.flatMap { dto ->
            dbMapper.toParametersEnties(dto.id.toLong(), dto.parameters)
        }

        val cr = CriteriaBuilder.build {
            criteria("Id", In = parameterEntities.map { it.Id } as List<Any>?)
            criteria("ProductId", In = parameterEntities.map { it.ProductId } as List<Any>?)

        }

        val queryStr = "DELETE FROM ProductParameterEntity WHERE " + cr.toSqlCriteriaWithParams()

        val operations = listOf(
            TableOperation(
                tableName = SyncProductEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductEntityMetadata.columns.map { it.name },
                values = productEntities.map { it.toSqlValuesString() },
                recordCount = productEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            ),

            TableOperation(
                tableName = SyncProductUnitEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductUnitEntityMetadata.columns.map { it.name },
                values = unitEntities.map { it.toSqlValuesString() },
                recordCount = unitEntities.size,
                includeClears = includeClears,
                useUpsert = useUpsert
            ),

            TableOperation(
                tableName = ProductParameterEntityMetadata.tableName,
                clearSql = listOf(queryStr),
                columns = ProductParameterEntityMetadata.columns.map { it.name },
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