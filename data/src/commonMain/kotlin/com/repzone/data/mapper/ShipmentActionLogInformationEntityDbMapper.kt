package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.ShipmentActionLogInformationEntity
import com.repzone.domain.model.ShipmentActionLogInformationModel

class ShipmentActionLogInformationEntityDbMapper : Mapper<ShipmentActionLogInformationEntity, ShipmentActionLogInformationModel> {
    //region Public Method
    override fun toDomain(from: ShipmentActionLogInformationEntity): ShipmentActionLogInformationModel {
        return ShipmentActionLogInformationModel(
            id = from.Id,
            actionDate = from.ActionDate,
            actionType = from.ActionType,
            description = from.Description,
            reasonId = from.ReasonId,
            shippingDocumentId = from.ShippingDocumentId,
            shippingPlanId = from.ShippingPlanId
        )
    }

    override fun fromDomain(domain: ShipmentActionLogInformationModel): ShipmentActionLogInformationEntity {
        return ShipmentActionLogInformationEntity(
            Id = domain.id,
            ActionDate = domain.actionDate,
            ActionType = domain.actionType,
            Description = domain.description,
            ReasonId = domain.reasonId,
            ShippingDocumentId = domain.shippingDocumentId,
            ShippingPlanId = domain.shippingPlanId
        )
    }
    //endregion

}
