package com.repzone.data.repository.imp

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
import com.repzone.domain.events.base.IEventBus
import com.repzone.domain.events.base.events.DomainEvent
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.VisitInformation
import com.repzone.domain.model.VisitModel
import com.repzone.domain.model.VisitReasonInformation
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.IVisitRepository
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class VisitRepositoryImpl(private val iDatabaseManager: IDatabaseManager,
                          private val mapper: VisitEntityDbMapper,
    private val iRouteAppointmentRepository: IRouteAppointmentRepository,
    private val eventBus: IEventBus): IVisitRepository {

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

    override suspend fun startVisit(customerItemModel: CustomerItemModel, info: VisitReasonInformation?) {
        //TODO DAILY OPERATIONLAR BU KISIMDA YAPILACAK VE FIREBASE ENTEGRASYONU OLACAK
        complateVisit(info)
        val model = createVisit(customerItemModel, info)
        insertRestWork(model)

        //TODO: FIREBASE VE MERKEZE LOGLAMALAR BURA BURA KONULACAK
    }

    override suspend fun complateVisit(info: VisitReasonInformation?){
        var visit = iDatabaseManager.getSqlDriver().select<VisitEntity> {
            where {
                criteria("Finish", isNull = true)
            }
        }.firstOrNull()
        if(visit == null)
            return
        //TODO LOCATION SERVICE DEN SON LOKASYON ALINACAK

        visit = visit.copy(Finish = now())
        iDatabaseManager.getSqlDriver().insertOrReplace(visit)

        //TODO FIREBASE VE MERKEZE LOGLAMALAR BURALARA KONULACAK


    }
    //endregion

    //region Private Method
    private suspend fun insertRestWork(visitInformation: VisitInformation){

    }
    private suspend fun createVisit(customerItemModel: CustomerItemModel, info: VisitReasonInformation?): VisitInformation{
        //TODO FIREBASE VE GPS ENTEGRASYONLARI BURADA OLACAKTIR
        var onAtLocation = true
        var distance = 0.0
        val routeInfo = iRouteAppointmentRepository.getRouteInformation(customerItemModel.appointmentId)!!
        if(customerItemModel.latitude > 0 && customerItemModel.longitude > 0){
            onAtLocation = false
            distance = 10.0
        }
        val isItOnRoute = (((routeInfo.start?.compareTo(now().toInstant()) ?: 0) > 0))
        val isItOneTime = (((routeInfo.start?.compareTo(now().toInstant()) ?: 0) < 0) && ((routeInfo.end?.compareTo(now().toInstant()) ?: 0) > 0))

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
            latitude = 0.0,
            longitude = 0.0,
            orderIdsRaw = null,
            selectedCustomerOrganizationId = null,
            start = now().toInstant(),
            visitNote = info?.reason ?: "",
            visitType = info?.selectedVisitType?.enumToLong() ?: 0L
        )
        val visitId = iDatabaseManager.getSqlDriver().insert(mapper.fromDomain(visit))

        eventBus.publish(DomainEvent.VisitStartEvent(
            visitId = visitId,
            customerId = customerItemModel.customerId,
            appointmentId = customerItemModel.appointmentId
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