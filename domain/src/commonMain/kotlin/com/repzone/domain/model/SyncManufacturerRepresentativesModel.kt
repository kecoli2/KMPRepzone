package com.repzone.domain.model

data class SyncManufacturerRepresentativesModel(
  val id: Long,
  val fulfillment: Long?,
  val manufacturerId: Long?,
  val modificationDateUtc: Long?,
  val recordDateUtc: Long?,
  val representativeId: Long?,
  val salesNotAllowed: Long?,
  val state: Long?,
)
