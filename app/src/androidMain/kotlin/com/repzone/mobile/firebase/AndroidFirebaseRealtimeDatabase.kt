package com.repzone.mobile.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.repzone.core.enums.DailyOperationType
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.core.platform.Logger
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toDateString
import com.repzone.core.util.extensions.todayRange
import com.repzone.data.mapper.DailyOperationLogInformationEntityDbMapper
import com.repzone.database.DailyOperationLogInformationEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.maxByOrNull
import com.repzone.database.runtime.select
import com.repzone.domain.common.DomainException
import kotlinx.coroutines.tasks.await
import com.repzone.domain.common.Result
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.repository.IRouteAppointmentRepository
import com.repzone.domain.repository.IVisitRepository


class AndroidFirebaseRealtimeDatabase(
    private val iDatabaseManager: IDatabaseManager,
    private val mapper: DailyOperationLogInformationEntityDbMapper,
    private val iVisitRepository: IVisitRepository,
    private val iRouteAppointmentRepository: IRouteAppointmentRepository) : IFirebaseRealtimeDatabase {
    //region Field

    val fireBaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance(secondApp)
    }

    val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance(secondApp)
    }

    private val secondApp: FirebaseApp by lazy {
        val options = FirebaseOptions.Builder()
            .setApplicationId("1:821671896424:android:7a5d65cb727421f4e022dd")
            .setApiKey("AIzaSyD7SOmkAtTA6XMZb0tRMI9T53nf6kQs9gY")
            .setDatabaseUrl("https://repzone-199313.firebaseio.com")
            .setProjectId("repzone-199313")
            .setStorageBucket("repzone-199313.appspot.com")
            .build()

        FirebaseApp.initializeApp(FirebaseApp.getInstance().applicationContext, options,"SecondaryApp")
    }
    private val listeners = mutableMapOf<String, ValueEventListener>()
    //endregion Field

    //region Public Method
    override suspend fun writeData(path: String, data: Map<String, Any>): Result<Unit> {
        return try {
            database.getReference(path).setValue(data).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun readData(path: String): Result<Map<String, Any>?> {
        return try {
            val snapshot = database.getReference(path).get().await()
            @Suppress("UNCHECKED_CAST")
            val data = snapshot.value as? Map<String, Any>
            Result.Success(data)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun updateData(path: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            database.getReference(path).updateChildren(updates).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun deleteData(path: String): Result<Unit> {
        return try {
            database.getReference(path).removeValue().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun sendToFirebase(data: GpsLocation): Result<Boolean> {
        return connectLocationDatabaseAndSendLocation(data)
    }

    override fun listenToData(path: String, onDataChange: (Map<String, Any>?) -> Unit) {
        val reference = database.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                @Suppress("UNCHECKED_CAST")
                val data = snapshot.value as? Map<String, Any>
                onDataChange(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Logger.d ("IFirebaseRealtimeDatabase","Database error: ${error.message}")
                onDataChange(null)
            }
        }

        listeners[path] = listener
        reference.addValueEventListener(listener)
    }

    override fun stopListening(path: String) {
        listeners[path]?.let { listener ->
            database.getReference(path).removeEventListener(listener)
            listeners.remove(path)
        }
    }

    override suspend fun userAuthentication(email: String): Result<Unit> {
        return try {
            val result = fireBaseAuth.signInWithEmailAndPassword(email, "bilgera2018").await()
            result.user?.let {
                Result.Success(Unit)
            }
            Result.Success(Unit)
        }catch (e: Exception){
            try {
                val result = fireBaseAuth.createUserWithEmailAndPassword(email, "bilgera2018").await()
                Result.Success(Unit)
            }catch (ex: Exception){
                Result.Error(DomainException.UnknownException(cause = ex))
            }
        }
    }
    //endregion Public Method

    //region Private Method
    private suspend fun connectLocationDatabaseAndSendLocation(model: GpsLocation): Result<Boolean> {
        try {
            // Location node path
            val (todayStart, todayEnd) = todayRange()

            val today = now().toDateString("yyyy-MM-dd")
            val locNode = database.reference
                .child(today)
                .child(model.organizationId.toString())
                .child("locations")

            val newLoc = locNode.push()

            // Last location node
            val lastLocNode = database.reference
                .child(today)
                .child("live")
                .child(model.tenantId.toString())

            val lastLoc = lastLocNode.child(model.representativeId.toString())

            val map = hashMapOf(
                "Latitude" to model.latitude,
                "Longitude" to model.longitude,
                "Time" to now().toDateString("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"),
                "Accuracy" to model.accuracy,
                "Altitude" to model.altitude,
                "AltitudeAccuracy" to model.altitudeAccuracy,
                "Heading" to (model.bearing ?: 0f),
                "Speed" to (model.speed ?: 0f),
                "RepresentativeId" to model.representativeId,
                "DailyOperationType" to model.dailyOperationType.ordinal,
                "Description" to model.description,
                "ReverseGeocoded" to model.reverseGeocoded,
                "BatteryLevel" to (model.batteryLevel ?: 0),
                "TenantId" to model.tenantId,
                "TimeStamp" to ServerValue.TIMESTAMP
            )

            val lastOperation = iDatabaseManager.getSqlDriver().select<DailyOperationLogInformationEntity> {
                where {
                    criteria("Date", between = todayStart to todayEnd)
                }

                orderBy {
                    order("Date")
                }
            }.firstOrNull()?.let {
                mapper.toDomain(it)
            }

            if (model.dailyOperationType == DailyOperationType.ERROR) {
                when (lastOperation?.type) {
                    DailyOperationType.DAYOFF,
                    DailyOperationType.PAUSE,
                    DailyOperationType.VISITSTART,
                    DailyOperationType.LOGOUT -> {
                        map["DailyOperationType"] = lastOperation.type.ordinal
                    }
                    DailyOperationType.CONTINUE -> {

                        val lastOpBeforeBreak = iDatabaseManager.getSqlDriver().maxByOrNull<DailyOperationLogInformationEntity>("Date") {
                            where {
                                criteria("Date", between = todayStart to todayEnd)
                                criteria("Type",
                                    notIn = listOf(DailyOperationType.OPERATION.ordinal,
                                        DailyOperationType.PAUSE.ordinal,
                                        DailyOperationType.CONTINUE.ordinal))
                            }
                        }?.let {
                            mapper.toDomain(it)
                        }


                        when (lastOpBeforeBreak?.type) {
                            DailyOperationType.DAYOFF,
                            DailyOperationType.PAUSE,
                            DailyOperationType.VISITSTART,
                            DailyOperationType.LOGOUT -> {
                                map["DailyOperationType"] = lastOpBeforeBreak.type.ordinal
                            }
                            else -> {
                                map["DailyOperationType"] = DailyOperationType.ERROR.ordinal
                            }
                        }
                    }
                    else -> {
                        map["DailyOperationType"] = DailyOperationType.ERROR.ordinal
                    }
                }
            } else if (model.dailyOperationType == DailyOperationType.CONTINUE) {
                val lastOpBeforeBreak = iDatabaseManager.getSqlDriver().maxByOrNull<DailyOperationLogInformationEntity>("Date") {
                    where {
                        criteria("Date", between = todayStart to todayEnd)
                        criteria("Type",
                            notIn = listOf(DailyOperationType.OPERATION.ordinal,
                                DailyOperationType.PAUSE.ordinal,
                                DailyOperationType.CONTINUE.ordinal))
                    }
                }?.let {
                    mapper.toDomain(it)
                }

                when (lastOpBeforeBreak?.type) {
                    DailyOperationType.DAYOFF,
                    DailyOperationType.PAUSE,
                    DailyOperationType.VISITSTART,
                    DailyOperationType.LOGOUT -> {
                        map["DailyOperationType"] = lastOpBeforeBreak.type.ordinal
                    }
                    else -> {
                        map["DailyOperationType"] = DailyOperationType.ERROR.ordinal
                    }
                }
            }

            // Visit kontrolü

            val lastOpAboutVisit = iDatabaseManager.getSqlDriver().maxByOrNull<DailyOperationLogInformationEntity>("Date") {
                where {
                    criteria("Date", between = todayStart to todayEnd)
                    criteria("Type",
                        In = listOf(DailyOperationType.OPERATION.ordinal,
                            DailyOperationType.PAUSE.ordinal,
                            DailyOperationType.CONTINUE.ordinal))
                }
            }?.let {
                mapper.toDomain(it)
            }

            if (lastOpAboutVisit?.type == DailyOperationType.VISITSTART) {
                map["DailyOperationType"] = DailyOperationType.VISITSTART.ordinal
            }

            // VisitStart ise customer bilgilerini ekle
            if (map["DailyOperationType"] == DailyOperationType.VISITSTART.ordinal) {
                val activeVisit = iVisitRepository.getActiveVisit()
                if (activeVisit != null) {
                    val customer = iRouteAppointmentRepository.getRouteInformationForCustomer(activeVisit.appointmentId)
                    if (customer != null) {
                        val desc = map["Description"] as String? ?: ""
                        map["Description"] = "$desc|${customer.code}|${customer.name}"
                    }
                }
            }

            // Break kontrolü
            val lastOpAboutBreak = iDatabaseManager.getSqlDriver().maxByOrNull<DailyOperationLogInformationEntity>("Date") {
                where {
                    criteria("Date", between = todayStart to todayEnd)
                    criteria("Type",
                        In = listOf(DailyOperationType.PAUSE.ordinal, DailyOperationType.CONTINUE.ordinal))
                }
            }?.let {
                mapper.toDomain(it)
            }

            if (lastOpAboutBreak?.type == DailyOperationType.PAUSE) {
                map["DailyOperationType"] = DailyOperationType.PAUSE.ordinal
            }

            // Firebase'e yaz
            val result =
                try {
                    newLoc.setValue(map).await()
                    lastLoc.setValue(map).await()
                    true
                }catch (e: Exception){
                    false
                }
            return Result.Success(result)
        }catch (e: Exception){
            return Result.Error(DomainException.UnknownException(cause = e))
        }
    }
    //endregion Private Method
}