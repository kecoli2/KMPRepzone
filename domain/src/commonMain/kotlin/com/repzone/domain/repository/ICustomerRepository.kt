package com.repzone.domain.repository

import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.base.CrudRepository

interface ICustomerRepository  {
    suspend fun getById(id: Long): SyncCustomerModel?

}