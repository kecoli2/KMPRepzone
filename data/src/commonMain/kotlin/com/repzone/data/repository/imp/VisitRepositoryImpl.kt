package com.repzone.data.repository.imp

import com.repzone.core.enums.DailyOperationType
import com.repzone.core.model.module.base.IModuleParametersBase
import com.repzone.core.platform.randomUUID
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toInstant
import com.repzone.core.util.extensions.toLong
import com.repzone.data.mapper.VisitEntityDbMapper
import com.repzone.database.VisitEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.count
import com.repzone.database.runtime.insert
import com.repzone.database.runtime.insertOrReplace
import com.repzone.database.runtime.select
import com.repzone.database.runtime.selectAsFlow
import com.repzone.database.runtime.update
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DomainEvent
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.DailyOperationLogInformationModel
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.model.VisitModel
import com.repzone.domain.model.VisitReasonInformation
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.repository.IDailyOparationRepository
import com.repzone.domain.repository.ILocationRepository
import com.repzone.domain.repository.IMobileModuleParameterRepository
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.IVisitRepository
import com.repzone.domain.util.distanceTo
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class VisitRepositoryImpl(private val iDatabaseManager: IDatabaseManager,
                          private val mapper: VisitEntityDbMapper,
                          private val iRouteAppointmentRepository: IRouteAppointmentRepository,
                          private val eventBus: IEventBus,
                          private val iMobileModuleParameterRepository: IMobileModuleParameterRepository,
                          private val iDailyOparationRepository: IDailyOparationRepository,
                          private val iLocationRepository: ILocationRepository): IVisitRepository {

    //region Public Method
    override suspend fun getActiveVisit(): VisitInformation? {
        val visit = iDatabaseManager.getSqlDriver().select<VisitEntity> {
            where {
                criteria("Finish", isNull = true)
            }
        }.firstOrNull()
        return if(visit == null){
            null
        }else{
            mapper.toDomainVisitInformation(visit)
        }
    }

    override suspend fun hasAnyForm(guid: String, appointmentId: Long, formName: String): Boolean {
        return iDatabaseManager.getSqlDriver().count<VisitEntity> {
            where {
                criteria("Guid", guid)
                criteria("AppointmentId", appointmentId)
                criteria("FormIdsRaw", isNull = false)
                criteria("FormIdsRaw", like = formName )
                criteria("Finish", isNull = true)
            }
        } > 0
    }

    override suspend fun startVisit(customerItemModel: CustomerItemModel, info: VisitReasonInformation?, location: GpsLocation) {
        //TODO DAILY OPERATIONLAR BU KISIMDA YAPILACAK VE FIREBASE ENTEGRASYONU OLACAK
        complateVisit(info, location)
        val model = createVisit(customerItemModel, info, location)
        insertRestWork(model)
        val lastOperation = iDailyOparationRepository.getLasLog()
        if(lastOperation != null && lastOperation.type == DailyOperationType.PAUSE){
            //TODO BURASI REST SERVISE BILGI GONDERME ADIMI
            /*            var leaveRequest = new RepresentativeLeaveUpdateModel();
            leaveRequest.UniqueId = lastOperation.RepzoneLeaveRequestUniqueId ?? "";
            leaveRequest.EndDate = DateTime.UtcNow;

            var _restServiceManager = new RestServiceManager();
            _restServiceManager.Push(new RestServiceTask
                    {
                        ActionName = "LeaveFinish",
                        CallType = MethodType.Post,
                        RequestObject = JsonConvert.SerializeObject(leaveRequest),
                        RequestType = "RepresentativeLeaveUpdateModel",
                        MethodPath = "",
                        RequestObjectUniqueID = Guid.NewGuid().ToString()
                    });*/

            val continueData = DailyOperationLogInformationModel(
                batteryLevel = location.batteryLevel?.toLong(),
                date = now().toInstant(),
                description = "",
                localTime = now().toInstant(),
                type = DailyOperationType.CONTINUE
            )
            iDailyOparationRepository.audit(continueData)
        }

        val data = DailyOperationLogInformationModel(
            date = now().toInstant(),
            localTime = now().toInstant(),
            type = DailyOperationType.VISITSTART,
            description = model.guid,
            batteryLevel = location.batteryLevel?.toLong()
        )
        iDailyOparationRepository.audit(data)

        val newLocationData = location.copy(
            timestamp = data.date?.toEpochMilliseconds() ?: now(),
            dailyOperationType = DailyOperationType.VISITSTART,
            description = data.description,
            isSynced = false,
            id = randomUUID()
        )
        iLocationRepository.saveLocation(newLocationData)
    }

    override suspend fun complateVisit(info: VisitReasonInformation?, location: GpsLocation?){
        val visitEndTime = now()
        var visit = iDatabaseManager.getSqlDriver().select<VisitEntity> {
            where {
                criteria("Finish", isNull = true)
            }
        }.firstOrNull()
        if(visit == null)
            return

        visit = visit.copy(Finish = visitEndTime)
        iDatabaseManager.getSqlDriver().insertOrReplace(visit)
        eventBus.publish(DomainEvent.VisitStoptEvent(visit.Id, visit.AppointmentId ,timestamp = visitEndTime))
        //TODO FIREBASE VE MERKEZE LOGLAMALAR BURALARA KONULACAK
    }
    //endregion

    //region Private Method
    private suspend fun insertRestWork(visitInformation: VisitInformation){

    }
    private suspend fun createVisit(customerItemModel: CustomerItemModel, info: VisitReasonInformation?, location: GpsLocation?): VisitInformation{
        //TODO FIREBASE VE GPS ENTEGRASYONLARI BURADA OLACAKTIR
        var onAtLocation = true
        var distance = 0.0
        val routeInfo = iRouteAppointmentRepository.getRouteInformation(customerItemModel.appointmentId)!!
        val nowInstant = now().toInstant()

        if(location != null){
            onAtLocation = false
            distance = location.distanceTo(latitude = customerItemModel.latitude, longitude = customerItemModel.longitude)
            if(iMobileModuleParameterRepository.getGeofenceRouteTrackingParameters()!!.isActive && iMobileModuleParameterRepository.getGeofenceRouteTrackingParameters()!!.distance > 0) {
                onAtLocation = distance > iMobileModuleParameterRepository.getGeofenceRouteTrackingParameters()!!.distance
            }
        }


        val isItOnRoute = (((routeInfo.start?.compareTo(nowInstant) ?: 0) > 0))
        val isItOneTime = (((routeInfo.start?.compareTo(nowInstant) ?: 0) < 0) && ((routeInfo.end?.compareTo(nowInstant) ?: 0) > 0))

        val visit = VisitModel(
            id = 0,
            appointmentExceptionId = 0,
            appointmentId = customerItemModel.appointmentId,
            distanceFromCustomerLocation = distance.toLong(),
            finish = null,
            formIdsRaw = null,
            guid = randomUUID(),
            isItOnLocation = onAtLocation,
            isItOnRoute = isItOnRoute,
            isItOnTime = isItOneTime,
            latitude = location?.latitude,
            longitude = location?.longitude,
            orderIdsRaw = null,
            selectedCustomerOrganizationId = null,
            start = nowInstant,
            visitNote = info?.reason ?: "",
            visitType = info?.selectedVisitType?.enumToLong() ?: 0L
        )
        val visitId = iDatabaseManager.getSqlDriver().insert(mapper.fromDomain(visit))

        eventBus.publish(DomainEvent.VisitStartEvent(
            visitId = visitId,
            customerId = customerItemModel.customerId,
            appointmentId = customerItemModel.appointmentId,
            timestamp = nowInstant.toEpochMilliseconds()
        ))

        return VisitInformation(
            appointmentId = visit.appointmentId!!,
            visitId = visitId,
            guid = visit.guid,
            latitude = visit.latitude,
            longitude = visit.longitude,
            isItOnRoute = visit.isItOnRoute,
            isItOnTime = visit.isItOnTime,
            isItOnLocation = visit.isItOnLocation,
            distanceFromCustomerLocation = visit.distanceFromCustomerLocation?.toInt() ?: 0,
            visitType = visit.visitType?.toInt() ?: 0,
            selectedCustomerOrganizationId = visit.selectedCustomerOrganizationId?.toInt() ?: 0,
            start = visit.start,
        )
    }

    //endregion

}