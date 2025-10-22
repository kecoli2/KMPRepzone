package com.repzone.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class CustomerItemModel(
    val customerId: Long,
    val visitId: Long?,
    val iconIndex: Long?,
    val finishDate: Instant?,
    val appointmentId: Long,
    val date: Instant?,
    val tagRaw: List<String?>? = emptyList(),
    val name: String?,
    val customerCode: String?,
    val customerGroupName: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val addressType: Long?,
    val imageUri: String?,
    val parentCustomerId: Long?,
    val endDate: Instant?,
    val customerBlocked: Long?,
    val sprintId: Long?,
){
    
}