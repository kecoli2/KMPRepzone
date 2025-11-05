package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
import com.repzone.data.mapper.SyncEventReasonEntityDbMapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncEventReasonEntity
import com.repzone.database.SyncEventReasonEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncEventReasonModel
import com.repzone.network.dto.EventReasonDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_module

class EventReasonsRawSqlBulkInsertService(private val mapper: SyncEventReasonEntityDbMapper,
                                          coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<EventReasonDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation(items: List<EventReasonDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val eventReasons = items.map { mapper.fromDto(it) }
        val operations = listOf(
            TableOperation(
                tableName = SyncEventReasonEntityMetadata.tableName,
                clearSql = null,
                columns = SyncEventReasonEntityMetadata.columns,
                values = eventReasons.map { it.toSqlValuesString() },
                recordCount = eventReasons.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_module)
            )
        )
    }
    //endregion

}