package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class LastDocumentModel(
    val docId: Int = 0,
    val docNumber: String = "",
    val docDate: Instant? = null
)
