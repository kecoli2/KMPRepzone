package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.CollectionLogInformationEntity
import com.repzone.domain.model.CollectionLogInformationModel

class CollectionLogInformationEntityDbMapper : Mapper<CollectionLogInformationEntity, CollectionLogInformationModel> {
    //region Public Method
    override fun toDomain(from: CollectionLogInformationEntity): CollectionLogInformationModel {
        return CollectionLogInformationModel(
            id = from.Id,
            collectionDate = from.CollectionDate,
            customerId = from.CustomerId,
            documentUniqueId = from.DocumentUniqueId,
            note = from.Note,
            restServiceTaskId = from.RestServiceTaskId,
            sumTotal = from.SumTotal
        )
    }

    override fun fromDomain(domain: CollectionLogInformationModel): CollectionLogInformationEntity {
        return CollectionLogInformationEntity(
            Id = domain.id,
            CollectionDate = domain.collectionDate,
            CustomerId = domain.customerId,
            DocumentUniqueId = domain.documentUniqueId,
            Note = domain.note,
            RestServiceTaskId = domain.restServiceTaskId,
            SumTotal = domain.sumTotal
        )
    }
    //endregion

}
