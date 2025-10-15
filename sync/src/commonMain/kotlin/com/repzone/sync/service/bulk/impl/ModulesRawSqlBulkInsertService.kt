package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncPackageCustomFieldEntity
import com.repzone.database.SyncPackageCustomFieldEntityMetadata
import com.repzone.database.SyncPackageCustomFieldProductEntity
import com.repzone.database.SyncPackageCustomFieldProductEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncPackageCustomFieldModel
import com.repzone.domain.model.SyncPackageCustomFieldProductModel
import com.repzone.network.dto.PackageCustomFieldDto
import com.repzone.network.dto.PackageCustomFieldProductDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_product_group

class ModulesRawSqlBulkInsertService(private val mapperCustomField: MapperDto<SyncPackageCustomFieldEntity, SyncPackageCustomFieldModel, PackageCustomFieldDto>,
    private val mapperCustomFieldProduct: MapperDto<SyncPackageCustomFieldProductEntity, SyncPackageCustomFieldProductModel, PackageCustomFieldProductDto>, coordinator: TransactionCoordinator)
    : CompositeRawSqlBulkInsertService<List<PackageCustomFieldDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun buildCompositeOperation(items: List<PackageCustomFieldDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val customField = items.map { mapperCustomField.fromDto(it) }
        val customFieldProduct = items.flatMap { it.fields }.map { mapperCustomFieldProduct.fromDto(it) }

        val operations = listOf(
            TableOperation(
                tableName = SyncPackageCustomFieldEntityMetadata.tableName,
                clearSql = null,
                columns = SyncPackageCustomFieldEntityMetadata.columns,
                values = customField.map { it.toSqlValuesString() },
                recordCount = customField.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            ),

            TableOperation(
                tableName = SyncPackageCustomFieldProductEntityMetadata.tableName,
                clearSql = null,
                columns = SyncPackageCustomFieldProductEntityMetadata.columns,
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

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}