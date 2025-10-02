package com.repzone.data.util

interface Mapper<Db, Domain> {
    fun toDomain(from: Db): Domain
    fun fromDomain(domain: Domain): Db
}

fun <Db, Domain> Mapper<Db, Domain>.toDomainList(list: List<Db>) =
    list.map(::toDomain)

fun <Db, Domain> Mapper<Db, Domain>.toDomainNullable(from: Db?) =
    from?.let(::toDomain)


interface MapperDto<Db, Domain, Dto> {
    fun toDomain(from: Db): Domain
    fun fromDomain(domain: Domain): Db
    fun fromDto(dto: Dto): Db
}