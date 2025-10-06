package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.PendingReportEntity
import com.repzone.domain.model.PendingReportModel

class PendingReportEntityDbMapper : Mapper<PendingReportEntity, PendingReportModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: PendingReportEntity): PendingReportModel {
        return PendingReportModel(
            productId = from.ProductId,
            pendingAmount = from.PendingAmount
        )
    }

    override fun fromDomain(domain: PendingReportModel): PendingReportEntity {
        return PendingReportEntity(
            ProductId = domain.productId,
            PendingAmount = domain.pendingAmount
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
