package com.repzone.domain.pipline.model

import com.repzone.core.model.UiText
import com.repzone.domain.pipline.model.pipline.DecisionOption
import com.repzone.domain.pipline.rules.util.RuleId

data class DecisionDialogState(
    val ruleId: RuleId,
    val title: UiText,
    val message: UiText,
    val options: List<DecisionOption>,
    val sessionId: String
)
