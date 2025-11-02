package com.repzone.data.repository.imp

import com.repzone.core.enums.OnOf
import com.repzone.core.enums.RepresentativeEventReasonType
import com.repzone.core.enums.VisitPlanSchedulesType
import com.repzone.core.util.extensions.addDays
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.CustomerItemViewEntity
import com.repzone.database.SyncEventReasonEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.rawQueryToEntity
import com.repzone.database.runtime.select
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.repository.ICustomerListRepository
import com.repzone.domain.repository.IEventReasonRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.offsetAt
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class CustomerListRepositoryImpl(private val iMobileModuleParameter: IMobileModuleParameterRepository,
                                 private val iRouteAppointmentRepository: IRouteAppointmentRepository,
                                 private val iEventReasonRepository: IEventReasonRepository,
                                 private val mapper: Mapper<CustomerItemViewEntity, CustomerItemModel>,
                                 private val iDatabaseManager: IDatabaseManager
): ICustomerListRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getCustomerList(utcDate: Instant?): List<CustomerItemModel> {
        val activeStrint = iRouteAppointmentRepository.getActiveSprintInformation()
        val dontShowDatePart = iMobileModuleParameter.getGeofenceRouteTrackingParameters()?.isActive == true && iMobileModuleParameter.getGeofenceRouteTrackingParameters()?.visitPlanSchedules == VisitPlanSchedulesType.FLEXIBLE_DATES
        val onlyParents = iMobileModuleParameter.getGeofenceRouteTrackingParameters()?.isActive == true && iMobileModuleParameter.getGeofenceRouteTrackingParameters()?.groupByParentCustomer == OnOf.ON
        val swipeEnable = iEventReasonRepository.getEventReasonList(RepresentativeEventReasonType.NOVISIT).count() > 0

        var listModel = iDatabaseManager.getDatabase().customerItemViewEntityQueries.selectCustomerItemViewEntity(activeStrint?.id?.toLong(), onlyParents.toLong()).executeAsList().map {
            val domain = mapper.toDomain(it)
            domain.copy(
                dontShowDatePart = dontShowDatePart,
                swipeEnabled = swipeEnable,
                showCalendarInfo = domain.addressType in listOf(0L, 4L, 2L)
            )
        }

        if(dontShowDatePart){
            listModel = listModel.sortedBy { it.date }
        }else{

            if(utcDate == null){
                val actDate = now().toInstant().addDays(28)
                listModel.filter {
                   it.date != null && it.date!! < actDate
                }.forEach { itemModel ->
                    itemModel.copy(
                        date = itemModel.date?.addDays(28)
                    )
                }
                listModel = listModel.sortedBy { it.date?.toEpochMilliseconds() ?: Long.MIN_VALUE  }
            }else {
                val utcOffsetMinutes = TimeZone.currentSystemDefault()
                    .offsetAt(Clock.System.now())
                    .totalSeconds / 60

                val localdateTime = utcDate.toLocalDateTime(TimeZone.UTC).date.atStartOfDayIn(TimeZone.UTC)

                val targetDate = localdateTime
                    .plus(utcOffsetMinutes, DateTimeUnit.MINUTE, TimeZone.UTC)
                    .toLocalDateTime(TimeZone.UTC)
                    .date

                listModel = listModel
                    .filter { item ->
                        item.date
                            ?.plus(utcOffsetMinutes, DateTimeUnit.MINUTE, TimeZone.UTC)
                            ?.toLocalDateTime(TimeZone.UTC)
                            ?.date == targetDate
                    }
                    .sortedBy { it.date?.toEpochMilliseconds() ?: Long.MIN_VALUE  }
            }
        }

        if((iMobileModuleParameter.getGeofenceRouteTrackingParameters()?.visitPlanSchedules?.ordinal?: 0) > 1){
            listModel = listModel.mapIndexed { index, item ->
                item.copy(
                    displayOrder = index + 1,
                    showDisplayOrder = true,
                    showCalendarInfo = utcDate == null,
                    showDisplayClock = if ((iMobileModuleParameter.getGeofenceRouteTrackingParameters()?.visitPlanSchedules) == VisitPlanSchedulesType.FIXED_DATES_SHOW_VISIT_ORDER_INSTEAD_OF_VIST_TIME_INTERVAL_WITHOUT_DURATION) false else item.showDisplayClock
                )
            }
        }

        return listModel
    }

    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}