package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class MobileLastDocumentModel(
    val docId: Int? = null,
    val docNumber: String?,
    val docDate: Instant?
)
