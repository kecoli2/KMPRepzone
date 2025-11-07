package com.repzone.sync.service.bulk.impl

import com.repzone.core.enums.OrderStatus
import com.repzone.core.model.ResourceUI
import com.repzone.database.FormLogInformationEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.metadata.FormLogInformationEntityMetadata
import com.repzone.database.runtime.select
import com.repzone.database.toSqlValuesString
import com.repzone.network.dto.SyncMandatoryFormDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.job_complate_template_desc
import repzonemobile.core.generated.resources.job_form_definations
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class FormMandatoryDataRawSqlBulkInsertService(coordinator: TransactionCoordinator, private val iDatabaseManager: IDatabaseManager
): CompositeRawSqlBulkInsertService<List<SyncMandatoryFormDto>>(coordinator) {
    //region Public Method
    override suspend fun buildCompositeOperation(items: List<SyncMandatoryFormDto>, includeClears: Boolean, useUpsert: Boolean): CompositeOperation {
        val logForITem = ArrayList<FormLogInformationEntity>()

        items.forEach { it->
            var log = iDatabaseManager.getSqlDriver().select<FormLogInformationEntity> {
                where {
                    criteria("FormId", it.formId)
                    criteria("CustomerId", it.customerId)
                    criteria("State", notEqual = OrderStatus.DRAFT.ordinal)
                    criteria("Version", it.version)
                }
            }.firstOrNull()

            if(log == null){
                log = FormLogInformationEntity(
                    Id = 0,
                    CustomerId = it.customerId.toLong(),
                    CustomerName = it.customerName,
                    FormId = it.formId.toLong(),
                    IsListView = null,
                    LastOpenedPage = null,
                    RecordDate = it.recordDateUtc?.toEpochMilliseconds(),
                    RestServiceTaskDoneDate = null,
                    RestServiceTaskId = null,
                    RestServiceTaskObject = null,
                    RouteAppointmentId = null,
                    Status = 0,
                    Version = it.version,
                    visitUniqueId = null
                )
            }else{
                log = log.copy(
                    RecordDate = it.recordDateUtc?.toEpochMilliseconds()
                )
            }

            logForITem.add(log)
        }

        val operations = listOf(
            TableOperation(
                tableName = FormLogInformationEntityMetadata.tableName,
                clearSql = null,
                columns = FormLogInformationEntityMetadata.columns.map { it.name },
                values = logForITem.map { it.toSqlValuesString() },
                recordCount = logForITem.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operations,
            description = ResourceUI(
                res = Res.string.job_complate_template_desc,
                args = listOf(Res.string.job_form_definations)
            )
        )
    }
    //endregion
}