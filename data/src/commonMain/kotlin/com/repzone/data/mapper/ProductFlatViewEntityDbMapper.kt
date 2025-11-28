package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.ProductFlatViewEntity
import com.repzone.domain.document.model.ProductInformationModel


class ProductFlatViewEntityDbMapper : Mapper<ProductFlatViewEntity, ProductInformationModel> {
    //region Public Method
    override fun toDomain(from: ProductFlatViewEntity): ProductInformationModel {
        TODO("Not yet implemented")
    }

    override fun fromDomain(domain: ProductInformationModel): ProductFlatViewEntity {
        TODO("Not yet implemented")
    }
    //endregion
}
