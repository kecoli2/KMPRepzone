package com.repzone.database.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.repzone.database.AppDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(userId: Int): SqlDriver {
        return AndroidSqliteDriver(schema = AppDatabase.Schema, context = context, name = "repzone_$userId.db")
    }

    actual fun deleteDatabase(userId: Int) {
        context.deleteDatabase("repzone_$userId.db")
    }
}