package com.repzone.sync.service.bulk.impl

import com.repzone.data.util.MapperDto
import com.repzone.database.SyncCustomerEmailEntity
import com.repzone.database.SyncCustomerEmailEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncCustomerEmailModel
import com.repzone.network.dto.CustomerEmailDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class CustomerEmailRawSqlBulkInsertService(private val mapper: MapperDto<SyncCustomerEmailEntity, SyncCustomerEmailModel, CustomerEmailDto>,
                                           coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<CustomerEmailDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun buildCompositeOperation(items: List<CustomerEmailDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val emails = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncCustomerEmailEntityMetadata.tableName,
                clearSql = null,
                columns = SyncCustomerEmailEntityMetadata.columns,
                values = emails.map { it.toSqlValuesString() },
                recordCount = emails.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = "Tüm Müşteri Emailleri Sync Yapildi"
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}