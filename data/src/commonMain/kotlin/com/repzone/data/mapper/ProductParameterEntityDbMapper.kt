package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.ProductParameterEntity
import com.repzone.domain.model.ProductParameterModel

class ProductParameterEntityDbMapper: Mapper<ProductParameterEntity, ProductParameterModel> {
    //region Public Method
    override fun toDomain(from: ProductParameterEntity): ProductParameterModel {
        return ProductParameterModel(
            color = from.Color,
            id = from.Id,
            isVisible = from.IsVisible?.let { it != 0L } ?: false,
            order = from.Order,
            organizationId = from.OrganizationId,
            productId = from.ProductId
        )
    }

    override fun fromDomain(domain: ProductParameterModel): ProductParameterEntity {
        return ProductParameterEntity(
            Color = domain.color,
            Id = domain.id,
            IsVisible = domain.isVisible?.let { if (it) 1L else 0L },
            Order = domain.order,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId)
    }
    //endregion

}