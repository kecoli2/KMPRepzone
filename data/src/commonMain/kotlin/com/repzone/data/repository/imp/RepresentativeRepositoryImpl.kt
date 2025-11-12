package com.repzone.data.repository.imp

import com.repzone.core.enums.OrderStatus
import com.repzone.core.util.extensions.enumToLong
import com.repzone.database.FormLogInformationEntity
import com.repzone.database.OrderLogInformationEntity
import com.repzone.database.SyncRouteAppointmentEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.RepresentSummary
import com.repzone.domain.repository.IRepresentativeRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RepresentativeRepositoryImpl(private val iDatabaseManager: IDatabaseManager,
                                   private val iRouteAppointmentRepository: IRouteAppointmentRepository): IRepresentativeRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getSummary(date: Instant?, routes: List<CustomerItemModel>) : RepresentSummary{
        val activeSprint = iRouteAppointmentRepository.getActiveSprintInformation()
        val sprintOrders = iDatabaseManager.getSqlDriver().select<OrderLogInformationEntity> {
            where {

                criteria("OrderDate", greaterThan = activeSprint?.startDate)
                criteria("OrderDate", lessThan = activeSprint?.endDate)
                criteria("Status", equal = OrderStatus.SENT.enumToLong())

            }
        }.toList()

        val sprintRoutes = iDatabaseManager.getSqlDriver().select<SyncRouteAppointmentEntity> {
            where {
                criteria("SprintId", activeSprint?.id)
                criteria("State", OrderStatus.SENT.enumToLong())
            }
        }.toList()

        val sprintForms = iDatabaseManager.getSqlDriver().select<FormLogInformationEntity> {
            where {
                criteria("RecordDate", greaterThan = activeSprint?.startDate)
                criteria("RecordDate", lessThan = activeSprint?.endDate)
                criteria("Status", OrderStatus.SENT.enumToLong())
            }
        }.toList()

        val summary = if (date != null){
            RepresentSummary(
                visitTotal = routes.size,
                visitDoneTotal = routes.count { it.visitId != null },
                orderCount = sprintOrders.count {  it.OrderDate == date.toEpochMilliseconds() },
                orderValue = sprintOrders.filter { it.OrderDate == date.toEpochMilliseconds() }.sumOf { it.TotalCost!! },
                formCount = sprintForms.filter { it.RecordDate == date.toEpochMilliseconds() }.size,
                activeAppoinmentDayCount = routes.groupBy { it.date }.count { it.value.isNotEmpty() }
            )
        }else{
            RepresentSummary(
                visitTotal = routes.size,
                visitDoneTotal = routes.count { it.visitId != null },
                orderCount = sprintRoutes.count(),
                orderValue = sprintOrders.filter { it.TotalCost != null }.sumOf { it.TotalCost!! },
                formCount = sprintForms.count(),
                activeAppoinmentDayCount = routes.groupBy { it.date }.count { it.value.isNotEmpty() }
            )
        }

        return summary
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}