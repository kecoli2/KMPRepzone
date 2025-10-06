package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.ProductParameterv4Entity
import com.repzone.domain.model.ProductParameterv4Model

class ProductParameterv4EntityDbMapper : Mapper<ProductParameterv4Entity, ProductParameterv4Model> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: ProductParameterv4Entity): ProductParameterv4Model {
        return ProductParameterv4Model(
            id = from.Id,
            color = from.Color,
            ediCode = from.EdiCode,
            isVisible = from.IsVisible,
            modificationDateUtc = from.ModificationDateUtc,
            order = from.Order,
            organizationId = from.OrganizationId,
            productId = from.ProductId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: ProductParameterv4Model): ProductParameterv4Entity {
        return ProductParameterv4Entity(
            Id = domain.id,
            Color = domain.color,
            EdiCode = domain.ediCode,
            IsVisible = domain.isVisible,
            ModificationDateUtc = domain.modificationDateUtc,
            Order = domain.order,
            OrganizationId = domain.organizationId,
            ProductId = domain.productId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
