package com.repzone.domain.model

data class SyncGameModel(
  val id: Long,
  val description: String?,
  val icon: String?,
  val modificationDateUtc: Long?,
  val name: String?,
  val point: Long?,
  val priority: Long?,
  val rankDescription: String?,
  val rankId: Long?,
  val rankName: String?,
  val recordDateUtc: Long?,
  val stageIcon: String?,
  val stageId: Long?,
  val stageName: String?,
  val state: Long?,
  val unSuccessIcon: String?,
)
