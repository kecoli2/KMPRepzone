package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncRepresentativeProductDistributionEntityDbMapper
import com.repzone.database.metadata.SyncRepresentativeProductDistributionEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.transactioncoordinator.CompositeOperation
import com.repzone.domain.transactioncoordinator.ITransactionCoordinator
import com.repzone.domain.transactioncoordinator.TableOperation
import com.repzone.network.dto.SyncRepresentativeProductDistributionDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_RepresentativetDistributions
import repzonemobile.core.generated.resources.job_complate_template_desc

class RepresentativeProductDistributionRawSqlBulkService(private val mapper: SyncRepresentativeProductDistributionEntityDbMapper,
                                                         coordinator: ITransactionCoordinator): CompositeRawSqlBulkInsertService<List<SyncRepresentativeProductDistributionDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncRepresentativeProductDistributionDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val list = items.map { mapper.fromDto(it) }
        val operation = listOf(
            TableOperation(
                tableName = SyncRepresentativeProductDistributionEntityMetadata.tableName,
                clearSql = null,
                columns = SyncRepresentativeProductDistributionEntityMetadata.columns.map { it.name },
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
                args = listOf(Res.string.job_RepresentativetDistributions)
            )
        )
    }
    //endregion Public Method
}