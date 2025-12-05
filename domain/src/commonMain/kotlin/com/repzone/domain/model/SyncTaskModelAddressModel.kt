package com.repzone.domain.model

import com.repzone.core.enums.StateType

data class SyncTaskModelAddressModel(
  val id: Long,
  val city: String?,
  val country: String?,
  val district: String?,
  val latitude: Double?,
  val longitude: Double?,
  val phoneNumber: String?,
  val postalCode: String?,
  val relatedPerson: String?,
  val state: StateType,
  val street1: String?,
  val street2: String?,
  val taskStepId: Long?,
)
