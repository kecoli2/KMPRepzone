package com.repzone.domain.pipline.model.pipline

data class Stage(
    val id: String,
    val name: String,
    val rules: List<Rule>,
    val isConditional: Boolean = false,
    val condition: ((PipelineContext) -> Boolean)? = null
) {
    fun shouldExecute(context: PipelineContext): Boolean {
        if (!isConditional) return true
        return condition?.invoke(context) ?: true
    }
}