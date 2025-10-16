package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.CustomDataHeaderEntity
import com.repzone.domain.model.CustomDataHeaderModel

class CustomDataHeaderEntityDbMapper : Mapper<CustomDataHeaderEntity, CustomDataHeaderModel> {
    //region Public Method
    override fun toDomain(from: CustomDataHeaderEntity): CustomDataHeaderModel {
        return CustomDataHeaderModel(
            id = from.Id,
            name = from.Name
        )
    }

    override fun fromDomain(domain: CustomDataHeaderModel): CustomDataHeaderEntity {
        return CustomDataHeaderEntity(
            Id = domain.id,
            Name = domain.name
        )
    }
    //endregion

}
