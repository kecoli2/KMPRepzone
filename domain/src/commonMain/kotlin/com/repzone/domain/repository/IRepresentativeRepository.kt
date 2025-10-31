package com.repzone.domain.repository

import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.model.RepresentSummary
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface IRepresentativeRepository {
    suspend fun getSummary(date: Instant?, routes: List<CustomerItemModel>): RepresentSummary
}