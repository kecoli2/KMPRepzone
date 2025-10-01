package com.repzone.firebase.manager

import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FireBaseRealtimeDatabaseImp : IFireBaseRealtimeDatabase {
    //region Field
    //TODO:
    //private val db = Database.database()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun set(path: String, value: String?) {
        // iOS için gerçek implementasyon yoksa boş bırakılabilir
    }

    override fun observe(path: String): Flow<String> {
        return callbackFlow {
            trySend("iOS Fake Value")
            awaitClose { }
        }
    }

    /*override fun observe(path: String) = kotlinx.coroutines.flow.callbackFlow<String> {
        val ref = db.referenceWithPath(path)
        val handle = ref.observe(.value) { snap in
            trySend(snap.value as? String ?: "")
    }
        awaitClose { ref.removeObserverWithHandle(handle) }
    }*/
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}