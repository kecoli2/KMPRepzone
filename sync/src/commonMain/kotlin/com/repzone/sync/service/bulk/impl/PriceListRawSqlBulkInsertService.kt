package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncProductPricesEntityDbMapper
import com.repzone.database.metadata.SyncProductPricesEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.transactioncoordinator.CompositeOperation
import com.repzone.domain.transactioncoordinator.ITransactionCoordinator
import com.repzone.domain.transactioncoordinator.TableOperation
import com.repzone.network.dto.SyncProductPricesDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_prices

class PriceListRawSqlBulkInsertService(private val mapper: SyncProductPricesEntityDbMapper,
                                       coordinator: ITransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncProductPricesDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncProductPricesDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val list = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncProductPricesEntityMetadata.tableName,
                clearSql = null,
                columns = SyncProductPricesEntityMetadata.columns.map { it.name },
                values = list.map { it.toSqlValuesString() },
                recordCount = list.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_product_prices)
            )
        )
    }
    //endregion Public Method
}