package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncDocumentMapEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessEntityDbMapper
import com.repzone.data.mapper.SyncDocumentMapProcessStepEntityDbMapper
import com.repzone.database.metadata.SyncDocumentMapEntityMetadata
import com.repzone.database.metadata.SyncDocumentMapProcessEntityMetadata
import com.repzone.database.metadata.SyncDocumentMapProcessStepEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.DocumentMapModelDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_document_map

class DocumentMapsRawSqlBulkInsertService(private val mapperDocument: SyncDocumentMapEntityDbMapper,
                                          private val mapperProcess: SyncDocumentMapProcessEntityDbMapper,
                                          private val mapperProcessStep: SyncDocumentMapProcessStepEntityDbMapper, coordinator: TransactionCoordinator
): CompositeRawSqlBulkInsertService<List<DocumentMapModelDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<DocumentMapModelDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val documents = items.map { mapperDocument.fromDto(it) }
        val process = items.flatMap {it -> it.process?.map { mapperProcess.fromDto(it, it.name) } ?: emptyList() }
        val processStep = items.flatMap { it -> it.process?.flatMap { stepp -> stepp.steps?.map { mapperProcessStep.fromDto(it, stepp.id) } ?: emptyList() } ?: emptyList()}

        val operations = listOf(
            TableOperation(
                tableName = SyncDocumentMapEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentMapEntityMetadata.columns.map { it.name },
                values = documents.map { it.toSqlValuesString() },
                recordCount = documents.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
            ),
            TableOperation(
                tableName = SyncDocumentMapProcessEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentMapProcessEntityMetadata.columns.map { it.name },
                values = process.map { it.toSqlValuesString() },
                recordCount = process.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
                includeOtherTableCount = false
            ),
            TableOperation(
                tableName = SyncDocumentMapProcessStepEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentMapProcessStepEntityMetadata.columns.map { it.name },
                values = processStep.map { it.toSqlValuesString() },
                recordCount = processStep.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
                includeOtherTableCount = false
            )
        )

        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_document_map)
            )
        )
    }
    //endregion
}