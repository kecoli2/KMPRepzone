package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncDocumentOrganizationEntityDbMapper
import com.repzone.database.metadata.SyncDocumentOrganizationEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.DocumentMapDocumentOrganizationDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_document_map

class DocumentMapsOrganizationsRawSqlBulkInsertService(private val mapperDocument: SyncDocumentOrganizationEntityDbMapper, coordinator: TransactionCoordinator
): CompositeRawSqlBulkInsertService<List<DocumentMapDocumentOrganizationDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation(items: List<DocumentMapDocumentOrganizationDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val documents = items.map { mapperDocument.fromDto(it) }


        val operations = listOf(
            TableOperation(
                tableName = SyncDocumentOrganizationEntityMetadata.tableName,
                clearSql = null,
                columns =  SyncDocumentOrganizationEntityMetadata.columns.map { it.name },
                values = documents.map { it.toSqlValuesString() },
                recordCount = documents.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
            ),
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