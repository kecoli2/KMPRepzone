package com.repzone.data.mapper

import com.repzone.core.enums.MonitoringActionType
import com.repzone.core.enums.PriceType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toBoolean
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel

class SyncCustomerEntityDbMapper : Mapper<SyncCustomerEntity, SyncCustomerModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerEntity): SyncCustomerModel {
        return SyncCustomerModel(
            id = from.Id,
            balance = from.Balance,
            blocked = from.Blocked,
            checkRiskDispatch = from.CheckRiskDispatch,
            checkRiskOnBalance = from.CheckRiskOnBalance,
            checkRiskOrder = from.CheckRiskOrder,
            checkRiskOrderProposal = from.CheckRiskOrderProposal,
            code = from.Code,
            damagedReturnPriceType = from.DamagedReturnPriceType?.toEnum<PriceType>(),
            dispatchMonitoringAction = from.DispatchMonitoringAction?.toEnum<MonitoringActionType>(),
            groupId = from.GroupId,
            groupName = from.GroupName,
            groupPhotoPath = from.GroupPhotoPath,
            iconIndex = from.IconIndex,
            invoiceMonitoringAction = from.InvoiceMonitoringAction?.toEnum<MonitoringActionType>(),
            isECustomer = from.IsECustomer?.toBoolean() ?: false,
            isVatExempt = from.IsVatExempt?.toBoolean() ?: false,
            isVisible = from.IsVisible?.toBoolean() ?: false,
            name = from.Name,
            orderMonitoringAction = from.OrderMonitoringAction?.toEnum<MonitoringActionType>(),
            orderProposalMonitoringAction = from.OrderProposalMonitoringAction?.toEnum<MonitoringActionType>(),
            organizationCode = from.OrganizationCode,
            organizationId = from.OrganizationId,
            organizationName = from.OrganizationName,
            parentId = from.ParentId,
            paymentPlanId = from.PaymentPlanId,
            photoPath = from.PhotoPath,
            returnPriceType = from.ReturnPriceType?.toEnum<PriceType>(),
            risk = from.Risk,
            riskDueDay = from.RiskDueDay,
            state = from.State,
            tags = from.Tags,
            taxNumber = from.TaxNumber,
            taxOffice = from.TaxOffice
        )
    }

    override fun fromDomain(domain: SyncCustomerModel): SyncCustomerEntity {
        return SyncCustomerEntity(
            Id = domain.id,
            Balance = domain.balance,
            Blocked = domain.blocked,
            CheckRiskDispatch = domain.checkRiskDispatch,
            CheckRiskOnBalance = domain.checkRiskOnBalance,
            CheckRiskOrder = domain.checkRiskOrder,
            CheckRiskOrderProposal = domain.checkRiskOrderProposal,
            Code = domain.code,
            DamagedReturnPriceType = domain.damagedReturnPriceType?.enumToLong(),
            DispatchMonitoringAction = domain.dispatchMonitoringAction?.enumToLong(),
            GroupId = domain.groupId,
            GroupName = domain.groupName,
            GroupPhotoPath = domain.groupPhotoPath,
            IconIndex = domain.iconIndex,
            InvoiceMonitoringAction = domain.invoiceMonitoringAction?.enumToLong(),
            IsECustomer = domain.isECustomer.toLong(),
            IsVatExempt = domain.isVatExempt.toLong(),
            IsVisible = domain.isVisible.toLong(),
            Name = domain.name,
            OrderMonitoringAction = domain.orderMonitoringAction?.enumToLong(),
            OrderProposalMonitoringAction = domain.orderProposalMonitoringAction?.enumToLong(),
            OrganizationCode = domain.organizationCode,
            OrganizationId = domain.organizationId,
            OrganizationName = domain.organizationName,
            ParentId = domain.parentId,
            PaymentPlanId = domain.paymentPlanId,
            PhotoPath = domain.photoPath,
            ReturnPriceType = domain.returnPriceType?.enumToLong(),
            Risk = domain.risk,
            RiskDueDay = domain.riskDueDay,
            State = domain.state,
            Tags = domain.tags,
            TaxNumber = domain.taxNumber,
            TaxOffice = domain.taxOffice
        )
    }
    //endregion

}
