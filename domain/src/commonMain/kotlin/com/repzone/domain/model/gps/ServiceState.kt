package com.repzone.domain.model.gps

import com.repzone.domain.common.DomainException

/**
 * Service State i burada tutulacak
 */
sealed class ServiceState {
    object Idle : ServiceState()
    object Starting : ServiceState()
    object Running : ServiceState()
    object Stopping : ServiceState()
    object Paused : ServiceState()
    data class Error(val domainException: DomainException) : ServiceState()

    fun isActive(): Boolean = this is Running || this is Starting
}