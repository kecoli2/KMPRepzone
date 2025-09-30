/*
package com.repzone.database.dao

import com.repzone.database.AppDatabase
import com.repzone.database.Orders

class OrderDao(private val db: AppDatabase) {
    private val queries get() = db.orderQueries

    fun upsert(order: Orders) {
        queries.insertOrReplace(
            order
        )
    }

    fun getById(id: String): Orders? =
        queries.selectById(id).executeAsOneOrNull()

    fun pending(): List<Orders> =
        queries.selectPending().executeAsList()
}*/
