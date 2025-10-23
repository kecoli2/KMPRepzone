package com.repzone.domain.repository

import com.repzone.domain.util.models.SprintInformation

interface IRouteAppointmentRepository {
    fun getActiveSprintInformation(): SprintInformation?
}