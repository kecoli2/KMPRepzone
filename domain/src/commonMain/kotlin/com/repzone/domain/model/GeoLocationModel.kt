package com.repzone.domain.model

data class GeoLocationModel(
  val id: Long,
  val accuracy: Double?,
  val altitude: Double?,
  val altitudeAccuracy: Double?,
  val batteryLevel: Long?,
  val dailyOperationType: Long?,
  val description: String?,
  val heading: Double?,
  val latitude: Double?,
  val longitude: Double?,
  val representativeId: Long?,
  val reverseGeocoded: String?,
  val speed: Double?,
  val time: Long?,
)
