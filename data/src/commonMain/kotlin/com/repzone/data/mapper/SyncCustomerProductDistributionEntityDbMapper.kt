package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerProductDistributionEntity
import com.repzone.domain.model.SyncCustomerProductDistributionModel

class SyncCustomerProductDistributionEntityDbMapper : Mapper<SyncCustomerProductDistributionEntity, SyncCustomerProductDistributionModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerProductDistributionEntity): SyncCustomerProductDistributionModel {
        return SyncCustomerProductDistributionModel(
            id = from.Id,
            modificationDateUtc = from.ModificationDateUtc,
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

    override fun fromDomain(domain: SyncCustomerProductDistributionModel): SyncCustomerProductDistributionEntity {
        return SyncCustomerProductDistributionEntity(
            Id = domain.id,
            ModificationDateUtc = domain.modificationDateUtc,
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
