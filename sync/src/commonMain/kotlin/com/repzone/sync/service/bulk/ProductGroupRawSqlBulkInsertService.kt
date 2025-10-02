package com.repzone.sync.service.bulk

import com.repzone.data.mapper.SyncProductGroupEntityDbMapper
import com.repzone.database.SyncProductGroupEntity
import com.repzone.network.dto.ServiceProductGroupDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import io.ktor.http.quote

class ProductGroupRawSqlBulkInsertService(private val dbMapper: SyncProductGroupEntityDbMapper,
                                          coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<ServiceProductGroupDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method

    override fun buildCompositeOperation(items: List<ServiceProductGroupDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val productGroupEntities = items.map { dbMapper.fromDto(it) }

        val operations = listOf(
            TableOperation(
                tableName = "SyncProductGroupEntity",
                clearSql = null,
                columns = listOf("Id", "IconIndex", "ModificationDateUtc", "Name", "OrganizationId", "ParentId","PhotoPath", "RecordDateUtc", "Shared", "State"),
                values = buildProductGroupValues(productGroupEntities),
                recordCount = productGroupEntities.size,
                useUpsert = true,
                includeClears = false
            )

        )
        return CompositeOperation(
            operations = operations,
            description = "Product Group Fetch..."
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun buildProductGroupValues(entities: List<SyncProductGroupEntity>): List<String> {
        return entities.map { e ->
            listOf(
                e.Id,
                e.IconIndex,
                e.ModificationDateUtc,
                e.Name?.quote(),
                e.OrganizationId,
                e.ParentId,
                e.PhotoPath?.quote(),
                e.RecordDateUtc,
                e.Shared,
                e.State
            ).joinToString(", ", prefix = "(", postfix = ")")
        }
    }
    //endregion
}