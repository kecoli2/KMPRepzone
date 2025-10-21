package com.repzone.domain.model

data class MobileParameterModel(
    var order: Boolean = false,
    var invoice: Boolean = false,
    var qlikReport: Boolean = false,
    var drive: Boolean = false,
    var task: Boolean = false,
    var eagleEye: Boolean = false,
    var chat: Boolean = false,
    var geoFence: Boolean = false,
    var form: Boolean = false,
    var timeTracker: Boolean = false,
    var report: Boolean = false,
    var dispatch: Boolean = false,
    var payment: Boolean = false,
    var gamification: Boolean = false,
    var crmOperations: Boolean = false
)