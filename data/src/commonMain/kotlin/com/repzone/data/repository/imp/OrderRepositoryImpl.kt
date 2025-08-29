package com.repzone.data.repository.imp

import com.repzone.data.repository.base.BaseCrudRepository
import com.repzone.data.util.IDbCrudOps
import com.repzone.data.util.Mapper
import com.repzone.database.Orders
import com.repzone.domain.model.OrderEntity
import com.repzone.domain.repository.IOrderRepository

class OrderRepositoryImpl(
    ops: IDbCrudOps<String, Orders>,
    mapper: Mapper<Orders, OrderEntity>
) : BaseCrudRepository<String, Orders, OrderEntity>(ops, mapper),
    IOrderRepository {

    override suspend fun pending(): List<OrderEntity> {
        throw NotImplementedError("pending() için OrderDbOps'a özel bir ops fonksiyon eklenebilir")
    }
}