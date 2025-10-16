package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncManufacturerRepresentativesEntity
import com.repzone.domain.model.SyncManufacturerRepresentativesModel

class SyncManufacturerRepresentativesEntityDbMapper : Mapper<SyncManufacturerRepresentativesEntity, SyncManufacturerRepresentativesModel> {
    //region Public Method
    override fun toDomain(from: SyncManufacturerRepresentativesEntity): SyncManufacturerRepresentativesModel {
        return SyncManufacturerRepresentativesModel(
            id = from.Id,
            fulfillment = from.Fulfillment,
            manufacturerId = from.ManufacturerId,
            modificationDateUtc = from.ModificationDateUtc,
            recordDateUtc = from.RecordDateUtc,
            representativeId = from.RepresentativeId,
            salesNotAllowed = from.SalesNotAllowed,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncManufacturerRepresentativesModel): SyncManufacturerRepresentativesEntity {
        return SyncManufacturerRepresentativesEntity(
            Id = domain.id,
            Fulfillment = domain.fulfillment,
            ManufacturerId = domain.manufacturerId,
            ModificationDateUtc = domain.modificationDateUtc,
            RecordDateUtc = domain.recordDateUtc,
            RepresentativeId = domain.representativeId,
            SalesNotAllowed = domain.salesNotAllowed,
            State = domain.state
        )
    }
    //endregion

}
