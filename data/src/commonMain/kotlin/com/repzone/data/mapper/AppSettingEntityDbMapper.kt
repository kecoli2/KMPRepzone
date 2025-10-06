package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.AppSettingEntity
import com.repzone.domain.model.AppSettingModel

class AppSettingEntityDbMapper : Mapper<AppSettingEntity, AppSettingModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: AppSettingEntity): AppSettingModel {
        return AppSettingModel(
            key = from.Key,
            value = from.Value,
            value2 = from.Value2
        )
    }

    override fun fromDomain(domain: AppSettingModel): AppSettingEntity {
        return AppSettingEntity(
            Key = domain.key,
            Value = domain.value,
            Value2 = domain.value2
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
