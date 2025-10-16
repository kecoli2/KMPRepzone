package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.CustomDataDetailEntity
import com.repzone.domain.model.CustomDataDetailModel

class CustomDataDetailEntityDbMapper : Mapper<CustomDataDetailEntity, CustomDataDetailModel> {
    //region Public Method
    override fun toDomain(from: CustomDataDetailEntity): CustomDataDetailModel {
        return CustomDataDetailModel(
            id = from.Id,
            headerId = from.HeaderId,
            value = from.Value
        )
    }

    override fun fromDomain(domain: CustomDataDetailModel): CustomDataDetailEntity {
        return CustomDataDetailEntity(
            Id = domain.id,
            HeaderId = domain.headerId,
            Value = domain.value
        )
    }
    //endregion

}
