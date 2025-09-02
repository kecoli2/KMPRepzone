package com.repzone.firebase.manager

import com.google.firebase.database.FirebaseDatabase
import com.repzone.core.util.IFireBaseRealtimeDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FireBaseRealtimeDatabaseImp(private val db: FirebaseDatabase = FirebaseDatabase.getInstance()): IFireBaseRealtimeDatabase {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun set(path: String, value: String?) {
        db.getReference(path).setValue(value)
    }

    override fun observe(path: String) = callbackFlow {
        val ref = db.getReference(path)
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(s: com.google.firebase.database.DataSnapshot) {
                trySend(s.getValue(String::class.java) ?: "")
            }
            override fun onCancelled(e: com.google.firebase.database.DatabaseError) {
                close(e.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}