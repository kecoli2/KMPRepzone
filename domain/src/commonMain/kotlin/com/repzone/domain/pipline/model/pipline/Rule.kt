package com.repzone.domain.pipline.model.pipline

interface Rule {
    val id: String
    val title: String
    val type: RuleType

    suspend fun execute(context: PipelineContext): RuleResult
}

enum class RuleType {
    CHECK,
    ACTION,
    DECISION,
    SCREEN
}