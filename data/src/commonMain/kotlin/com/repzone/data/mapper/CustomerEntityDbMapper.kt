package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel

class CustomerEntityDbMapper: Mapper<SyncCustomerEntity, SyncCustomerModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

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
          damagedReturnPriceType = from.DamagedReturnPriceType,
          dispatchMonitoringAction =from.DispatchMonitoringAction,
          groupId = from.GroupId,
          groupName = from.GroupName,
          groupPhotoPath = from.GroupPhotoPath,
          iconIndex = from.IconIndex,
          invoiceMonitoringAction = from.InvoiceMonitoringAction,
          isECustomer = from.IsECustomer?.let { it != 0L } ?: false,
          isVatExempt = from.IsVatExempt?.let { it != 0L } ?: false,
          isVisible = from.IsVisible?.let { it != 0L } ?: false,
          name = from.Name,
          orderMonitoringAction = from.OrderMonitoringAction,
          orderProposalMonitoringAction = from.OrderProposalMonitoringAction,
          organizationCode = from.OrganizationCode,
          organizationId = from.OrganizationId,
          organizationName = from.OrganizationName,
          parentId = from.ParentId,
          paymentPlanId = from.PaymentPlanId,
          photoPath = from.PhotoPath,
          returnPriceType = from.ReturnPriceType,
          risk = from.Risk,
          riskDueDay = from.RiskDueDay,
          state = from.State,
          tags = from.Tags,
          taxNumber = from.TaxNumber,
          taxOffice = from.TaxOffice)
    }

    override fun fromDomain(domain: SyncCustomerModel) :SyncCustomerEntity {
        return SyncCustomerEntity(
            Id = domain.id,
            Balance = domain.balance,
            Blocked = domain.blocked,
            CheckRiskDispatch = domain.checkRiskDispatch,
            CheckRiskOnBalance = domain.checkRiskOnBalance,
            CheckRiskOrder = domain.checkRiskOrder,
            CheckRiskOrderProposal = domain.checkRiskOrderProposal,
            Code = domain.code,
            DamagedReturnPriceType = domain.damagedReturnPriceType,
            DispatchMonitoringAction = domain.dispatchMonitoringAction,
            GroupId = domain.groupId,
            GroupName = domain.groupName,
            GroupPhotoPath = domain.groupPhotoPath,
            IconIndex = domain.iconIndex,
            InvoiceMonitoringAction = domain.invoiceMonitoringAction,
            IsECustomer = domain.isECustomer.let { if (it) 1 else 0 },
            IsVatExempt = domain.isVatExempt.let { if (it) 1 else 0 },
            IsVisible = domain.isVisible.let { if (it) 1 else 0 },
            Name = domain.name,
            OrderMonitoringAction = domain.orderMonitoringAction,
            OrderProposalMonitoringAction = domain.orderProposalMonitoringAction,
            OrganizationCode = domain.organizationCode,
            OrganizationId = domain.organizationId,
            OrganizationName = domain.organizationName,
            ParentId = domain.parentId,
            PaymentPlanId = domain.paymentPlanId,
            PhotoPath = domain.photoPath,
            ReturnPriceType = domain.returnPriceType,
            Risk = domain.risk,
            RiskDueDay = domain.riskDueDay,
            State = domain.state,
            Tags = domain.tags,
            TaxNumber = domain.taxNumber,
            TaxOffice = domain.taxOffice)
    }


    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}