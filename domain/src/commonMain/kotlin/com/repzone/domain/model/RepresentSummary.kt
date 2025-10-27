package com.repzone.domain.model

data class RepresentSummary(
    val visitTotal : Int,
    val visitDoneTotal: Int,
    val orderCount: Int,
    val orderValue: Double,
    val formCount: Int,
    val activeAppoinmentDayCount: Int
)