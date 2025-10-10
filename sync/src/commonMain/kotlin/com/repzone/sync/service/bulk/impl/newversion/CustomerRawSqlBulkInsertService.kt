package com.repzone.sync.service.bulk.impl.newversion

import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.database.SyncAddressEntityMetadata
import com.repzone.database.SyncCustomerEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.CustomerDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class CustomerRawSqlBulkInsertService(private val mapper: CustomerEntityDbMapper, coordinator: TransactionCoordinator):
    CompositeRawSqlBulkInsertService<List<CustomerDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

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
            description = "Tüm Müşteriler Sync Yapıldı"
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}