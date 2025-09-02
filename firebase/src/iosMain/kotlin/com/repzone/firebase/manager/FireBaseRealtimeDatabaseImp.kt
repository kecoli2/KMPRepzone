package com.repzone.firebase.manager

import com.repzone.core.util.IFireBaseRealtimeDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine

class FireBaseRealtimeDatabaseImp: IFireBaseRealtimeDatabase {
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
        suspendCancellableCoroutine<Unit> { cont ->
            /*db.referenceWithPath(path).setValue(value) { err, _ ->
                if (err != null) cont.resumeWith(Result.failure(NSErrorException(err)))
                else cont.resume(Unit) {}
            }*/
        }
    }

    override fun observe(path: String): Flow<String> {
        TODO("Not yet implemented")
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