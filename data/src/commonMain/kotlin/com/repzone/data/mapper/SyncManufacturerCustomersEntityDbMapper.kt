package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncManufacturerCustomersEntity
import com.repzone.domain.model.SyncManufacturerCustomersModel

class SyncManufacturerCustomersEntityDbMapper : Mapper<SyncManufacturerCustomersEntity, SyncManufacturerCustomersModel> {
    //region Public Method
    override fun toDomain(from: SyncManufacturerCustomersEntity): SyncManufacturerCustomersModel {
        return SyncManufacturerCustomersModel(
            id = from.Id,
            customerId = from.CustomerId,
            fulfillment = from.Fulfillment,
            manufacturerId = from.ManufacturerId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            salesNotAllowed = from.SalesNotAllowed,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncManufacturerCustomersModel): SyncManufacturerCustomersEntity {
        return SyncManufacturerCustomersEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            Fulfillment = domain.fulfillment,
            ManufacturerId = domain.manufacturerId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            SalesNotAllowed = domain.salesNotAllowed,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
