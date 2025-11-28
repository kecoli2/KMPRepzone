package com.repzone.domain.repository

interface IPriceRepository {
    suspend fun checkPriceListOverRules(priceListId: Long, customerId: Long) : Boolean
}