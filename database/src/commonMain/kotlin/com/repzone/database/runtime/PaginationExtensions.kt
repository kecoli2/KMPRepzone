package com.repzone.database.runtime

import app.cash.sqldelight.db.SqlDriver

// Basit pagination result
data class Page<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val hasMore: Boolean
)

// Extension: page helper
fun <T : Any> SelectBuilder<T>.page(pageNumber: Int, pageSize: Int): SelectBuilder<T> {
    require(pageNumber >= 1) { "Page must be >= 1" }
    require(pageSize > 0) { "Page size must be > 0" }

    val offsetCount = (pageNumber - 1) * pageSize
    this.limit(pageSize + 1) // +1 to check if there's more
    this.offset(offsetCount)  // this. ekle

    return this
}

// Extension: get page result
fun <T : Any> SelectBuilder<T>.toPage(pageNumber: Int, pageSize: Int): Page<T> {
    this.page(pageNumber, pageSize)

    val items = this.toList()
    val hasMore = items.size > pageSize

    return Page(
        items = items.take(pageSize),
        page = pageNumber,
        pageSize = pageSize,
        hasMore = hasMore
    )
}