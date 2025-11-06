package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.data.mapper.SyncPackageCustomFieldEntityDbMapper
import com.repzone.data.mapper.SyncPackageCustomFieldProductEntityDbMapper
import com.repzone.database.metadata.SyncPackageCustomFieldEntityMetadata
import com.repzone.database.metadata.SyncPackageCustomFieldProductEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_group

class ModulesRawSqlBulkInsertService(private val mapperCustomField: SyncPackageCustomFieldEntityDbMapper,
                                     private val mapperCustomFieldProduct: SyncPackageCustomFieldProductEntityDbMapper, coordinator: TransactionCoordinator)
    : CompositeRawSqlBulkInsertService<List<PackageCustomFieldDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation(items: List<PackageCustomFieldDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val customField = items.map { mapperCustomField.fromDto(it) }
        val customFieldProduct = items.flatMap { it.fields }.map { mapperCustomFieldProduct.fromDto(it) }

        val operations = listOf(
            TableOperation(
                tableName = SyncPackageCustomFieldEntityMetadata.tableName,
                clearSql = null,
                columns = SyncPackageCustomFieldEntityMetadata.columns.map { it.name },
                values = customField.map { it.toSqlValuesString() },
                recordCount = customField.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            ),

            TableOperation(
                tableName = SyncPackageCustomFieldProductEntityMetadata.tableName,
                clearSql = null,
                columns = SyncPackageCustomFieldProductEntityMetadata.columns.map { it.name },
                values = customFieldProduct.map { it.toSqlValuesString() },
                recordCount = customFieldProduct.size,
                useUpsert = useUpsert,
                includeClears = includeClears,
                includeOtherTableCount = false
            )
        )

        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_product_group)
            )
        )
    }
    //endregion

}