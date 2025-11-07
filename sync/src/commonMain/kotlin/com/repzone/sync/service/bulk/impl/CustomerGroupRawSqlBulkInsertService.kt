package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncCustomerGroupEntityDbMapper
import com.repzone.database.metadata.SyncCustomerGroupEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.CustomerGroupDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_customer_group

class CustomerGroupRawSqlBulkInsertService(private val mapper: SyncCustomerGroupEntityDbMapper,
                                           coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<CustomerGroupDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<CustomerGroupDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val customerGroupsList = items.map { mapper.fromDto(it) }

        val operations = listOf(
            TableOperation(
                tableName = SyncCustomerGroupEntityMetadata.tableName,
                clearSql = null,
                columns = SyncCustomerGroupEntityMetadata.columns.map { it.name },
                values = customerGroupsList.map { it.toSqlValuesString() },
                recordCount = customerGroupsList.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_customer_group)
            )
        )
    }
    //endregion

}