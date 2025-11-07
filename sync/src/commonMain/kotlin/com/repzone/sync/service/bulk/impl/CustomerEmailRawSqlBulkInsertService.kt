package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncCustomerEmailEntityDbMapper
import com.repzone.database.metadata.SyncCustomerEmailEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_email

class CustomerEmailRawSqlBulkInsertService(private val mapper: SyncCustomerEmailEntityDbMapper,
                                           coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<CustomerEmailDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<CustomerEmailDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val emails = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncCustomerEmailEntityMetadata.tableName,
                clearSql = null,
                columns = SyncCustomerEmailEntityMetadata.columns.map { it.name },
                values = emails.map { it.toSqlValuesString() },
                recordCount = emails.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_email)
            )
        )
    }
    //endregion


}