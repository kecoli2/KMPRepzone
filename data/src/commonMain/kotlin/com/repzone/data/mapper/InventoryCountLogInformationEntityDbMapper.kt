package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.InventoryCountLogInformationEntity
import com.repzone.domain.model.InventoryCountLogInformationModel

class InventoryCountLogInformationEntityDbMapper : Mapper<InventoryCountLogInformationEntity, InventoryCountLogInformationModel> {
    //region Public Method
    override fun toDomain(from: InventoryCountLogInformationEntity): InventoryCountLogInformationModel {
        return InventoryCountLogInformationModel(
            id = from.Id,
            approValDate = from.ApprovalDate,
            data = from.Data,
            direction = from.Direction,
            totalProductKind = from.TotalProductKind,
            totalProductQuantity = from.TotalProductQuantity
        )
    }

    override fun fromDomain(domain: InventoryCountLogInformationModel): InventoryCountLogInformationEntity {
        return InventoryCountLogInformationEntity(
            Id = domain.id,
            ApprovalDate = domain.approValDate,
            Data = domain.data,
            Direction = domain.direction,
            TotalProductKind = domain.totalProductKind,
            TotalProductQuantity = domain.totalProductQuantity
        )
    }
    //endregion

}
