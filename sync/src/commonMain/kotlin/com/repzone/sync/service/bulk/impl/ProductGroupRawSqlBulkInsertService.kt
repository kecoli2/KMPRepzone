package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncProductGroupEntityDbMapper
import com.repzone.database.metadata.SyncProductGroupEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.ProductGroupDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.domain.transactioncoordinator.CompositeOperation
import com.repzone.domain.transactioncoordinator.TableOperation
import com.repzone.data.transactioncoordinator.TransactionCoordinator
import com.repzone.domain.transactioncoordinator.ITransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_parameters

class ProductGroupRawSqlBulkInsertService(private val dbMapper: SyncProductGroupEntityDbMapper,
                                          coordinator: ITransactionCoordinator
): CompositeRawSqlBulkInsertService<List<ProductGroupDto>>(coordinator) {
    //region Public Method

    override suspend fun buildCompositeOperation(items: List<ProductGroupDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val productGroupEntities = items.map { dbMapper.fromDto(it) }

        val operations = listOf(
            TableOperation(
                tableName = SyncProductGroupEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductGroupEntityMetadata.columns.map { it.name },
                values = productGroupEntities.map { it.toSqlValuesString() },
                recordCount = productGroupEntities.size,
                useUpsert = true,
                includeClears = false
            )

        )
        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_product_parameters)
            )
        )
    }
    //endregion

}