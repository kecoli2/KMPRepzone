package com.repzone.sync.service.bulk.impl.newversion

import com.repzone.core.enums.CrmParameterEntityType
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncCrmPriceListParameterEntity
import com.repzone.database.SyncCrmPriceListParameterEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncCrmPriceListParameterModel
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class CustomerGroupPriceParameterslRawSqlBulkInsertService(private val mapper: MapperDto<SyncCrmPriceListParameterEntity, SyncCrmPriceListParameterModel, CrmPriceListParameterDto>,
                                                           coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<CrmPriceListParameterDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun buildCompositeOperation(items: List<CrmPriceListParameterDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val parameters = items.map { it ->
            it.entityType = CrmParameterEntityType.CUSTOMER_GROUP
            mapper.fromDto(it)
        }
        val operation = listOf(
            TableOperation(
                tableName = SyncCrmPriceListParameterEntityMetadata.tableName,
                clearSql = null,
                columns = SyncCrmPriceListParameterEntityMetadata.columns,
                values = parameters.map { it.toSqlValuesString() },
                recordCount = parameters.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = "Tüm Müşteri Fiyat Parametreleri Sync Yapildi"
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}