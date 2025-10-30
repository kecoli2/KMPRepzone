package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver
import com.repzone.core.util.extensions.now

// Transaction scope - içinde CRUD işlemleri yapılabilir
class TransactionScope(val driver: SqlDriver) {

    fun <T : Any> insert(entity: T): Long {
        return driver.insert(entity)
    }

    fun <T : Any> update(entity: T): Int {
        return driver.update(entity)
    }

    fun <T : Any> delete(entity: T): Int {
        return driver.delete(entity)
    }

    inline fun <reified T : Any> delete(
        noinline block: DeleteBuilder<T>.() -> Unit
    ): Int {
        return driver.delete(block)
    }

    inline fun <reified T : Any> select(
        noinline block: SelectBuilder<T>.() -> Unit = {}
    ): SelectBuilder<T> {
        return driver.select(block)
    }

    // Aggregate fonksiyonları
    inline fun <reified T : Any> count(
        noinline block: (SelectBuilder<T>.() -> Unit)? = null
    ): Long {
        return driver.count(block)
    }

    inline fun <reified T : Any> sum(
        field: String,
        noinline block: (SelectBuilder<T>.() -> Unit)? = null
    ): Double {
        return driver.sum(field, block)
    }
}

// Transaction - sonuç dönmez
fun SqlDriver.transaction(body: TransactionScope.() -> Unit) {
    // SQLDelight'ın transaction API'sini kullan
    execute(null, "BEGIN TRANSACTION", 0)

    try {
        val scope = TransactionScope(this)
        scope.body()
        execute(null, "COMMIT", 0)
    } catch (e: Exception) {
        execute(null, "ROLLBACK", 0)
        throw e
    }
}

// Transaction with result - sonuç döner
fun <R> SqlDriver.transactionWithResult(body: TransactionScope.() -> R): R {
    execute(null, "BEGIN TRANSACTION", 0)

    return try {
        val scope = TransactionScope(this)
        val result = scope.body()
        execute(null, "COMMIT", 0)
        result
    } catch (e: Exception) {
        execute(null, "ROLLBACK", 0)
        throw e
    }
}

// Nested transaction support
fun TransactionScope.transaction(body: TransactionScope.() -> Unit) {
    val savepointName = "sp_${now()}"
    driver.execute(null, "SAVEPOINT nested_${savepointName}", 0)

    try {
        this.body()
        driver.execute(null, "RELEASE SAVEPOINT $savepointName", 0)
    } catch (e: Exception) {
        driver.execute(null, "ROLLBACK TO SAVEPOINT $savepointName", 0)
        throw e
    }
}