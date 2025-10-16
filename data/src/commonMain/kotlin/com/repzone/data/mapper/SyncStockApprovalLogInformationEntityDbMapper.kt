package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncStockApprovalLogInformationEntity
import com.repzone.domain.model.SyncStockApprovalLogInformationModel

class SyncStockApprovalLogInformationEntityDbMapper : Mapper<SyncStockApprovalLogInformationEntity, SyncStockApprovalLogInformationModel> {
    //region Public Method
    override fun toDomain(from: SyncStockApprovalLogInformationEntity): SyncStockApprovalLogInformationModel {
        return SyncStockApprovalLogInformationModel(
            id = from.Id,
            approValDate = from.ApprovalDate,
            data = from.Data,
            referenceDocUniqueId = from.ReferenceDocUniqueId,
            totalProductKind = from.TotalProductKind,
            totalProductQuantity = from.TotalProductQuantity
        )
    }

    override fun fromDomain(domain: SyncStockApprovalLogInformationModel): SyncStockApprovalLogInformationEntity {
        return SyncStockApprovalLogInformationEntity(
            Id = domain.id,
            ApprovalDate = domain.approValDate,
            Data = domain.data,
            ReferenceDocUniqueId = domain.referenceDocUniqueId,
            TotalProductKind = domain.totalProductKind,
            TotalProductQuantity = domain.totalProductQuantity
        )
    }
    //endregion

}
