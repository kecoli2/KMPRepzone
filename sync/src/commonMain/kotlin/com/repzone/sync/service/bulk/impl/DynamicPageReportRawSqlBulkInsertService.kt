package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDynamicPageReportEntity
import com.repzone.database.SyncDynamicPageReportEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncDynamicPageReportModel
import com.repzone.network.dto.DynamicPageReportDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_dynamic_event_reasons

class DynamicPageReportRawSqlBulkInsertService(private val mapper: MapperDto<SyncDynamicPageReportEntity, SyncDynamicPageReportModel, DynamicPageReportDto>,
    coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<DynamicPageReportDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation(items: List<DynamicPageReportDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val pages = items.map { mapper.fromDto(it) }
        val operations = listOf(
            TableOperation(
                tableName = SyncDynamicPageReportEntityMetadata.tableName,
                clearSql = null,
                columns = SyncDynamicPageReportEntityMetadata.columns,
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