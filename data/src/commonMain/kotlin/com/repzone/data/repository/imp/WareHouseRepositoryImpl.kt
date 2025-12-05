package com.repzone.data.repository.imp

import com.repzone.core.enums.SalesOperationType
import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.toLong
import com.repzone.data.mapper.SyncWarehouseEntityDbMapper
import com.repzone.database.SyncWarehouseEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.SyncWarehouseModel
import com.repzone.domain.repository.IWareHouseRepository

class WareHouseRepositoryImpl(private val iDatabaseManager: IDatabaseManager, private val mapper: SyncWarehouseEntityDbMapper): IWareHouseRepository {
    //region Public Method
    override suspend fun getWarehouseList(orgId: Int, salesOperationType: SalesOperationType): List<SyncWarehouseModel> {

        val list =  when(salesOperationType){
            SalesOperationType.SALESRETURN -> {
                iDatabaseManager.getSqlDriver().select<SyncWarehouseEntity> {
                    where {
                        criteria("OrganizationId", orgId)
                        criteria("State", notEqual = StateType.DELETED.ordinal)
                        criteria("MobileCloseToReturns", equal = false.toLong())
                    }
                }.toList().map {
                    mapper.toDomain(it)
                }
            }
            SalesOperationType.SALESDAMAGEDRETURN8 -> {
                iDatabaseManager.getSqlDriver().select<SyncWarehouseEntity> {
                    where {
                        criteria("OrganizationId", orgId)
                        criteria("State", notEqual = StateType.DELETED.ordinal)
                        criteria("MobileCloseToDamagedReturns", equal = false.toLong())
                    }
                }.toList().map {
                    mapper.toDomain(it)
                }
            }
            else -> {
                iDatabaseManager.getSqlDriver().select<SyncWarehouseEntity> {
                    where {
                        criteria("OrganizationId", orgId)
                        criteria("State", notEqual = StateType.DELETED.ordinal)
                        criteria("MobileCloseToSales", equal = false.toLong())
                    }
                }.toList().map {
                    mapper.toDomain(it)
                }
            }
        }
        return list
    }


    override suspend fun getWarehouse(warehouseId: Int): SyncWarehouseModel? {
        return iDatabaseManager.getSqlDriver().select<SyncWarehouseEntity> {
            where {
                criteria("Id", warehouseId)
            }
        }.firstOrNull()?.let {
            mapper.toDomain(it)
        }

    }
    //endregion Public Method
}