package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerGroupProductDistributionEntity
import com.repzone.domain.model.SyncCustomerGroupProductDistributionModel
import com.repzone.network.dto.SyncCustomerGroupProductDistributionDto
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
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
            State = domain.state.enumToLong()
        )
    }

    fun fromDto(dto : SyncCustomerGroupProductDistributionDto): SyncCustomerGroupProductDistributionEntity {
        return SyncCustomerGroupProductDistributionEntity(
            Id = dto.id.toLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            MustStockListId = dto.mustStockListId.toLong(),
            OrganizationId = dto.organizationId.toLong(),
            PricePurchaseReturnDamagedListId = dto.pricePurchaseReturnDamagedListId?.toLong(),
            PriceSalesDamagedListId = dto.priceSalesDamagedListId?.toLong(),
            PriceSalesDistributionListId = dto.priceSalesDistributionListId.toLong(),
            PriceSalesReturnDistributionListId = dto.priceSalesReturnDistributionListId.toLong(),
            ProductSalesDistributionListId = dto.productSalesDistributionListId.toLong(),
            ProductSalesReturnDistributionListId = dto.productSalesReturnDistributionListId.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            State = dto.state.enumToLong()
        )

    }
    //endregion

}
