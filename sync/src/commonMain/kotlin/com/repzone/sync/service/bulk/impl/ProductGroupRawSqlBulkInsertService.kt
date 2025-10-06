package com.repzone.sync.service.bulk.impl

import com.repzone.data.mapper.SyncProductGroupEntityDbMapper
import com.repzone.database.SyncProductGroupEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class ProductGroupRawSqlBulkInsertService(private val dbMapper: SyncProductGroupEntityDbMapper,
                                          coordinator: TransactionCoordinator
): CompositeRawSqlBulkInsertService<List<ServiceProductGroupDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override fun buildCompositeOperation(items: List<ServiceProductGroupDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val productGroupEntities = items.map { dbMapper.fromDto(it) }

        val operations = listOf(
            TableOperation(
                tableName = SyncProductGroupEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductGroupEntityMetadata.columns,
                values = productGroupEntities.map { it.toSqlValuesString() },
                recordCount = productGroupEntities.size,
                useUpsert = true,
                includeClears = false
            )

        )
        return CompositeOperation(
            operations = operations,
            description = "Product Group Fetch..."
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}