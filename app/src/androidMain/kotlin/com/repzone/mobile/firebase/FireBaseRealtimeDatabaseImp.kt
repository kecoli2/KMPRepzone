package com.repzone.mobile.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.repzone.core.interfaces.IFireBaseRealtimeDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FireBaseRealtimeDatabaseImp(private val db: FirebaseDatabase = FirebaseDatabase.getInstance()): IFireBaseRealtimeDatabase {
    //region Public Method
    override suspend fun set(path: String, value: String?) {
        db.getReference(path).setValue(value)
    }

    override fun observe(path: String) = callbackFlow {
        val ref = db.getReference(path)
        val listener = object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {
                trySend(s.getValue(String::class.java) ?: "")
            }
            override fun onCancelled(e: DatabaseError) {
                close(e.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
    //endregion


}

class FireBaseRealtimeDatabaseImpFake(): IFireBaseRealtimeDatabase{
    override suspend fun set(path: String, value: String?) {

    }

    override fun observe(path: String): Flow<String> {
        return callbackFlow {
            trySend("Fake")
            awaitClose {  }
        }
    }

}