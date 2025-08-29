package com.repzone.data.repository.base

import com.repzone.data.util.IDbCrudOps
import com.repzone.data.util.Mapper
import com.repzone.domain.repository.CrudRepository

/**
 * DB <-> DOMAIN dönüşümü yapan generic CRUD implementasyonu.
 * ID: kimlik tipi, DB: SQLDelight row, DOMAIN: domain entity
 */
open class BaseCrudRepository<ID, DB, DOMAIN>(
    private val ops: IDbCrudOps<ID, DB>,
    private val mapper: Mapper<DB, DOMAIN>) : CrudRepository<ID, DOMAIN> {

    override suspend fun upsert(entity: DOMAIN) {
        ops.insertOrUpdate(mapper.fromDomain(entity))
    }

    override suspend fun getById(id: ID): DOMAIN? =
        ops.findById(id)?.let(mapper::toDomain)

    override suspend fun deleteById(id: ID) {
        ops.deleteById(id)
    }

    override suspend fun getAll(): List<DOMAIN> =
        ops.listAll().map(mapper::toDomain)
}