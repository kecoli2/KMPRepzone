package com.repzone.sync.service.bulk.impl

import com.repzone.data.mapper.SyncDocumentMapProcessEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessStepEntityDbMapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncDocumentMapEntity
import com.repzone.database.SyncDocumentMapEntityMetadata
import com.repzone.database.SyncDocumentMapProcessEntityMetadata
import com.repzone.database.SyncDocumentMapProcessStepEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncDocumentMapModel
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class DocumentMapsRawSqlBulkInsertService(private val mapperDocument: MapperDto<SyncDocumentMapEntity, SyncDocumentMapModel, DocumentMapModelDto>,
                                          private val mapperProcess: SyncDocumentMapProcessEntityDbMapper,
                                          private val mapperProcessStep: SyncDocumentMapProcessStepEntityDbMapper, coordinator: TransactionCoordinator
): CompositeRawSqlBulkInsertService<List<DocumentMapModelDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun buildCompositeOperation(items: List<DocumentMapModelDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val documents = items.map { mapperDocument.fromDto(it) }
        val process = items.flatMap {it -> it.process?.map { mapperProcess.fromDto(it, it.name) } ?: emptyList() }
        val processStep = items.flatMap { it -> it.process?.flatMap { stepp -> stepp.steps?.map { mapperProcessStep.fromDto(it, it.id) } ?: emptyList() } ?: emptyList()}

        val operations = listOf(
            TableOperation(
                tableName = SyncDocumentMapEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentMapEntityMetadata.columns,
                values = documents.map { it.toSqlValuesString() },
                recordCount = documents.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
            ),
            TableOperation(
                tableName = SyncDocumentMapProcessEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentMapProcessEntityMetadata.columns,
                values = process.map { it.toSqlValuesString() },
                recordCount = process.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
                includeOtherTableCount = false
            ),
            TableOperation(
                tableName = SyncDocumentMapProcessStepEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentMapProcessStepEntityMetadata.columns,
                values = processStep.map { it.toSqlValuesString() },
                recordCount = processStep.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
                includeOtherTableCount = false
            )
        )

        return CompositeOperation(
            operations = operations,
            description = "Tüm Doküman Tipleri Sync Yapıldı"
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}