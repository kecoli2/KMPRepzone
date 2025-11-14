package com.repzone.domain.pipline.model.pipline

import com.repzone.core.model.UiText

interface Rule {
    val id: String
    val title: UiText
    val type: RuleType

    suspend fun execute(context: PipelineContext): RuleResult
    suspend fun canExecute(context: PipelineContext): Boolean = true
}

enum class RuleType {
    CHECK,
    ACTION,
    DECISION,
    SCREEN
}