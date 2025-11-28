package com.repzone.data.repository.imp

import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.delete
import com.repzone.database.runtime.insertOrReplace
import com.repzone.database.runtime.select
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ICustomerRepository

class CustomerRepositoryImpl(private val mapper: CustomerEntityDbMapper,
                             private val iDatabaseManager: IDatabaseManager): ICustomerRepository {
    //region Public Method

    override suspend fun getById(id: Long): SyncCustomerModel {
        return iDatabaseManager.getSqlDriver().select<SyncCustomerEntity>{where {
            criteria("Id", id)
        }}.first().let {  mapper.toDomain(it)}
    }
    //endregion

}