package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncWarehouseEntityDbMapper
import com.repzone.database.metadata.SyncWarehouseEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.transactioncoordinator.CompositeOperation
import com.repzone.domain.transactioncoordinator.ITransactionCoordinator
import com.repzone.domain.transactioncoordinator.TableOperation
import com.repzone.network.dto.SyncWarehouseDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_warehouse

class WarehousesRawSqlBulkInsertService(private val mapper: SyncWarehouseEntityDbMapper,
                                        coordinator: ITransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncWarehouseDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncWarehouseDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val items = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncWarehouseEntityMetadata.tableName,
                clearSql = null,
                columns = SyncWarehouseEntityMetadata.columns.map { it.name },
                values = items.map { it.toSqlValuesString() },
                recordCount = items.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_warehouse)
            )
        )
    }
    //endregion Public Method
}