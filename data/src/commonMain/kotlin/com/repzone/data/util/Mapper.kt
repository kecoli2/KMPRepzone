package com.repzone.data.util

interface Mapper<Db, Domain> {
    fun toDomain(from: Db): Domain
    fun fromDomain(domain: Domain): Db
}
interface MapperDto<Db, Domain, Dto> {
    fun toDomain(from: Db): Domain
    fun fromDomain(domain: Domain): Db
    fun fromDto(dto: Dto): Db
}

// ==================== EXTENSIONS ====================

fun <Db, Domain> Mapper<Db, Domain>.toDomainList(list: List<Db>): List<Domain> =
    list.map(::toDomain)

fun <Db, Domain> Mapper<Db, Domain>.toDomainNullable(from: Db?): Domain? =
    from?.let(::toDomain)

fun <Db, Domain> Mapper<Db, Domain>.fromDomainList(list: List<Domain>): List<Db> =
    list.map(::fromDomain)

fun <Db, Domain> Mapper<Db, Domain>.fromDomainNullable(domain: Domain?): Db? =
    domain?.let(::fromDomain)

fun <Db, Domain> Mapper<Db, Domain>.toDomainSet(set: Set<Db>): Set<Domain> =
    set.map(::toDomain).toSet()

fun <Db, Domain> Mapper<Db, Domain>.fromDomainSet(set: Set<Domain>): Set<Db> =
    set.map(::fromDomain).toSet()

fun <Db, Domain> Mapper<Db, Domain>.toDomainFirstOrNull(list: List<Db>): Domain? =
    list.firstOrNull()?.let(::toDomain)

fun <Db, Domain> Mapper<Db, Domain>.toDomainLastOrNull(list: List<Db>): Domain? =
    list.lastOrNull()?.let(::toDomain)

fun <Db, Domain> Mapper<Db, Domain>.toDomainDistinct(list: List<Db>): List<Domain> =
    list.map(::toDomain).distinct()

inline fun <Db, Domain> Mapper<Db, Domain>.toDomainListFiltered(list: List<Db>, predicate: (Domain) -> Boolean): List<Domain> = list.map(::toDomain).filter(predicate)

inline fun <Db, Domain, K> Mapper<Db, Domain>.toDomainListGrouped(list: List<Db>, keySelector: (Domain) -> K): Map<K, List<Domain>> = list.map(::toDomain).groupBy(keySelector)

inline fun <Db, Domain, R : Comparable<R>> Mapper<Db, Domain>.toDomainListSortedBy(list: List<Db>, crossinline selector: (Domain) -> R?): List<Domain> = list.map(::toDomain).sortedBy(selector)

inline fun <Db, Domain, R : Comparable<R>> Mapper<Db, Domain>.toDomainListSortedByDescending(list: List<Db>, crossinline selector: (Domain) -> R?): List<Domain> = list.map(::toDomain).sortedByDescending(selector)

fun <Db, Domain> Mapper<Db, Domain>.toDomainListTake(list: List<Db>, n: Int): List<Domain> = list.take(n).map(::toDomain)

inline fun <Db, Domain, R> Mapper<Db, Domain>.toDomainAndMap(from: Db, transform: (Domain) -> R): R = transform(toDomain(from))

inline fun <Db, Domain, R> Mapper<Db, Domain>.fromDomainAndMap(domain: Domain, transform: (Db) -> R): R = transform(fromDomain(domain))

inline fun <Db, Domain> Mapper<Db, Domain>.anyDomain(list: List<Db>, predicate: (Domain) -> Boolean): Boolean = list.map(::toDomain).any(predicate)

inline fun <Db, Domain> Mapper<Db, Domain>.countDomain(list: List<Db>, predicate: (Domain) -> Boolean): Int = list.map(::toDomain).count(predicate)

// ==================== DTO MAPPER EXTENSIONS ====================
fun <Db, Domain, Dto> MapperDto<Db, Domain, Dto>.fromDtoList(list: List<Dto>): List<Db> = list.map(::fromDto)
fun <Db, Domain, Dto> MapperDto<Db, Domain, Dto>.dtoToDomain(dto: Dto): Domain = toDomain(fromDto(dto))

fun <Db, Domain, Dto> MapperDto<Db, Domain, Dto>.dtoToDomainList(list: List<Dto>): List<Domain> = list.map { dtoToDomain(it) }

fun <Db, Domain, Dto> MapperDto<Db, Domain, Dto>.fromDtoNullable(dto: Dto?): Db? = dto?.let(::fromDto)