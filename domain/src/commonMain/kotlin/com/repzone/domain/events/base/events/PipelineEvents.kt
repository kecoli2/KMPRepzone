package com.repzone.domain.events.base.events

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.util.extensions.now
import com.repzone.domain.pipline.model.pipline.PipelineContext

sealed class PipelineEvents: DomainEvent {

    data class PipelineCompleted(
        val actionType: DocumentActionType,
        val context: PipelineContext,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : PipelineEvents()

    data class PipelineFailed(
        val actionType: DocumentActionType,
        val reason: String,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : PipelineEvents()
}