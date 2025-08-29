package com.repzone.domain.repository

interface ReadOnlyRepository<ID, DOMAIN> {
    suspend fun getById(id: ID): DOMAIN?
    suspend fun getAll(): List<DOMAIN>
}

interface CrudRepository<ID, DOMAIN> : ReadOnlyRepository<ID, DOMAIN> {
    suspend fun upsert(entity: DOMAIN)
    suspend fun deleteById(id: ID)
}