package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncManufacturerParameterEntity
import com.repzone.domain.model.SyncManufacturerParameterModel

class SyncManufacturerParameterEntityDbMapper : Mapper<SyncManufacturerParameterEntity, SyncManufacturerParameterModel> {
    //region Public Method
    override fun toDomain(from: SyncManufacturerParameterEntity): SyncManufacturerParameterModel {
        return SyncManufacturerParameterModel(
            id = from.Id,
            allowedUnitIds = from.AllowedUnitIds,
            manufacturerId = from.ManufacturerId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            tenantId = from.TenantId
        )
    }

    override fun fromDomain(domain: SyncManufacturerParameterModel): SyncManufacturerParameterEntity {
        return SyncManufacturerParameterEntity(
            Id = domain.id,
            AllowedUnitIds = domain.allowedUnitIds,
            ManufacturerId = domain.manufacturerId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            TenantId = domain.tenantId
        )
    }
    //endregion

}
