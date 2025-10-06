package com.repzone.data.mapper

import com.repzone.core.enums.CrmParameterEntityType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.MapperDto
import com.repzone.database.SyncCrmPriceListParameterEntity
import com.repzone.domain.model.SyncCrmPriceListParameterModel
import com.repzone.network.dto.CrmPriceListParameterDto
import kotlin.time.ExperimentalTime

class SyncCrmPriceListParameterEntityDbMapper : MapperDto<SyncCrmPriceListParameterEntity, SyncCrmPriceListParameterModel, CrmPriceListParameterDto> {
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
            entityType = from.EntityType?.toEnum<CrmParameterEntityType>() ?: CrmParameterEntityType.CUSTOMER,
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
            EntityType = domain.entityType.enumToLong(),
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

    @OptIn(ExperimentalTime::class)
    override fun fromDto(dto: CrmPriceListParameterDto): SyncCrmPriceListParameterEntity {
        return SyncCrmPriceListParameterEntity(
            Id = dto.id.toLong(),
            EntityId = dto.entityId?.toLong(),
            EntityType = dto.entityType.enumToLong(),
            ModificationDateUtc = dto.modificationDateUtc?.toEpochMilliseconds(),
            OrganizationId = dto.organizationId?.toLong(),
            PaymentPlanId = dto.paymentPlanId?.toLong(),
            PricePurchaseReturnDamagedListId = dto.pricePurchaseReturnDamagedListId?.toLong(),
            PriceSalesDamagedListId = dto.priceSalesDamagedListId?.toLong(),
            PurchasePriceListId = dto.purchasePriceListId?.toLong(),
            PurchaseReturnPriceListId = dto.purchaseReturnPriceListId?.toLong(),
            RecordDateUtc = dto.recordDateUtc?.toEpochMilliseconds(),
            SalesPriceListId = dto.salesPriceListId?.toLong(),
            SalesReturnPriceListId = dto.salesReturnPriceListId?.toLong(),
            State = dto.state.toLong()
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
