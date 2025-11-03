package com.repzone.domain.repository

import com.repzone.domain.model.CustomerByParrentModel
import com.repzone.domain.model.CustomerItemModel
import com.repzone.domain.util.models.SprintInformation
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface ICustomerListRepository {
    suspend fun getCustomerList(utcDate: Instant?): List<CustomerItemModel>

    suspend fun getAllByParrent(selectedCustomer: CustomerItemModel): CustomerByParrentModel
}