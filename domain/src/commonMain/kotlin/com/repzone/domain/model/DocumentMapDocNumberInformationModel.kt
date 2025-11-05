package com.repzone.domain.model

import com.repzone.core.enums.DocumentTypeGroup

data class DocumentMapDocNumberInformationModel(
  val id: Long,
  val documentMapId: Long?,
  val documentNumberBody: Long?,
  val documentNumberPostfix: String?,
  val documentNumberPrefix: String?,
  val documentType: DocumentTypeGroup,
  val state: Long?,
)
