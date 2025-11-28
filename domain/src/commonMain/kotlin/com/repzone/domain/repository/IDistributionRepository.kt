package com.repzone.domain.repository

import com.repzone.domain.model.DistributionControllerModel
import com.repzone.domain.model.SyncCustomerModel

interface IDistributionRepository {
    suspend fun getActiveDistributionListId(customer: SyncCustomerModel?, paymentPlanId: Int = 0): DistributionControllerModel?

    suspend fun hasAnyCrmPriceListParameter(customer: SyncCustomerModel, orgId: Int): Boolean

    suspend fun getCrmPriceListIdWithPayterm(customer: SyncCustomerModel, orgId: Int, paymentPlanId: Int): Int
}