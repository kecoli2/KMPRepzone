package com.repzone.data.repository.imp

import com.repzone.domain.model.DistributionControllerModel
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.domain.repository.IDistributionRepository

class DistributionRepositoryPreviewImpl: IDistributionRepository {
    //region Field
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    //endregion Public Method

    //region Private Method
    //endregion  Private Method
    override suspend fun getActiveDistributionListId(
        customer: SyncCustomerModel?,
        paymentPlanId: Int,
    ): DistributionControllerModel? {
        return null
    }

    override suspend fun hasAnyCrmPriceListParameter(
        customer: SyncCustomerModel,
        orgId: Int,
    ): Boolean {
        return true
    }

    override suspend fun getCrmPriceListIdWithPayterm(
        customer: SyncCustomerModel,
        orgId: Int,
        paymentPlanId: Int,
    ): Int {
        return 1
    }
}