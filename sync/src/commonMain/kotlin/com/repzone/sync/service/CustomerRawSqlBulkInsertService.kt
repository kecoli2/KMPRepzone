package com.repzone.sync.service

import com.repzone.core.util.extensions.quote
import com.repzone.data.mapper.CustomerEntityDbMapper
import com.repzone.database.SyncAddressEntity
import com.repzone.database.SyncCustomerEntity
import com.repzone.network.dto.CustomerDto
import com.repzone.sync.service.bulk.base.CompositeRawSqlBulkInsertService
import com.repzone.sync.transaction.CompositeOperation
import com.repzone.sync.transaction.TableOperation
import com.repzone.sync.transaction.TransactionCoordinator

class CustomerRawSqlBulkInsertService(private val mapper: CustomerEntityDbMapper, coordinator: TransactionCoordinator):
    CompositeRawSqlBulkInsertService<List<CustomerDto>>(coordinator) {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun buildCompositeOperation( items: List<CustomerDto>, includeClears: Boolean, useUpsert: Boolean,): CompositeOperation {
        val customerEntity = items.map { mapper.fromDto(it) }
        val addresstEntity = items.flatMap { it ->
            mapper.fromDtoAdress(it.addresses, it.id.toLong())
        }
        
        val operation = listOf(
            TableOperation(
                tableName = "SyncCustomerEntity",
                clearSql = null,
                columns = listOf("Id", "Balance", "Blocked", "CheckRiskDispatch", "CheckRiskOnBalance",
                    "CheckRiskOrder", "CheckRiskOrderProposal", "Code", "DamagedReturnPriceType", "DispatchMonitoringAction",
                    "GroupId", "GroupName", "GroupPhotoPath", "IconIndex", "InvoiceMonitoringAction", "IsECustomer", "IsVatExempt",
                    "IsVisible", "Name", "OrderMonitoringAction", "OrderProposalMonitoringAction", "OrganizationCode", "OrganizationId",
                    "OrganizationName", "ParentId", "PaymentPlanId", "PhotoPath", "ReturnPriceType", "Risk", "RiskDueDay", "State", "Tags", "TaxNumber", "TaxOffice"
                ),
                values = bindCustomerValues(customerEntity),
                recordCount = customerEntity.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            ),
            TableOperation(
                tableName = "SyncAddressEntity",
                clearSql = null,
                columns = listOf("Id", "AddressName", "AddressType", "City", "Contact", "Country", "CustomerId", "District", "FaxNumber", "Latitude", "Longitude", "PhoneNumber", "State", "Street", "Street2"),
                values = bindAdressValues(addresstEntity),
                recordCount = addresstEntity.size,
                useUpsert = useUpsert,
                includeClears = includeClears
            )
        )

        return CompositeOperation(
            operations = operation,
            description = "Tüm Müşteriler Sync Yapıldı"
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    private fun bindCustomerValues(list: List<SyncCustomerEntity>): List<String> {
        return list.map { it ->
                listOf(
                    it.Id,
                    it.Balance,
                    it.Blocked,
                    it.CheckRiskDispatch,
                    it.CheckRiskOnBalance,
                    it.CheckRiskOrder,
                    it.CheckRiskOrderProposal,
                    it.Code?.quote(),
                    it.DamagedReturnPriceType,
                    it.DispatchMonitoringAction,
                    it.GroupId,
                    it.GroupName?.quote(),
                    it.GroupPhotoPath?.quote(),
                    it.IconIndex,
                    it.InvoiceMonitoringAction,
                    it.IsECustomer,
                    it.IsVatExempt,
                    it.IsVisible,
                    it.Name?.quote(),
                    it.OrderMonitoringAction,
                    it.OrderProposalMonitoringAction,
                    it.OrganizationCode?.quote(),
                    it.OrganizationId,
                    it.OrganizationName?.quote(),
                    it.ParentId,
                    it.PaymentPlanId,
                    it.PhotoPath?.quote(),
                    it.ReturnPriceType,
                    it.Risk,
                    it.RiskDueDay,
                    it.State,
                    it.Tags?.quote(),
                    it.TaxNumber?.quote(),
                    it.TaxOffice?.quote()
                ).joinToString(", ", prefix = "(", postfix = ")")
        }

    }

    private fun bindAdressValues(list: List<SyncAddressEntity>): List<String>{
        return list.map { it ->
            listOf(
                it.Id,
                it.AddressName?.quote(),
                it.AddressType,
                it.City?.quote(),
                it.Contact?.quote(),
                it.Country?.quote(),
                it.CustomerId,
                it.District?.quote(),
                it.FaxNumber?.quote(),
                it.Latitude,
                it.Longitude,
                it.PhoneNumber?.quote(),
                it.State,
                it.Street?.quote(),
                it.Street2?.quote()
            ).joinToString(", ", prefix = "(", postfix = ")")
        }
    }
    //endregion
}