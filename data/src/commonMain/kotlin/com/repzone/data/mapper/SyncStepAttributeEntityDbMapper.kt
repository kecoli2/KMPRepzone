package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncStepAttributeEntity
import com.repzone.domain.model.SyncStepAttributeModel

class SyncStepAttributeEntityDbMapper : Mapper<SyncStepAttributeEntity, SyncStepAttributeModel> {
    //region Public Method
    override fun toDomain(from: SyncStepAttributeEntity): SyncStepAttributeModel {
        return SyncStepAttributeModel(
            alias = from.Alias,
            description = from.Description,
            id = from.Id,
            key = from.Key,
            taskStepId = from.TaskStepId,
            value = from.Value,
            valueType = from.ValueType
        )
    }

    override fun fromDomain(domain: SyncStepAttributeModel): SyncStepAttributeEntity {
        return SyncStepAttributeEntity(
            Alias = domain.alias,
            Description = domain.description,
            Id = domain.id,
            Key = domain.key,
            TaskStepId = domain.taskStepId,
            Value = domain.value,
            ValueType = domain.valueType
        )
    }
    //endregion

}
