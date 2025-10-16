package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncWarehouseEntity
import com.repzone.domain.model.SyncWarehouseModel

class SyncWarehouseEntityDbMapper : Mapper<SyncWarehouseEntity, SyncWarehouseModel> {
    //region Public Method
    override fun toDomain(from: SyncWarehouseEntity): SyncWarehouseModel {
        return SyncWarehouseModel(
            id = from.Id,
            mobileCloseToDamagedReturns = from.MobileCloseToDamagedReturns,
            mobileCloseToReturns = from.MobileCloseToReturns,
            mobileCloseToSales = from.MobileCloseToSales,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            organizationId = from.OrganizationId,
            organizationName = from.OrganizationName,
            recordDateUtc = from.RecordDateUtc,
            state = from.State
        )
    }

    override fun fromDomain(domain: SyncWarehouseModel): SyncWarehouseEntity {
        return SyncWarehouseEntity(
            Id = domain.id,
            MobileCloseToDamagedReturns = domain.mobileCloseToDamagedReturns,
            MobileCloseToReturns = domain.mobileCloseToReturns,
            MobileCloseToSales = domain.mobileCloseToSales,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            OrganizationId = domain.organizationId,
            OrganizationName = domain.organizationName,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state
        )
    }
    //endregion

}
