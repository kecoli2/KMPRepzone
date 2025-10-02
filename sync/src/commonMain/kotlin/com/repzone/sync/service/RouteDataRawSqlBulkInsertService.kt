package com.repzone.sync.service

import com.repzone.data.util.MapperDto

import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.domain.model.SyncRouteAppointmentModel
import com.repzone.network.dto.MobileRouteDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import io.ktor.http.quote

class RouteDataRawSqlBulkInsertService(private val dbMapper: MapperDto<SyncRouteAppointmentEntity, SyncRouteAppointmentModel, MobileRouteDto>,
                                       coordinator: TransactionCoordinator): CompositeRawSqlBulkInsertService<List<MobileRouteDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun buildCompositeOperation(items: List<MobileRouteDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val routeList = items.map { dbMapper.fromDto(it) }
        return CompositeOperation(
            operations = listOf(
                TableOperation(
                    tableName = "SyncRouteAppointmentEntity",
                    clearSql = null,
                    columns = listOf(
                        "Id",
                        "CustomerId",
                        "Description",
                        "EndDate",
                        "SprintId",
                        "StartDate",
                        "State"
                    ),
                    values = buildRouteValues(routeList),
                    recordCount = routeList.size,
                    useUpsert = useUpsert,
                    includeClears = includeClears
                )
            ),
            description = "Route Data Fetch..."
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun buildRouteValues(entities: List<SyncRouteAppointmentEntity>): List<String> {
        return entities.map { e ->
            listOf(
                e.Id,
                e.CustomerId,
                e.Description?.quote(),
                e.EndDate,
                e.SprintId,
                e.StartDate,
                e.State
            ).joinToString(", ", prefix = "(", postfix = ")")
        }
    }
    //endregion
}