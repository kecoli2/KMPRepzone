package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
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
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_document_map

class DocumentMapsRawSqlBulkInsertService(private val mapperDocument: MapperDto<SyncDocumentMapEntity, SyncDocumentMapModel, DocumentMapModelDto>,
                                          private val mapperProcess: SyncDocumentMapProcessEntityDbMapper,
                                          private val mapperProcessStep: SyncDocumentMapProcessStepEntityDbMapper, coordinator: TransactionCoordinator
): CompositeRawSqlBulkInsertService<List<DocumentMapModelDto>>(coordinator) {
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
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_document_map)
            )
        )
    }
    //endregion

}