package com.repzone.domain.model

import com.repzone.core.enums.DocumentActionType
import com.repzone.core.enums.StateType

data class DocumentMapDocNumberInformationModel(
    val id: Long = 0,
    val documentMapId: Long?,
    val documentNumberBody: Long?,
    val documentNumberPostfix: String?,
    val documentNumberPrefix: String?,
    val documentType: DocumentActionType,
    val state: StateType = StateType.ACTIVE,
)
