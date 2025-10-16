package com.repzone.database.factory

import app.cash.sqldelight.db.SqlDriver
import com.repzone.database.AppDatabase
import com.repzone.database.driver.DatabaseDriverFactory

class DatabaseFactory(private val driverFactory: DatabaseDriverFactory) {
    //region Field
    private val databaseCache = mutableMapOf<Int, Pair<SqlDriver, AppDatabase>>()
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    fun getDatabase(userId: Int): AppDatabase {
        return databaseCache.getOrPut(userId) {
            val driver = driverFactory.createDriver(userId)
            val database = AppDatabase(driver)
            driver to database
        }.second
    }

    fun closeDatabase(userId: Int) {
        databaseCache.remove(userId)?.first?.close()
    }

    fun closeAllDatabases() {
        databaseCache.values.forEach { (driver, _) ->
            driver.close()
        }
        databaseCache.clear()
    }
    //endregion

}