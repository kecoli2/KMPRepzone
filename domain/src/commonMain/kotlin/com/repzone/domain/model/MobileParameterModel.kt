package com.repzone.domain.model

data class MobileParameterModel(
    val order: Boolean = false,
    val invoice: Boolean = false,
    val qlikReport: Boolean = false,
    val drive: Boolean = false,
    val task: Boolean = false,
    val eagleEye: Boolean = false,
    val chat: Boolean = false,
    val geoFence: Boolean = false,
    val form: Boolean = false,
    val timeTracker: Boolean = false,
    val report: Boolean = false,
    val dispatch: Boolean = false,
    val payment: Boolean = false,
    val gamification: Boolean = false,
    val crmOperations: Boolean = false
)