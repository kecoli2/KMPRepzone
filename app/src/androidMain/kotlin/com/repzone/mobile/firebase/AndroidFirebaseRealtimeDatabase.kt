package com.repzone.mobile.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.core.platform.Logger
import com.repzone.domain.common.DomainException
import kotlinx.coroutines.tasks.await
import com.repzone.domain.common.Result
import com.repzone.domain.model.DailyOperationLogInformationModel
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.service.ILocationService


class AndroidFirebaseRealtimeDatabase : IFirebaseRealtimeDatabase {

    //region Field
    private val database = FirebaseDatabase.getInstance()
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

    override suspend fun sendToFirebase(data: DailyOperationLogInformationModel): Result<Boolean> {
        //val location  = iLocationManager.getLastKnownLocation()
        return Result.Success(true)
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
    //endregion Public Method

    //region Private Method
    private fun connectLocationDatabaseAndSendLocation(model: GpsLocation): Boolean {

        return true
    }
    //endregion Private Method
}