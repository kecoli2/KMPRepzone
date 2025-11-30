package com.repzone.domain.model

data class PaymentPlanModel(
    val id: Int,
    val name: String,
    val code: String,
    val isDefault : Boolean = false
)
