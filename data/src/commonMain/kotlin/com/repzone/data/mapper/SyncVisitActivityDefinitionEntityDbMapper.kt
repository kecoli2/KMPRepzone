package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncVisitActivityDefinitionEntity
import com.repzone.domain.model.SyncVisitActivityDefinitionModel

class SyncVisitActivityDefinitionEntityDbMapper : Mapper<SyncVisitActivityDefinitionEntity, SyncVisitActivityDefinitionModel> {
    //region Public Method
    override fun toDomain(from: SyncVisitActivityDefinitionEntity): SyncVisitActivityDefinitionModel {
        return SyncVisitActivityDefinitionModel(
            id = from.Id,
            customerTags = from.CustomerTags,
            descriptions = from.Descriptions,
            formTags = from.FormTags,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            order = from.Order,
            recordDateUtc = from.RecordDateUtc,
            representativeTags = from.RepresentativeTags,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncVisitActivityDefinitionModel): SyncVisitActivityDefinitionEntity {
        return SyncVisitActivityDefinitionEntity(
            Id = domain.id,
            CustomerTags = domain.customerTags,
            Descriptions = domain.descriptions,
            FormTags = domain.formTags,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            Order = domain.order,
            RecordDateUtc = domain.recordDateUtc,
            RepresentativeTags = domain.representativeTags,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
