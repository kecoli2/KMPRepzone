package com.repzone.domain.model.gps

import com.repzone.core.model.UiText

/**
 * Service State i burada tutulacak
 */
sealed class ServiceState {
    object Idle : ServiceState()
    object Starting : ServiceState()
    object Running : ServiceState()
    object Stopping : ServiceState()
    data class Error(val message: UiText, val throwable: Throwable? = null) : ServiceState()
}