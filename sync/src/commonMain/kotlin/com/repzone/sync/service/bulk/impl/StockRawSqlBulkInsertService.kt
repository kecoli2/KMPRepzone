package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncStockEntityDbMapper
import com.repzone.database.metadata.SyncStockEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.SyncStockDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_stock

class StockRawSqlBulkInsertService(private val mapper: SyncStockEntityDbMapper,
                                   coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncStockDto>>(coordinator) {
    //region Field
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncStockDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val stock = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncStockEntityMetadata.tableName,
                clearSql = null,
                columns = SyncStockEntityMetadata.columns.map { it.name },
                values = stock.map { it.toSqlValuesString() },
                recordCount = stock.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_product_stock)
            )
        )
    }
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
}