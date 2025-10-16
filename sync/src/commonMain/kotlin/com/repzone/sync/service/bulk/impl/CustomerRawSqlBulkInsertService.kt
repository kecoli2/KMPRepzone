package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.database.SyncAddressEntityMetadata
import com.repzone.database.SyncCustomerEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.CustomerDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_customer

class CustomerRawSqlBulkInsertService(private val mapper: CustomerEntityDbMapper, coordinator: TransactionCoordinator):
    CompositeRawSqlBulkInsertService<List<CustomerDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation( items: List<CustomerDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val customerEntity = items.map { mapper.fromDto(it) }
        val addresstEntity = items.flatMap { it ->
            mapper.fromDtoAdress(it.addresses, it.id.toLong())
        }
        
        val operation = listOf(
            TableOperation(
                tableName = SyncCustomerEntityMetadata.tableName,
                clearSql = null,
                columns = SyncCustomerEntityMetadata.columns,
                values = customerEntity.map { it.toSqlValuesString() },
                recordCount = customerEntity.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            ),
            TableOperation(
                tableName = SyncAddressEntityMetadata.tableName,
                clearSql = null,
                columns = SyncAddressEntityMetadata.columns,
                values = addresstEntity.map { it.toSqlValuesString() },
                recordCount = addresstEntity.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
                includeOtherTableCount = false
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_customer)
            )
        )
    }
    //endregion

}