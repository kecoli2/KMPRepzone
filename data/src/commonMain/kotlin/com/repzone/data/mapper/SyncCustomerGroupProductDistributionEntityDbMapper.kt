package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerGroupProductDistributionEntity
import com.repzone.domain.model.SyncCustomerGroupProductDistributionModel

class SyncCustomerGroupProductDistributionEntityDbMapper : Mapper<SyncCustomerGroupProductDistributionEntity, SyncCustomerGroupProductDistributionModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerGroupProductDistributionEntity): SyncCustomerGroupProductDistributionModel {
        return SyncCustomerGroupProductDistributionModel(
            id = from.Id,
            modificationDateUtc = from.ModificationDateUtc,
            mustStockListId = from.MustStockListId,
            organizationId = from.OrganizationId,
            pricePurchaseReturnDamagedListId = from.PricePurchaseReturnDamagedListId,
            priceSalesDamagedListId = from.PriceSalesDamagedListId,
            priceSalesDistributionListId = from.PriceSalesDistributionListId,
            priceSalesReturnDistributionListId = from.PriceSalesReturnDistributionListId,
            productSalesDistributionListId = from.ProductSalesDistributionListId,
            productSalesReturnDistributionListId = from.ProductSalesReturnDistributionListId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncCustomerGroupProductDistributionModel): SyncCustomerGroupProductDistributionEntity {
        return SyncCustomerGroupProductDistributionEntity(
            Id = domain.id,
            ModificationDateUtc = domain.modificationDateUtc,
            MustStockListId = domain.mustStockListId,
            OrganizationId = domain.organizationId,
            PricePurchaseReturnDamagedListId = domain.pricePurchaseReturnDamagedListId,
            PriceSalesDamagedListId = domain.priceSalesDamagedListId,
            PriceSalesDistributionListId = domain.priceSalesDistributionListId,
            PriceSalesReturnDistributionListId = domain.priceSalesReturnDistributionListId,
            ProductSalesDistributionListId = domain.productSalesDistributionListId,
            ProductSalesReturnDistributionListId = domain.productSalesReturnDistributionListId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
