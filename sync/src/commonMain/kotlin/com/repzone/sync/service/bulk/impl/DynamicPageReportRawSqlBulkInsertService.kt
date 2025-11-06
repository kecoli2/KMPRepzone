package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncDynamicPageReportEntityDbMapper
import com.repzone.database.metadata.SyncDynamicPageReportEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.DynamicPageReportDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_dynamic_event_reasons

class DynamicPageReportRawSqlBulkInsertService(private val mapper: SyncDynamicPageReportEntityDbMapper,
                                               coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<DynamicPageReportDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation(items: List<DynamicPageReportDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val pages = items.map { mapper.fromDto(it) }
        val operations = listOf(
            TableOperation(
                tableName = SyncDynamicPageReportEntityMetadata.tableName,
                clearSql = null,
                columns = SyncDynamicPageReportEntityMetadata.columns.map { it.name },
                values = pages.map { it.toSqlValuesString() },
                includeClears = includeClears,
                useUpsert = useUpsert,
                recordCount = pages.size
            )
        )

        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_dynamic_event_reasons)
            )
        )

    }
    //endregion

}