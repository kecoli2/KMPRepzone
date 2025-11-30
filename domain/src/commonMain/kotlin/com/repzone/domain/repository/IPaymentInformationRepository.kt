package com.repzone.domain.repository

import com.repzone.domain.model.PaymentPlanModel

interface IPaymentInformationRepository {
    suspend fun getPaymentInformation(customerOrgIdOrganizationId: Long): List<PaymentPlanModel>
}