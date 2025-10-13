package com.repzone.database.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.repzone.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseDriverFactory {
    actual fun createDriver(userId: Int): SqlDriver{
        return NativeSqliteDriver(schema = AppDatabase.Schema, name = "repzone_$userId.db")
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun deleteDatabase(userId: Int) {
        val documentsDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbPath = documentsDirectory?.path + "/repzone_$userId.db"
        NSFileManager.defaultManager.removeItemAtPath(dbPath, error = null)
    }
}