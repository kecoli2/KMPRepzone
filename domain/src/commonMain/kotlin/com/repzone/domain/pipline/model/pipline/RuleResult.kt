package com.repzone.domain.pipline.model.pipline

import com.repzone.domain.common.DomainException
import com.repzone.domain.pipline.model.Screen

sealed class RuleResult {
    abstract val rule: Rule

    data class Success(
        override val rule: Rule,
        val data: Map<String, Any>? = null
    ) : RuleResult()

    data class Failed(
        override val rule: Rule,
        val message: String? = null,
        val domainException: DomainException? = null
    ) : RuleResult()

    data class AwaitingScreen(
        override val rule: Rule,
        val screen: Screen
    ) : RuleResult()

    data class AwaitingDecision(
        override val rule: Rule,
        val options: List<DecisionOption>
    ) : RuleResult()
}

data class DecisionOption(
    val id: String,
    val label: String
)