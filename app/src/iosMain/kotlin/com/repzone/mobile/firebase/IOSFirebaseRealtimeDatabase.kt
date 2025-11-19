package com.repzone.mobile.firebase

import com.repzone.core.platform.Logger
import com.repzone.domain.common.DomainException
import com.repzone.domain.firebase.IFirebaseRealtimeDatabase
import com.repzone.domain.common.Result

class IOSFirebaseRealtimeDatabase : IFirebaseRealtimeDatabase {

    // TODO: Firebase iOS SDK'yı CocoaPods ile eklendiğinde implement edilecek
    // import cocoapods.FirebaseDatabase.*

    override suspend fun writeData(path: String, data: Map<String, Any>): Result<Unit> {
        return try {
            // TODO: FIRDatabase.database().reference().child(path).setValue(data)
            Logger.d ("IOSFirebaseRealtimeDatabase","iOS Firebase Database: writeData not yet implemented")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun readData(path: String): Result<Map<String, Any>?> {
        return try {
            // TODO: FIRDatabase.database().reference().child(path).getData()
            Logger.d ("IOSFirebaseRealtimeDatabase","iOS Firebase Database: readData not yet implemented")
            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun updateData(path: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            // TODO: FIRDatabase.database().reference().child(path).updateChildValues(updates)
            Logger.d ("IOSFirebaseRealtimeDatabase","iOS Firebase Database: updateData not yet implemented")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override suspend fun deleteData(path: String): Result<Unit> {
        return try {
            // TODO: FIRDatabase.database().reference().child(path).removeValue()
            Logger.d ("IOSFirebaseRealtimeDatabase","iOS Firebase Database: deleteData not yet implemented")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DomainException.UnknownException(cause = e))
        }
    }

    override fun listenToData(path: String, onDataChange: (Map<String, Any>?) -> Unit) {
        // TODO: FIRDatabase.database().reference().child(path).observe(.value) { snapshot in
        //     onDataChange(snapshot.value as? Map<String, Any>)
        // }
        Logger.d ("IOSFirebaseRealtimeDatabase","iOS Firebase Database: listenToData not yet implemented")
    }

    override fun stopListening(path: String) {
        // TODO: reference.removeAllObservers()
        Logger.d ("IOSFirebaseRealtimeDatabase","iOS Firebase Database: stopListening not yet implemented")
    }
}