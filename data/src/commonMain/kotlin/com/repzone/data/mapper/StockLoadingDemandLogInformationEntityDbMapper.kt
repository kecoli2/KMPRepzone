package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.StockLoadingDemandLogInformationEntity
import com.repzone.domain.model.StockLoadingDemandLogInformationModel

class StockLoadingDemandLogInformationEntityDbMapper : Mapper<StockLoadingDemandLogInformationEntity, StockLoadingDemandLogInformationModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: StockLoadingDemandLogInformationEntity): StockLoadingDemandLogInformationModel {
        return StockLoadingDemandLogInformationModel(
            id = from.Id,
            recordDate = from.RecordDate,
            restServiceTaskId = from.RestServiceTaskId,
            totalProductKind = from.TotalProductKind,
            totalProductQuantity = from.TotalProductQuantity
        )
    }

    override fun fromDomain(domain: StockLoadingDemandLogInformationModel): StockLoadingDemandLogInformationEntity {
        return StockLoadingDemandLogInformationEntity(
            Id = domain.id,
            RecordDate = domain.recordDate,
            RestServiceTaskId = domain.restServiceTaskId,
            TotalProductKind = domain.totalProductKind,
            TotalProductQuantity = domain.totalProductQuantity
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
