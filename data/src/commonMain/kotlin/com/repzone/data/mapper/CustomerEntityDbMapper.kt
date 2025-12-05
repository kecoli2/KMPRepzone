package com.repzone.data.mapper

import com.repzone.core.enums.MonitoringActionType
import com.repzone.core.enums.PriceType
import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.joinList
import com.repzone.core.util.extensions.toEnum
import com.repzone.core.util.extensions.toLong
import com.repzone.data.util.Mapper
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncAddressEntity
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.network.dto.CustomerDto
import com.repzone.network.dto.MobileAddressDto

class CustomerEntityDbMapper: MapperDto<SyncCustomerEntity, SyncCustomerModel, CustomerDto> {
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
          isECustomer = from.IsECustomer?.let { it != 0L } ?: false,
          isVatExempt = from.IsVatExempt?.let { it != 0L } ?: false,
          isVisible = from.IsVisible?.let { it != 0L } ?: false,
          name = from.Name,
          orderMonitoringAction = from.OrderMonitoringAction?.toEnum<MonitoringActionType>(),
          orderProposalMonitoringAction = from.OrderProposalMonitoringAction?.toEnum<MonitoringActionType>(),
          organizationCode = from.OrganizationCode,
          organizationId = from.OrganizationId?.toInt() ?: 0,
          organizationName = from.OrganizationName,
          parentId = from.ParentId,
          paymentPlanId = from.PaymentPlanId,
          photoPath = from.PhotoPath,
          returnPriceType = from.ReturnPriceType?.toEnum<PriceType>(),
          risk = from.Risk,
          riskDueDay = from.RiskDueDay,
          state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE,
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
            DamagedReturnPriceType = domain.damagedReturnPriceType?.enumToLong(),
            DispatchMonitoringAction = domain.dispatchMonitoringAction?.enumToLong(),
            GroupId = domain.groupId,
            GroupName = domain.groupName,
            GroupPhotoPath = domain.groupPhotoPath,
            IconIndex = domain.iconIndex,
            InvoiceMonitoringAction = domain.invoiceMonitoringAction?.enumToLong(),
            IsECustomer = domain.isECustomer.let { if (it) 1 else 0 },
            IsVatExempt = domain.isVatExempt.let { if (it) 1 else 0 },
            IsVisible = domain.isVisible.let { if (it) 1 else 0 },
            Name = domain.name,
            OrderMonitoringAction = domain.orderMonitoringAction?.enumToLong(),
            OrderProposalMonitoringAction = domain.orderProposalMonitoringAction?.enumToLong(),
            OrganizationCode = domain.organizationCode,
            OrganizationId = domain.organizationId.toLong(),
            OrganizationName = domain.organizationName,
            ParentId = domain.parentId,
            PaymentPlanId = domain.paymentPlanId,
            PhotoPath = domain.photoPath,
            ReturnPriceType = domain.returnPriceType?.enumToLong(),
            Risk = domain.risk,
            RiskDueDay = domain.riskDueDay,
            State = domain.state.enumToLong(),
            Tags = domain.tags,
            TaxNumber = domain.taxNumber,
            TaxOffice = domain.taxOffice)
    }

    override fun fromDto(dto: CustomerDto): SyncCustomerEntity {
        return SyncCustomerEntity(
            Id = dto.id.toLong(),
            Balance = dto.balance,
            Blocked = dto.blocked.toLong(),
            CheckRiskDispatch = dto.checkRiskDispatch.toLong(),
            CheckRiskOnBalance = dto.checkRiskOnBalance.toLong(),
            CheckRiskOrder = dto.checkRiskOrder.toLong(),
            CheckRiskOrderProposal = dto.checkRiskOrderProposal.toLong(),
            Code = dto.code,
            DamagedReturnPriceType = dto.damagedReturnPriceType.enumToLong(),
            DispatchMonitoringAction = dto.dispatchMonitoringAction.enumToLong(),
            GroupId = dto.groupId?.toLong(),
            GroupName = dto.groupName,
            GroupPhotoPath = dto.groupPhotoPath,
            IconIndex = dto.groupIconIndex?.toLong(),
            InvoiceMonitoringAction = dto.invoiceMonitoringAction.enumToLong(),
            IsECustomer = dto.isECustomer.toLong(),
            IsVatExempt = dto.isVatExempt.toLong(),
            IsVisible = dto.isVisible.toLong(),
            Name = dto.name,
            OrderMonitoringAction = dto.orderMonitoringAction.enumToLong(),
            OrderProposalMonitoringAction = dto.orderProposalMonitoringAction.enumToLong(),
            OrganizationCode = dto.organizationCode,
            OrganizationId = dto.organizationId?.toLong(),
            OrganizationName = dto.organizationName,
            ParentId = dto.parentId?.toLong(),
            PaymentPlanId = dto.paymentPlanId?.toLong(),
            PhotoPath = dto.photoPath,
            ReturnPriceType = dto.returnPriceType.enumToLong(),
            Risk = dto.risk,
            RiskDueDay = dto.riskDueDay?.toLong(),
            State = dto.state.toLong(),
            Tags = dto.tags.joinList(),
            TaxNumber = dto.taxNumber,
            TaxOffice = dto.taxOffice
        )
    }

    fun fromDtoAdress(list: List<MobileAddressDto>, customerId: Long?): List<SyncAddressEntity>{
        return list.map { dto ->
             SyncAddressEntity(
                Id = dto.id.toLong(),
                AddressName = dto.name,
                AddressType = dto.type.enumToLong(),
                City = dto.city,
                Contact = dto.responsible,
                Country = dto.country,
                CustomerId = customerId,
                District = dto.district,
                FaxNumber = dto.fax,
                Latitude = dto.latitude,
                Longitude = dto.longitude,
                PhoneNumber = dto.phone,
                State = dto.state.toLong(),
                Street = dto.street,
                Street2 = dto.street2
            )
        }
    }


    //endregion


}