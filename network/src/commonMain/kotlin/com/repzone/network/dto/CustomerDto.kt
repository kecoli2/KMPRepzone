package com.repzone.network.dto

import com.repzone.core.enums.MonitoringActionType
import com.repzone.core.enums.PriceType
import com.repzone.core.util.InstantSerializer
import com.repzone.network.util.OrdinalEnumSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class CustomerDto(
    val id: Int,
    val state: Int,
    @Serializable(with = InstantSerializer::class)
    val modificationDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val recordDateUtc: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updateTime: Instant? = null,
    val name: String? = null,
    val organizationId: Int? = null,
    val tags: List<String> = emptyList(),
    val addresses: List<MobileAddressDto> = emptyList(),
    val code: String? = null,
    val groupId: Int? = null,
    val groupName: String? = null,
    val groupIconIndex: Int? = null,
    val organizationCode: String? = null,
    val photoPath: String? = null,
    val taxNumber: String? = null,
    val taxOffice: String? = null,
    val organizationName: String? = null,
    val isECustomer: Boolean,
    val risk: Double? = null,
    val balance: Double? = null,
    val riskDueDay: Int? = null,
    val isVisible: Boolean,
    val groupPhotoPath: String? = null,
    val parentId: Int? = null,
    val paymentPlanId: Int? = null,
    val checkRiskOnBalance: Boolean,
    val checkRiskDispatch: Boolean,
    val checkRiskOrder: Boolean,
    val checkRiskOrderProposal: Boolean,
    val invoiceMonitoringAction: MonitoringActionType,
    val dispatchMonitoringAction: MonitoringActionType,
    val orderMonitoringAction: MonitoringActionType,
    val orderProposalMonitoringAction: MonitoringActionType,
    val blocked: Boolean,
    val isVatExempt: Boolean,
    val returnPriceType: PriceType,
    val damagedReturnPriceType: PriceType
)