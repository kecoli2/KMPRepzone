package com.repzone.data.repository.imp

import com.repzone.data.repository.base.BaseCrudRepository
import com.repzone.data.util.IDbCrudOps
import com.repzone.data.util.Mapper
import com.repzone.database.AppDatabase
import com.repzone.database.SyncCustomerEntity
import com.repzone.database.SyncCustomerEntityQueries
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.ICustomerRepository

class CustomerRepositoryImpl(ops: IDbCrudOps<Long, SyncCustomerEntity>, mapper: Mapper<SyncCustomerEntity, SyncCustomerModel>, private val queries: SyncCustomerEntityQueries):BaseCrudRepository<Long, SyncCustomerEntity, SyncCustomerModel>(ops, mapper),
    ICustomerRepository {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override suspend fun pending(): List<SyncCustomerModel> {
        TODO("Not yet implemented")
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}