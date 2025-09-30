package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel

class CustomerEntityDbMapper:Mapper<SyncCustomerEntity, SyncCustomerModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncCustomerEntity): SyncCustomerModel {
      return SyncCustomerModel(
          Id = from.Id,
          Balance = from.Balance,
          Blocked = from.Blocked,
          CheckRiskDispatch = from.CheckRiskDispatch,
          CheckRiskOnBalance = from.CheckRiskOnBalance,
          CheckRiskOrder = from.CheckRiskOrder,
          CheckRiskOrderProposal = from.CheckRiskOrderProposal,
          Code = from.Code,
          DamagedReturnPriceType = from.DamagedReturnPriceType,
          DispatchMonitoringAction =from.DispatchMonitoringAction,
          GroupId = from.GroupId,
          GroupName = from.GroupName,
          GroupPhotoPath = from.GroupPhotoPath,
          IconIndex = from.IconIndex,
          InvoiceMonitoringAction = from.InvoiceMonitoringAction,
          IsECustomer = from.IsECustomer,
          IsVatExempt = from.IsVatExempt,
          IsVisible = from.IsVisible,
          Name = from.Name,
          OrderMonitoringAction = from.OrderMonitoringAction,
          OrderProposalMonitoringAction = from.OrderProposalMonitoringAction,
          OrganizationCode = from.OrganizationCode,
          OrganizationId = from.OrganizationId,
          OrganizationName = from.OrganizationName,
          ParentId = from.ParentId,
          PaymentPlanId = from.PaymentPlanId,
          PhotoPath = from.PhotoPath,
          ReturnPriceType = from.ReturnPriceType,
          Risk = from.Risk,
          RiskDueDay = from.RiskDueDay,
          State = from.State,
          Tags = from.Tags,
          TaxNumber = from.TaxNumber,
          TaxOffice = from.TaxOffice)
    }

    override fun fromDomain(domain: SyncCustomerModel) :SyncCustomerEntity {
        return SyncCustomerEntity(
            Id = domain.Id,
            Balance = domain.Balance,
            Blocked = domain.Blocked,
            CheckRiskDispatch = domain.CheckRiskDispatch,
            CheckRiskOnBalance = domain.CheckRiskOnBalance,
            CheckRiskOrder = domain.CheckRiskOrder,
            CheckRiskOrderProposal = domain.CheckRiskOrderProposal,
            Code = domain.Code,
            DamagedReturnPriceType = domain.DamagedReturnPriceType,
            DispatchMonitoringAction = domain.DispatchMonitoringAction,
            GroupId = domain.GroupId,
            GroupName = domain.GroupName,
            GroupPhotoPath = domain.GroupPhotoPath,
            IconIndex = domain.IconIndex,
            InvoiceMonitoringAction = domain.InvoiceMonitoringAction,
            IsECustomer = domain.IsECustomer,
            IsVatExempt = domain.IsVatExempt,
            IsVisible = domain.IsVisible,
            Name = domain.Name,
            OrderMonitoringAction = domain.OrderMonitoringAction,
            OrderProposalMonitoringAction = domain.OrderProposalMonitoringAction,
            OrganizationCode = domain.OrganizationCode,
            OrganizationId = domain.OrganizationId,
            OrganizationName = domain.OrganizationName,
            ParentId = domain.ParentId,
            PaymentPlanId = domain.PaymentPlanId,
            PhotoPath = domain.PhotoPath,
            ReturnPriceType = domain.ReturnPriceType,
            Risk = domain.Risk,
            RiskDueDay = domain.RiskDueDay,
            State = domain.State,
            Tags = domain.Tags,
            TaxNumber = domain.TaxNumber,
            TaxOffice = domain.TaxOffice)
    }


    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion

}