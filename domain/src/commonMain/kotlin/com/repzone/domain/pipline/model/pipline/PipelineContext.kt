package com.repzone.domain.pipline.model.pipline

import com.repzone.core.enums.DocumentActionType

data class PipelineContext(
    val actionType: DocumentActionType,
    val sessionId: String,
    private val data: MutableMap<String, Any> = mutableMapOf()
) {
    fun <T> getData(key: String): T? = data[key] as? T

    fun putData(key: String, value: Any) {
        data[key] = value
    }
}