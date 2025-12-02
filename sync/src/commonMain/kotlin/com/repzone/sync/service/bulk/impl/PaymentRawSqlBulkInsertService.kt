package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncPaymentPlanEntityDbMapper
import com.repzone.database.metadata.SyncPaymentPlanEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.SyncPaymentPlanDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.domain.transactioncoordinator.CompositeOperation
import com.repzone.domain.transactioncoordinator.TableOperation
import com.repzone.data.transactioncoordinator.TransactionCoordinator
import com.repzone.domain.transactioncoordinator.ITransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_email

class PaymentRawSqlBulkInsertService(private val mapper: SyncPaymentPlanEntityDbMapper,
                                     coordinator: ITransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncPaymentPlanDto>>(coordinator) {
    //region Field
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncPaymentPlanDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val payments = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncPaymentPlanEntityMetadata.tableName,
                clearSql = null,
                columns = SyncPaymentPlanEntityMetadata.columns.map { it.name },
                values = payments.map { it.toSqlValuesString() },
                recordCount = payments.size,
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
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
}