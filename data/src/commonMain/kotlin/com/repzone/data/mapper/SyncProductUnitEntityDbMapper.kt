package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncProductUnitEntity
import com.repzone.domain.model.SyncProductUnitModel

class SyncProductUnitEntityDbMapper: Mapper<SyncProductUnitEntity, SyncProductUnitModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncProductUnitEntity): SyncProductUnitModel {
        return SyncProductUnitModel(
            id = from.Id,
            barcode = from.Barcode,
            displayOrder = from.DisplayOrder?.toInt(),
            isVisible = from.IsVisible?.let { it != 0L } ?: false,
            maxOrderQuantity = from.MaxOrderQuantity?.toInt(),
            minimumOrderQuantity = from.MinimumOrderQuantity?.toInt(),
            modificationDateUtc = from.ModificationDateUtc,
            multiplier = from.Multiplier?.toInt(),
            orderQuantityFactor = from.OrderQuantityFactor?.toInt(),
            price = from.Price,
            priceId = from.PriceId?.toInt(),
            productId = from.ProductId,
            purchaseDamagedReturnPrice = from.PurchaseDamagedReturnPrice,
            purchasePrice = from.PurchasePrice,
            purchaseReturnPrice = from.PurchaseReturnPrice,
            recordDateUtc = from.RecordDateUtc,
            salesDamagedReturnPrice = from.SalesDamagedReturnPrice,
            salesReturnPrice = from.SalesReturnPrice,
            selected = from.Selected?.let { it != 0L } ?: false,
            state = from.State,
            tenantId = from.TenantId,
            unitId = from.UnitId?.toInt(),
            weight = from.Weight
        )
    }

    override fun fromDomain(domain: SyncProductUnitModel): SyncProductUnitEntity {
        return SyncProductUnitEntity(
            Id = domain.id,
            Barcode = domain.barcode,
            DisplayOrder = domain.displayOrder?.toLong(),
            IsVisible = domain.isVisible.let { if (it) 1L else 0L },
            MaxOrderQuantity = domain.maxOrderQuantity?.toLong(),
            MinimumOrderQuantity = domain.minimumOrderQuantity?.toLong(),
            ModificationDateUtc = domain.modificationDateUtc,
            Multiplier = domain.multiplier?.toLong(),
            OrderQuantityFactor = domain.orderQuantityFactor?.toLong(),
            Price = domain.price,
            PriceId = domain.priceId?.toLong(),
            ProductId = domain.productId,
            PurchaseDamagedReturnPrice = domain.purchaseDamagedReturnPrice,
            PurchasePrice = domain.purchasePrice,
            PurchaseReturnPrice = domain.purchaseReturnPrice,
            RecordDateUtc = domain.recordDateUtc,
            SalesDamagedReturnPrice = domain.salesDamagedReturnPrice,
            SalesReturnPrice = domain.salesReturnPrice,
            Selected = domain.selected.let{ if (it) 1L else 0L },
            State = domain.state,
            TenantId = domain.tenantId,
            UnitId = domain.unitId?.toLong(),
            Weight = domain.weight
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion


}