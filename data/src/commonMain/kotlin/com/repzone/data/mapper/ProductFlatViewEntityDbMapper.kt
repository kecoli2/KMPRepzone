package com.repzone.data.mapper

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.repzone.core.util.extensions.toBoolean
import com.repzone.data.util.Mapper
import com.repzone.database.ProductFlatViewEntity
import com.repzone.domain.document.model.ProductInformationModel
import com.repzone.domain.document.model.ProductUnit


class ProductFlatViewEntityDbMapper : Mapper<ProductFlatViewEntity, ProductInformationModel> {
    //region Public Method
    override fun toDomain(from: ProductFlatViewEntity): ProductInformationModel {
        TODO("Not yet implemented")
    }

    override fun fromDomain(domain: ProductInformationModel): ProductFlatViewEntity {
        TODO("Not yet implemented")
    }

    fun toDomain(from: ProductFlatViewEntity, unitList: List<ProductUnit>): ProductInformationModel {
        return ProductInformationModel(
            id = from.ProductId.toInt(),
            name = from.ProductName ?: "",
            sku = from.Sku ?: "",
            vat = BigDecimal.fromLong(from.Vat),
            tags = from.Tags?.split(",") ?: emptyList(),
            brandId = from.BrandId.toInt(),
            brandName = from.BrandName,
            groupId = from.GroupId.toInt(),
            groupName = from.GroupName ?: "",
            stock = from.Stock.toBigDecimal(),
            orderStock = from.OrderStock.toBigDecimal(),
            vanStock = from.VanStock.toBigDecimal(),
            transitStock = from.TransitStock.toBigDecimal(),
            photoPath = from.PhotoPath,
            defaultUnitMultiplier = from.Multiplier.toBigDecimal(),
            defaultUnitName = from.UnitName ?: "",
            defaultUnitWeight = from.Weight?.toBigDecimal(),
            units = unitList,
            color = from.Color ?: "",
            brandPhotoPath = from.BrandPhotoPath,
            groupPhotoPath = from.GroupPhotoPath,
            displayOrder = from.DisplayOrder.toInt(),
            description = from.Description ?: "",
            pendingStock = from.PendingStock.toBigDecimal(),
            reservedStock = from.ReservedStock.toBigDecimal(),
            showAvailableStock = from.ShowAvailableStock.toBoolean(),
            showTransitStock = from.ShowTransitStock.toBoolean(),
            manufacturerId = from.ManufacturerId?.toInt()
        )
    }
    //endregion
}
