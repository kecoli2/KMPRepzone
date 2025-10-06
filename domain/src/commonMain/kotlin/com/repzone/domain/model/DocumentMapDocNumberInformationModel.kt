package com.repzone.domain.model

data class DocumentMapDocNumberInformationModel(
  val id: Long,
  val documentMapId: Long?,
  val documentNumberBody: Long?,
  val documentNumberPostfix: String?,
  val documentNumberPrefix: String?,
  val documentType: Long?,
  val state: Long?,
)
