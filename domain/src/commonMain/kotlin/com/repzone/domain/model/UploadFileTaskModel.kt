package com.repzone.domain.model

data class UploadFileTaskModel(
  val id: Long,
  val azurePath: String?,
  val filePath: String?,
  val status: Long?,
  val storeFileName: String?,
)
