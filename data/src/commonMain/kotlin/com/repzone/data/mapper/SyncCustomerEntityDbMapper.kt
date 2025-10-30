package com.repzone.data.mapper

import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel

/**
 * Auto-generated mapper for SyncCustomerEntity
 * Maps between Entity (database) and Model (domain)
 */
object SyncCustomerEntityDbMapper {

    /**
     * Convert Entity to Model
     */
    fun SyncCustomerEntity.toModel(): SyncCustomerModel {
        return SyncCustomerModel(
            Id = this.Id,
            Balance = this.Balance,
            Blocked = this.Blocked,
            CheckRiskDispatch = this.CheckRiskDispatch,
            CheckRiskOnBalance = this.CheckRiskOnBalance,
            CheckRiskOrder = this.CheckRiskOrder,
            CheckRiskOrderProposal = this.CheckRiskOrderProposal,
            Code = this.Code,
            DamagedReturnPriceType = this.DamagedReturnPriceType,
            DispatchMonitoringAction = this.DispatchMonitoringAction,
            GroupId = this.GroupId,
            GroupName = this.GroupName,
            GroupPhotoPath = this.GroupPhotoPath,
            IconIndex = this.IconIndex,
            InvoiceMonitoringAction = this.InvoiceMonitoringAction,
            IsECustomer = this.IsECustomer,
            IsVatExempt = this.IsVatExempt,
            IsVisible = this.IsVisible,
            Name = this.Name,
            OrderMonitoringAction = this.OrderMonitoringAction,
            OrderProposalMonitoringAction = this.OrderProposalMonitoringAction,
            OrganizationCode = this.OrganizationCode,
            OrganizationId = this.OrganizationId,
            OrganizationName = this.OrganizationName,
            ParentId = this.ParentId,
            PaymentPlanId = this.PaymentPlanId,
            PhotoPath = this.PhotoPath,
            ReturnPriceType = this.ReturnPriceType,
            Risk = this.Risk,
            RiskDueDay = this.RiskDueDay,
            State = this.State,
            Tags = this.Tags,
            TaxNumber = this.TaxNumber,
            TaxOffice = this.TaxOffice
        )
    }

    /**
     * Convert Model to Entity
     */
    fun SyncCustomerModel.toEntity(): SyncCustomerEntity {
        return SyncCustomerEntity(
            Id = this.Id,
            Balance = this.Balance,
            Blocked = this.Blocked,
            CheckRiskDispatch = this.CheckRiskDispatch,
            CheckRiskOnBalance = this.CheckRiskOnBalance,
            CheckRiskOrder = this.CheckRiskOrder,
            CheckRiskOrderProposal = this.CheckRiskOrderProposal,
            Code = this.Code,
            DamagedReturnPriceType = this.DamagedReturnPriceType,
            DispatchMonitoringAction = this.DispatchMonitoringAction,
            GroupId = this.GroupId,
            GroupName = this.GroupName,
            GroupPhotoPath = this.GroupPhotoPath,
            IconIndex = this.IconIndex,
            InvoiceMonitoringAction = this.InvoiceMonitoringAction,
            IsECustomer = this.IsECustomer,
            IsVatExempt = this.IsVatExempt,
            IsVisible = this.IsVisible,
            Name = this.Name,
            OrderMonitoringAction = this.OrderMonitoringAction,
            OrderProposalMonitoringAction = this.OrderProposalMonitoringAction,
            OrganizationCode = this.OrganizationCode,
            OrganizationId = this.OrganizationId,
            OrganizationName = this.OrganizationName,
            ParentId = this.ParentId,
            PaymentPlanId = this.PaymentPlanId,
            PhotoPath = this.PhotoPath,
            ReturnPriceType = this.ReturnPriceType,
            Risk = this.Risk,
            RiskDueDay = this.RiskDueDay,
            State = this.State,
            Tags = this.Tags,
            TaxNumber = this.TaxNumber,
            TaxOffice = this.TaxOffice
        )
    }

    /**
     * Convert List of Entities to List of Models
     */
    fun List<SyncCustomerEntity>.toModelList(): List<SyncCustomerModel> {
        return map { it.toModel() }
    }

    /**
     * Convert List of Models to List of Entities
     */
    fun List<SyncCustomerModel>.toEntityList(): List<SyncCustomerEntity> {
        return map { it.toEntity() }
    }
}
