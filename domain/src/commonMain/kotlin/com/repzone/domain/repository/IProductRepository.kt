package com.repzone.domain.repository

import com.repzone.domain.model.SyncProductModel
import com.repzone.domain.repository.base.CrudRepository

interface IProductRepository: CrudRepository<Long, SyncProductModel> {

}