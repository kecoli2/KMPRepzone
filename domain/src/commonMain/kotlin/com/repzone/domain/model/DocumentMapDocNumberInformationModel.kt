package com.repzone.domain.model

import com.repzone.core.enums.DocumentActionType

data class DocumentMapDocNumberInformationModel(
    val id: Long = 0,
    val documentMapId: Long?,
    val documentNumberBody: Long?,
    val documentNumberPostfix: String?,
    val documentNumberPrefix: String?,
    val documentType: DocumentActionType,
    val state: Long? = 0L,
)
