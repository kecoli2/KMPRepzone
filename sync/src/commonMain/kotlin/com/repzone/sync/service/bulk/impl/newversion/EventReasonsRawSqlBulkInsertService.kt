package com.repzone.sync.service.bulk.impl.newversion

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

class EventReasonsRawSqlBulkInsertService(private val mapper: MapperDto<SyncEventReasonEntity, SyncEventReasonModel, EventReasonDto>,
    coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<EventReasonDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

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
            description = "Event Reasons Sync Yapıldı"
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}