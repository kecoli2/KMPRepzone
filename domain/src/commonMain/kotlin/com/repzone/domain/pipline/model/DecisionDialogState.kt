package com.repzone.domain.pipline.model

import com.repzone.domain.pipline.model.pipline.DecisionOption

data class DecisionDialogState(
    val ruleId: String,
    val title: String,
    val message: String,
    val options: List<DecisionOption>,
    val sessionId: String
)
