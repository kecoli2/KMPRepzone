package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncManufacturersEntity
import com.repzone.domain.model.SyncManufacturersModel

class SyncManufacturersEntityDbMapper : Mapper<SyncManufacturersEntity, SyncManufacturersModel> {
    //region Public Method
    override fun toDomain(from: SyncManufacturersEntity): SyncManufacturersModel {
        return SyncManufacturersModel(
            id = from.Id,
            customerId = from.CustomerId,
            docNote = from.DocNote,
            fulfillment = from.Fulfillment,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncManufacturersModel): SyncManufacturersEntity {
        return SyncManufacturersEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            DocNote = domain.docNote,
            Fulfillment = domain.fulfillment,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
