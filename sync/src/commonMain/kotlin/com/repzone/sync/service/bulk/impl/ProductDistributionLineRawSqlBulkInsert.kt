package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncProductDistributionLineEntityDbMapper
import com.repzone.database.metadata.SyncProductDistributionLineEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.transactioncoordinator.CompositeOperation
import com.repzone.domain.transactioncoordinator.ITransactionCoordinator
import com.repzone.domain.transactioncoordinator.TableOperation
import com.repzone.network.dto.SyncProductDistributionLineDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_productdistribution_lines

class ProductDistributionLineRawSqlBulkInsert(private val mapper: SyncProductDistributionLineEntityDbMapper,
                                              coordinator: ITransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncProductDistributionLineDto>>(coordinator)  {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncProductDistributionLineDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val list = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncProductDistributionLineEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductDistributionLineEntityMetadata.columns.map { it.name },
                values = list.map { it.toSqlValuesString() },
                recordCount = list.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_productdistribution_lines)
            )
        )
    }
    //endregion Public Method
}