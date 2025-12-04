package com.repzone.data.repository.imp

import com.repzone.core.enums.CrmParameterEntityType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.toEnum
import com.repzone.database.SyncCrmPriceListParameterEntity
import com.repzone.database.SyncCustomerGroupProductDistributionEntity
import com.repzone.database.SyncCustomerProductDistributionEntity
import com.repzone.database.SyncProductDistributionEntity
import com.repzone.database.SyncProductPricesEntity
import com.repzone.database.SyncRepresentativeProductDistributionEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.DistributionControllerModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.IDistributionRepository

class DistributionRepositoryImpl(private val iDatabaseManager: IDatabaseManager,
                                 private val iUserSession: IUserSession
): IDistributionRepository {
    //region Field
    //endregion

    //region Public Method
    override suspend fun getActiveDistributionListId(customer: SyncCustomerModel?, paymentPlanId: Int): DistributionControllerModel? {
        val ret = DistributionControllerModel()
        //return ret
        iUserSession.getActiveSession()?.identity?.let {
            val repId = it.representativeId
            val repGroupId = it.groupId
            val repList = iDatabaseManager.getSqlDriver().select<SyncRepresentativeProductDistributionEntity> {
                where {
                    criteria("Id", equal = repId)
                    criteria("GroupId", equal = repGroupId)
                    criteria("State", equal = 1)
                }
            }.firstOrNull()
            repList?.let { it ->
                if ((it.SalesDistributionListId ?: 0) > 0) {
                    val productList =
                        iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                            where {
                                criteria("Id", equal = it.SalesDistributionListId)
                                criteria("State", equal = 1)
                            }
                        }.any()
                    if (productList)
                        ret.representativeDistributionListId = it.SalesDistributionListId?.toInt() ?: -1
                } else if ((it.GroupSalesDistributionListId ?: 0) > 0) {
                    val productList =
                        iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                            where {
                                criteria("Id", equal = it.GroupSalesDistributionListId)
                                criteria("State", equal = 1)
                            }
                        }.any()

                    if (productList)
                        ret.representativeDistributionListId =
                            it.GroupSalesDistributionListId?.toInt() ?: -1

                } else if ((it.SalesReturnDistributionListId ?: 0) > 0) {
                    val productList =
                        iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                            where {
                                criteria("Id", equal = it.SalesReturnDistributionListId)
                                criteria("State", equal = 1)
                            }
                        }.any()

                    if (productList)
                        ret.representativeReturnDistributionListId =
                            it.SalesReturnDistributionListId?.toInt() ?: -1
                }
            }
            customer?.let { customer ->
                val custGroupList =
                    iDatabaseManager.getSqlDriver().select<SyncCustomerGroupProductDistributionEntity> {
                        where {
                            criteria("Id", equal = customer.groupId)
                            criteria("OrganizationId", equal = iUserSession.decideWhichOrgIdToBeUsed(customer.organizationId?.toInt() ?: 0))
                            criteria("State", equal = 1)
                        }
                    }.firstOrNull()

                custGroupList?.let { it ->
                    if((it.ProductSalesDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                                where {
                                    criteria("Id", equal = it.ProductSalesDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerDistributionListId = custGroupList.ProductSalesDistributionListId?.toInt() ?: -1
                    }

                    if((it.ProductSalesReturnDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                                where {
                                    criteria("Id", equal = it.ProductSalesReturnDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerReturnDistributionListId = custGroupList.ProductSalesReturnDistributionListId?.toInt() ?: -1
                    }

                    if((it.PriceSalesDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
                                where {
                                    criteria("Id", equal = it.PriceSalesDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerGroupPriceListId = custGroupList.PriceSalesDistributionListId?.toInt() ?: -1
                    }

                    if((it.PriceSalesReturnDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
                                where {
                                    criteria("Id", equal = it.PriceSalesReturnDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerGroupReturnPriceListId = custGroupList.PriceSalesReturnDistributionListId?.toInt() ?: -1
                    }

                    if((it.PriceSalesDamagedListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
                                where {
                                    criteria("Id", equal = it.PriceSalesDamagedListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerGroupDamagedPriceListId = custGroupList.PriceSalesDamagedListId?.toInt() ?: -1
                    }

                    if((it.MustStockListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                                where {
                                    criteria("Id", equal = it.MustStockListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.mustStockListId = custGroupList.MustStockListId?.toInt() ?: -1
                    }

                }

                val custList = iDatabaseManager.getSqlDriver().select<SyncCustomerProductDistributionEntity> {
                    where {
                        criteria("Id", customer.id)
                        criteria("OrganizationId", iUserSession.decideWhichOrgIdToBeUsed(customer.organizationId?.toInt() ?: 0))
                        criteria("State", 1)
                    }
                }.firstOrNull()

                custList?.let { custList ->
                    if((custList.ProductSalesDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                                where {
                                    criteria("Id", equal = custList.ProductSalesDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerDistributionListId = custList.ProductSalesDistributionListId?.toInt() ?: -1
                    }

                    if((custList.ProductSalesReturnDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductDistributionEntity> {
                                where {
                                    criteria("Id", equal = custList.ProductSalesReturnDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerReturnDistributionListId = custList.ProductSalesReturnDistributionListId?.toInt() ?: -1
                    }

                    if((custList.PriceSalesDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
                                where {
                                    criteria("Id", equal = custList.PriceSalesDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerPriceListId = custList.PriceSalesDistributionListId?.toInt() ?: -1
                    }

                    if((custList.PriceSalesReturnDistributionListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
                                where {
                                    criteria("Id", equal = custList.PriceSalesReturnDistributionListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerReturnPriceListId = custList.PriceSalesReturnDistributionListId?.toInt() ?: -1
                    }

                    if((custList.PriceSalesDamagedListId ?: 0) > 0){
                        val productList =
                            iDatabaseManager.getSqlDriver().select<SyncProductPricesEntity> {
                                where {
                                    criteria("Id", equal = custList.PriceSalesDamagedListId)
                                    criteria("State", equal = 1)
                                }
                            }.any()
                        if (productList)
                            ret.customerDamagedPriceListId = custList.PriceSalesDamagedListId?.toInt() ?: -1
                    }

                }

                if(paymentPlanId > 0){
                    val cgParam = iDatabaseManager.getSqlDriver().select<SyncCrmPriceListParameterEntity> {
                        where {
                            criteria("EntityType", CrmParameterEntityType.CUSTOMER_GROUP.ordinal)
                            criteria("OrganizationId", iUserSession.decideWhichOrgIdToBeUsed(customer.organizationId?.toInt() ?: 0))
                            criteria("EntityId", customer.groupId)
                            criteria("PaymentPlanId", paymentPlanId)
                            criteria("State", notEqual = 4)
                        }
                    }.firstOrNull()
                    cgParam?.let { param ->
                        param.SalesPriceListId?.let { it ->
                            ret.customerGroupPriceListId = it.toInt()
                        }

                        param.SalesReturnPriceListId?.let { it ->
                            ret.customerGroupReturnPriceListId = it.toInt()
                        }

                        param.PriceSalesDamagedListId?.let { it ->
                            ret.customerDamagedPriceListId = it.toInt()
                        }
                    }
                    val cParam = iDatabaseManager.getSqlDriver().select<SyncCrmPriceListParameterEntity> {
                        where {
                            criteria("EntityType", CrmParameterEntityType.CUSTOMER.ordinal)
                            criteria("OrganizationId", iUserSession.decideWhichOrgIdToBeUsed(customer.organizationId?.toInt() ?: 0))
                            criteria("EntityId", customer.id)
                            criteria("PaymentPlanId", paymentPlanId)
                            criteria("State", notEqual = 4)
                        }
                    }.firstOrNull()
                    cParam?.let { param ->
                        param.SalesPriceListId?.let { it ->
                            ret.customerPriceListId = it.toInt()
                        }

                        param.SalesReturnPriceListId?.let { it ->
                            ret.customerReturnPriceListId = it.toInt()
                        }

                        param.PriceSalesDamagedListId?.let { it ->
                            ret.customerDamagedPriceListId = it.toInt()
                        }
                    }
                }
            }
        }
        return ret
    }

    override suspend fun hasAnyCrmPriceListParameter(customer: SyncCustomerModel, orgId: Int): Boolean {
        val crmParams = iDatabaseManager.getSqlDriver().select<SyncCrmPriceListParameterEntity> {
            where {
                criteria("OrganizationId", orgId)
                criteria("State", notEqual = 4)
            }
        }.toList()

        return crmParams.any {
            (it.EntityType?.toEnum<CrmParameterEntityType>() == CrmParameterEntityType.CUSTOMER && it.EntityId == customer.id) ||
                    (it.EntityType?.toEnum<CrmParameterEntityType>() == CrmParameterEntityType.CUSTOMER_GROUP && it.EntityId == customer.groupId)
        }
    }

    override suspend fun getCrmPriceListIdWithPayterm(customer: SyncCustomerModel, orgId: Int, paymentPlanId: Int): Int {
        val crmParams = iDatabaseManager.getSqlDriver().select<SyncCrmPriceListParameterEntity> {
            where {
                criteria("OrganizationId", orgId)
                criteria("State", notEqual = 4)
            }
        }.toList()
        var ret = 0

        val customerGroupVal = crmParams.singleOrNull { it ->
            it.EntityType?.toEnum<CrmParameterEntityType>() == CrmParameterEntityType.CUSTOMER_GROUP
                    && it.EntityId == customer.groupId && it.PaymentPlanId == paymentPlanId.toLong()
        }

        val customerVal = crmParams.singleOrNull { it ->
            it.EntityType?.toEnum<CrmParameterEntityType>() == CrmParameterEntityType.CUSTOMER
                    && it.EntityId == customer.id && it.PaymentPlanId == paymentPlanId.toLong()
        }

        customerGroupVal?.let { it
            ret = it.SalesPriceListId?.toInt() ?: 0
        }

        customerVal?.let { it ->
            ret = it.SalesPriceListId?.toInt() ?: 0
        }

        return ret
    }
    //endregion

    //region Private Method
    //endregion


}