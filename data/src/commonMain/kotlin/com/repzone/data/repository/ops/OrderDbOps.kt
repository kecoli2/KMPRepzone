package com.repzone.data.repository.ops

import com.repzone.data.util.IDbCrudOps

class OrderDbOps(
    private val dao: com.repzone.database.dao.OrderDao
) : IDbCrudOps<String, com.repzone.database.Orders> {

    override suspend fun insertOrUpdate(row: com.repzone.database.Orders) {
        dao.upsert(row) // order.sq’de insertOrReplace tanımlı olmalı
    }

    override suspend fun findById(id: String): com.repzone.database.Orders? =
        dao.getById(id)

    override suspend fun deleteById(id: String) {
        //dao.deleteById(id)  // order.sq’ye deleteById eklenebili
        // veya hard delete yok ise durumu update edilir ve upsert e yollanır
    }

    override suspend fun listAll(): List<com.repzone.database.Orders> =
        dao.pending() // order.sq’ye getAll ekle (SELECT * FROM orders)
}