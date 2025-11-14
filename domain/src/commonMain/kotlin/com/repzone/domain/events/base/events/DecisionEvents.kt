package com.repzone.domain.events.base.events

import com.repzone.core.model.UiText
import com.repzone.core.util.extensions.now
import com.repzone.domain.pipline.model.pipline.DecisionOption

sealed class DecisionEvents : DomainEvent {

    data class DecisionRequired(
        val ruleId: String,
        val title: UiText,
        val message: UiText,
        val options: List<DecisionOption>,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : DecisionEvents()

    data class DecisionMade(
        val ruleId: String,
        val selectedOption: DecisionOption,
        val sessionId: String,
        override val timestamp: Long = now()
    ) : DecisionEvents()
}