package com.repzone.data.util

interface Mapper<Db, Domain> {
    fun toDomain(from: Db): Domain
    fun fromDomain(domain: Domain): Db
}

inline fun <Db, Domain> Mapper<Db, Domain>.toDomainList(list: List<Db>) =
    list.map(::toDomain)

inline fun <Db, Domain> Mapper<Db, Domain>.toDomainNullable(from: Db?) =
    from?.let(::toDomain)