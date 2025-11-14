package com.repzone.domain.pipline.model

import com.repzone.core.model.UiText
import com.repzone.domain.pipline.model.pipline.DecisionOption

data class DecisionDialogState(
    val ruleId: String,
    val title: UiText,
    val message: UiText,
    val options: List<DecisionOption>,
    val sessionId: String
)
