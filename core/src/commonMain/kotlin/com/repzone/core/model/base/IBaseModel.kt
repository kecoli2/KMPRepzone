package com.repzone.core.model.base

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface IBaseModel {
    val id: Int
    val state: Int
    val modificationDateUtc: Instant?
    val recordDateUtc: Instant?
    fun getUpdateTime(): Instant?
}