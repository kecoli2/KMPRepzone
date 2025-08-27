package com.repzone.data.util

/**
 * DB operasyonları için strateji.
 * ID = kimlik tipi (String/Long vs.), DB = SQLDelight row tipi (Orders gibi)
 */
interface IDbCrudOps<ID, DB> {
    suspend fun insertOrUpdate(row: DB)
    suspend fun findById(id: ID): DB?
    suspend fun deleteById(id: ID)
    suspend fun listAll(): List<DB>
}