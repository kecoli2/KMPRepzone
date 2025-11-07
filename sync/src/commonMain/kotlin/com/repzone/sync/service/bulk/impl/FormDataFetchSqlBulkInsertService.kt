package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toJsonOrDefault
import com.repzone.database.SyncFormBaseEntity
import com.repzone.database.metadata.SyncFormBaseEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.form.FormBaseDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_form_definations
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class FormDataFetchSqlBulkInsertService(coordinator: TransactionCoordinator
): CompositeRawSqlBulkInsertService<List<FormBaseDto>>(coordinator) {

    //region Public Method
    override fun buildCompositeOperation(items: List<FormBaseDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        items.forEach {
            it.formRows = it.formRows.sortedBy { p -> p.order }
        }

        val mobileSync = items.map { form ->
            SyncFormBaseEntity(
                Id = form.id.toLong(),
                Data = form.toJsonOrDefault(),
                Description = form.name,
                DocumentTypeId = form.formDocumentType?.enumToLong(),
                FormId = form.id.toLong(),
                FormName = form.schemaName,
                ModificationDateUtc = form.modificationDateUtc?.toEpochMilliseconds(),
                RecordDateUtc = form.recordDateUtc?.toEpochMilliseconds(),
                State = form.state.toLong(),
                VisibleOption = form.visibleOption?.enumToLong()
            )
        }

        val operation = listOf(
            TableOperation(
                tableName = SyncFormBaseEntityMetadata.tableName,
                clearSql = "DELETE FROM ${SyncFormBaseEntityMetadata.tableName}",
                columns = SyncFormBaseEntityMetadata.columns.map { it.name },
                values = mobileSync.map { it.toSqlValuesString() },
                recordCount = mobileSync.size,
                useUpsert = useUpsert,
                includeClears = true
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_form_definations)
            )
        )
    }
    //endregion

}