package com.repzone.database.impl

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.interfaces.IUserSession
import com.repzone.database.AppDatabase
import com.repzone.database.driver.DatabaseDriverFactory
import com.repzone.database.interfaces.IDatabaseManager

class DatabaseManagerImpl(private val driverFactory: DatabaseDriverFactory,
                          private val userSession: IUserSession
): IDatabaseManager {
    //region Field
    private val databaseCache = mutableMapOf<Int, Pair<SqlDriver, AppDatabase>>()
    private var currentUserId: Int? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun getDatabase(): AppDatabase {
        val userId = userSession.getActiveSession()?.userId
            ?: error("No user logged in")

        return databaseCache.getOrPut(userId) {
            createDatabaseForUser(userId)
        }.second // Pair'in ikinci elemanı AppDatabase
    }

    override suspend fun switchUser(userId: Int) {
        currentUserId?.let { oldUserId ->
            databaseCache[oldUserId]?.first?.close() // Pair'in birinci elemanı SqlDriver
            databaseCache.remove(oldUserId)
        }
        currentUserId = userId
    }

    override suspend fun logout() {
        currentUserId?.let { userId ->
            databaseCache[userId]?.first?.close()
            databaseCache.remove(userId)
        }
        currentUserId = null
    }

    override suspend fun deleteUserData(userId: Int) {
        databaseCache[userId]?.first?.close()
        databaseCache.remove(userId)

        // Database dosyasını sil
        driverFactory.deleteDatabase(userId)
    }

    override suspend fun getSqlDriver(): SqlDriver {
        val userId = userSession.getActiveSession()?.userId
            ?: error("No user logged in")

        return databaseCache.getOrPut(userId) {
            createDatabaseForUser(userId)
        }.first // Pair'in ikinci elemanı AppDatabase
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun createDatabaseForUser(userId: Int): Pair<SqlDriver, AppDatabase> {
        val driver = driverFactory.createDriver(userId)
        val database = AppDatabase(driver)
        return driver to database
    }
    //endregion
}

class DatabaseManagerPreview(private val database: AppDatabase, private val sqlDriver: SqlDriver): IDatabaseManager{
    override suspend fun getDatabase(): AppDatabase {
        return database
    }

    override suspend fun switchUser(userId: Int) {

    }

    override suspend fun logout() {

    }

    override suspend fun deleteUserData(userId: Int) {

    }

    override suspend fun getSqlDriver(): SqlDriver {
        return sqlDriver
    }

}