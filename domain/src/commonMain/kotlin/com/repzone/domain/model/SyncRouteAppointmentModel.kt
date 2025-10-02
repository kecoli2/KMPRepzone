package com.repzone.domain.model

data class SyncRouteAppointmentModel(
    val id: Long,
    val customerId: Long?,
    val description: String?,
    val endDate: Long?,
    val sprintId: Long?,
    val startDate: Long?,
    val state: Long?,
)
