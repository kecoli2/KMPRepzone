package com.repzone.sync.service.bulk.impl

import com.repzone.core.enums.CrmParameterEntityType
import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncCrmPriceListParameterEntityDbMapper
import com.repzone.database.metadata.SyncCrmPriceListParameterEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.CrmPriceListParameterDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_customer_price

class CustomerGroupPriceParameterslRawSqlBulkInsertService(private val mapper: SyncCrmPriceListParameterEntityDbMapper,
                                                           coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<CrmPriceListParameterDto>>(coordinator) {
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
                columns = SyncCrmPriceListParameterEntityMetadata.columns.map { it.name },
                values = parameters.map { it.toSqlValuesString() },
                recordCount = parameters.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_customer_price)
            )
        )
    }
    //endregion

}