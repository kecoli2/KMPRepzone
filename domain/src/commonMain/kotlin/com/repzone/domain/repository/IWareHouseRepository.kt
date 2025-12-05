package com.repzone.domain.repository

import com.repzone.core.enums.SalesOperationType
import com.repzone.domain.model.SyncWarehouseModel

interface IWareHouseRepository {
    suspend fun getWarehouseList(orgId: Int, salesOperationType: SalesOperationType): List<SyncWarehouseModel>
    suspend fun getWarehouse(warehouseId: Int): SyncWarehouseModel?
}