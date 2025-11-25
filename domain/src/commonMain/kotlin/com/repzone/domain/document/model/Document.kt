package com.repzone.domain.document.model

import com.repzone.domain.document.base.IDocumentLine
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


/**
 * Belge
 */
@OptIn(ExperimentalTime::class)
data class Document(
    val id: String,
    val type: DocumentType,
    val number: String? = null,
    val customerId: String? = null,
    val lines: List<IDocumentLine>,
    val createdAt: Instant,
    val updatedAt: Instant
)
