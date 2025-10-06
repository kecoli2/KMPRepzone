package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCrmPriceListParameterEntity
import com.repzone.domain.model.SyncCrmPriceListParameterModel

class SyncCrmPriceListParameterEntityDbMapper : Mapper<SyncCrmPriceListParameterEntity, SyncCrmPriceListParameterModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncCrmPriceListParameterEntity): SyncCrmPriceListParameterModel {
        return SyncCrmPriceListParameterModel(
            id = from.Id,
            entityId = from.EntityId,
            entityType = from.EntityType,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            paymentPlanId = from.PaymentPlanId,
            pricePurchaseReturnDamagedListId = from.PricePurchaseReturnDamagedListId,
            priceSalesDamagedListId = from.PriceSalesDamagedListId,
            purchasePriceListId = from.PurchasePriceListId,
            purchaseReturnPriceListId = from.PurchaseReturnPriceListId,
            recordDateUtc = from.RecordDateUtc,
            salesPriceListId = from.SalesPriceListId,
            salesReturnPriceListId = from.SalesReturnPriceListId,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncCrmPriceListParameterModel): SyncCrmPriceListParameterEntity {
        return SyncCrmPriceListParameterEntity(
            Id = domain.id,
            EntityId = domain.entityId,
            EntityType = domain.entityType,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            PaymentPlanId = domain.paymentPlanId,
            PricePurchaseReturnDamagedListId = domain.pricePurchaseReturnDamagedListId,
            PriceSalesDamagedListId = domain.priceSalesDamagedListId,
            PurchasePriceListId = domain.purchasePriceListId,
            PurchaseReturnPriceListId = domain.purchaseReturnPriceListId,
            RecordDateUtc = domain.recordDateUtc,
            SalesPriceListId = domain.salesPriceListId,
            SalesReturnPriceListId = domain.salesReturnPriceListId,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
