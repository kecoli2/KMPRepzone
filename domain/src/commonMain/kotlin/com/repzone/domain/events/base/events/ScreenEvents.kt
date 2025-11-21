package com.repzone.domain.events.base.events

import com.repzone.core.util.extensions.now
import com.repzone.domain.pipline.model.Screen
import com.repzone.domain.pipline.rules.util.RuleId

sealed class ScreenEvents: DomainEvent {

    data class ScreenRequired(
        val ruleId: RuleId,
        val screen: Screen,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : ScreenEvents()

    data class ScreenCompleted(
        val ruleId: RuleId,
        val screenId: String,
        val screenData: Map<String, Any>,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : ScreenEvents()

    data class ScreenCancelled(
        val ruleId: RuleId,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : ScreenEvents()
}