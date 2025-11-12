package com.repzone.domain.events.base.events

import com.repzone.core.util.extensions.now
import com.repzone.domain.pipline.model.Screen

sealed class ScreenEvents: DomainEvent {

    data class ScreenRequired(
        val ruleId: String,
        val screen: Screen,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : ScreenEvents()

    data class ScreenCompleted(
        val ruleId: String,
        val screenId: String,
        val screenData: Map<String, Any>,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : ScreenEvents()

    data class ScreenCancelled(
        val ruleId: String,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : ScreenEvents()
}