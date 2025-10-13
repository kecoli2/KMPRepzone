package com.repzone.database.interfaces

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.AppDatabase

interface IDatabaseManager {
    suspend fun getDatabase(): AppDatabase
    suspend fun switchUser(userId: Int)
    suspend fun logout()
    suspend fun deleteUserData(userId: Int)

    suspend fun getSqlDriver(): SqlDriver
}