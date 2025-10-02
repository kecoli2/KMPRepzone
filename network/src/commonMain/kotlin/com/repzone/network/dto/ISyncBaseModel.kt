package com.repzone.network.dto

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
sealed interface ISyncBaseModel{
    val id: Int
    val state: Int
    val modificationDateUtc: Instant?
    val recordDateUtc: Instant?
}

