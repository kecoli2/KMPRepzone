package com.repzone.domain.pipline.model.pipline

import com.repzone.core.enums.DocumentActionType

data class Pipeline(
    val id: String,
    val actionType: DocumentActionType,
    val stages: List<Stage>
)