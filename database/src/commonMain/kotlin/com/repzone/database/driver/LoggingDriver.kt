package com.repzone.database.driver

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import com.repzone.core.platform.Logger

class LoggingDriver(
    private val driver: SqlDriver,
    private val tag: String = "SqlDelight",
    private val enabled: Boolean = true
) : SqlDriver by driver {

    override fun execute(identifier: Int?, sql: String, parameters: Int, binders: (SqlPreparedStatement.() -> Unit)?): QueryResult<Long> {
        if (enabled) {
            Logger.d(tag, "EXECUTE: $sql")
        }
        return driver.execute(identifier, sql, parameters, binders)
    }
    override fun <R> executeQuery(identifier: Int?, sql: String, mapper: (SqlCursor) -> QueryResult<R>, parameters: Int, binders: (SqlPreparedStatement.() -> Unit)?): QueryResult<R> {
        if (enabled) {
            Logger.d(tag, "QUERY: $sql")
        }
        return driver.executeQuery(identifier, sql, mapper, parameters, binders)
    }
}