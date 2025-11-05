package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class LastDocumentModel(
    val docId: Int,
    val docNumber: String? = null,
    val docDate: Instant
)
