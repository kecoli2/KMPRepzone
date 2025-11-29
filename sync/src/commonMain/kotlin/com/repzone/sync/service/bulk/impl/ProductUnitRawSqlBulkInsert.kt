package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncUnitEntityDbMapper
import com.repzone.database.metadata.SyncUnitEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.SyncUnitDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*

class ProductUnitRawSqlBulkInsert(private val mapper: SyncUnitEntityDbMapper,
                                  coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncUnitDto>>(coordinator) {

    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncUnitDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val unityList = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncUnitEntityMetadata.tableName,
                clearSql = null,
                columns = SyncUnitEntityMetadata.columns.map { it.name },
                values = unityList.map { it.toSqlValuesString() },
                recordCount = unityList.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_product_unit)
            )
        )
    }
    //endregion
}