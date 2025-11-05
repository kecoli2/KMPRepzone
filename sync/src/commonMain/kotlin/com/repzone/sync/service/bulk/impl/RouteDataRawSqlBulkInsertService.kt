package com.repzone.sync.service.bulk.impl

import com.repzone.core.model.ResourceUI
import com.repzone.core.util.extensions.fromResource
import com.repzone.data.mapper.SyncRouteAppointmentEntityDbMapper
import com.repzone.data.util.MapperDto

import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.database.SyncRouteAppointmentEntityMetadata
import com.repzone.database.toSqlValuesString
import com.repzone.domain.model.SyncRouteAppointmentModel
import com.repzone.network.dto.RouteDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_rota

class RouteDataRawSqlBulkInsertService(private val dbMapper: SyncRouteAppointmentEntityDbMapper,
                                       coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<RouteDto>>(coordinator) {
    //region Public Method
    override fun buildCompositeOperation(items: List<RouteDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val routeList = items.map { dbMapper.fromDto(it) }
        return CompositeOperation(
            operations = listOf(
                TableOperation(
                    tableName = SyncRouteAppointmentEntityMetadata.tableName,
                    clearSql = null,
                    columns = SyncRouteAppointmentEntityMetadata.columns,
                    values = routeList.map { it.toSqlValuesString() },
                    recordCount = routeList.size,
                    useUpsert = useUpsert,
                    includeClears = includeClears
                )
            ),
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_rota)
            )
        )
    }
    //endregion

}