package com.repzone.sync.service

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEntity
import com.repzone.domain.model.SyncCustomerModel
import com.repzone.sync.service.bulk.base.RawSqlBulkInsertService
import com.repzone.sync.transaction.TransactionCoordinator

class CustomerRawSqlBulkInsertService(
    private val dbMapper: Mapper<SyncCustomerEntity, SyncCustomerModel>,
    coordinator: TransactionCoordinator): RawSqlBulkInsertService<SyncCustomerModel>(coordinator) {
    //region Field
    override val tableName = "SyncCustomerEntity"
    override val insertColumns = listOf(
        "Id",
        "Balance",
        "Blocked",
        "CheckRiskDispatch",
        "CheckRiskOnBalance",
        "CheckRiskOrder",
        "CheckRiskOrderProposal",
        "Code",
        "DamagedReturnPriceType",
        "DispatchMonitoringAction",
        "GroupId",
        "GroupName",
        "GroupPhotoPath",
        "IconIndex",
        "InvoiceMonitoringAction",
        "IsECustomer",
        "IsVatExempt",
        "IsVisible",
        "Name",
        "OrderMonitoringAction",
        "OrderProposalMonitoringAction",
        "OrganizationCode",
        "OrganizationId",
        "OrganizationName",
        "ParentId",
        "PaymentPlanId",
        "PhotoPath",
        "ReturnPriceType",
        "Risk",
        "RiskDueDay",
        "State",
        "Tags",
        "TaxNumber",
        "TaxOffice")

    override val clearSql = "DELETE FROM SyncCustomerEntity"
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun getValues(item: SyncCustomerModel): List<Any?> {
        val dbitem = dbMapper.fromDomain(item)
        return listOf(
            dbitem.Id,
            dbitem.Balance,
            dbitem.Blocked,
            dbitem.CheckRiskDispatch,
            dbitem.CheckRiskOnBalance,
            dbitem.CheckRiskOrder,
            dbitem.CheckRiskOrderProposal,
            dbitem.Code,
            dbitem.DamagedReturnPriceType,
            dbitem.DispatchMonitoringAction,
            dbitem.GroupId,
            dbitem.GroupName,
            dbitem.GroupPhotoPath,
            dbitem.IconIndex,
            dbitem.InvoiceMonitoringAction,
            dbitem.IsECustomer,
            dbitem.IsVatExempt,
            dbitem.IsVisible,
            dbitem.Name,
            dbitem.OrderMonitoringAction,
            dbitem.OrderProposalMonitoringAction,
            dbitem.OrganizationCode,
            dbitem.OrganizationId,
            dbitem.OrganizationName,
            dbitem.ParentId,
            dbitem.PaymentPlanId,
            dbitem.PhotoPath,
            dbitem.ReturnPriceType,
            dbitem.Risk,
            dbitem.RiskDueDay,
            dbitem.State,
            dbitem.Tags,
            dbitem.TaxNumber,
            dbitem.TaxOffice
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}