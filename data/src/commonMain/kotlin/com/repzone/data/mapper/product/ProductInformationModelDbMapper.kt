package com.repzone.data.mapper.product

import com.repzone.data.util.Mapper
import com.repzone.database.ProductFlatViewEntity
import com.repzone.domain.document.model.ProductInformationModel

class ProductInformationModelDbMapper: Mapper<ProductFlatViewEntity, ProductInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Public Method
    override fun toDomain(from: ProductFlatViewEntity): ProductInformationModel {
        TODO("Not yet implemented")
    }

    override fun fromDomain(domain: ProductInformationModel): ProductFlatViewEntity {
        TODO("Not yet implemented")
    }
    //endregion


}