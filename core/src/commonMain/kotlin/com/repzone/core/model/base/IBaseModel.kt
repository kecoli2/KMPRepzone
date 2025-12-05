package com.repzone.core.model.base

import com.repzone.core.enums.StateType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface IBaseModel {
    val id: Int
    val state: StateType
    val modificationDateUtc: Instant?
    val recordDateUtc: Instant?
    fun getUpdateTime(): Instant?
}