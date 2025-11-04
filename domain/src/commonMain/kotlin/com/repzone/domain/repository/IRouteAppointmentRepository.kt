package com.repzone.domain.repository

import com.repzone.domain.util.models.SprintInformation

interface IRouteAppointmentRepository {
   suspend fun getActiveSprintInformation(): SprintInformation?
   suspend fun getRouteInformation(appointmentId: Long)
}