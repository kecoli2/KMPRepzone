package com.repzone.data.repository.imp

import com.repzone.core.enums.StateType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.util.extensions.toBoolean
import com.repzone.database.SyncPaymentPlanEntity
import com.repzone.database.interfaces.IDatabaseManager
import com.repzone.database.runtime.select
import com.repzone.domain.model.PaymentPlanModel
import com.repzone.domain.repository.IPaymentInformationRepository

class PaymentInformationRepositoryImpl(private val iDatabaseManager: IDatabaseManager,
                                       private val iUserSession: IUserSession): IPaymentInformationRepository {
    //region Field
    //endregion Field

    //region Constructor
    //endregion Constructor

    //region Public Method
    override suspend fun getPaymentInformation(customerOrgIdOrganizationId: Int): List<PaymentPlanModel> {
        val customerOrgId = iUserSession.decideWhichOrgIdToBeUsed(customerOrgIdOrganizationId)
        val list = iDatabaseManager.getSqlDriver().select<SyncPaymentPlanEntity> {
            where {
                criteria("State", StateType.ACTIVE.ordinal)
            }
            orderBy {
                order("Ids")
            }
        }.toList()

        val filtered = list
            .filter { plan ->
                val fixedIds = fixIds(plan.Ids)
                fixedIds.isEmpty() || ",$customerOrgId," in fixedIds
            }
            .map { plan ->
                PaymentPlanModel(
                    code = plan.Code ?: "",
                    name = plan.Name ?: "",
                    id = plan.Id.toInt(),
                    isDefault = plan.IsDefault?.toBoolean() ?: false
                )
            }
        return filtered.sortedByDescending { it.isDefault }

    }
    //endregion Public Method

    //region Private Method
    private fun fixIds(ids: String?): String =  ids?.split(',')?.joinToString(",") { it.trim() } ?: ""
    //endregion  Private Method
}