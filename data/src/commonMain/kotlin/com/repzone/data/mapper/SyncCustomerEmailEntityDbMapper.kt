package com.repzone.data.mapper

import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerEmailEntity
import com.repzone.domain.model.SyncCustomerEmailModel

class SyncCustomerEmailEntityDbMapper : Mapper<SyncCustomerEmailEntity, SyncCustomerEmailModel> {
    //region Field
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    override fun toDomain(from: SyncCustomerEmailEntity): SyncCustomerEmailModel {
        return SyncCustomerEmailModel(
            id = from.Id,
            companyName = from.CompanyName,
            customerId = from.CustomerId,
            email = from.Email,
            modificationDateUtc = from.ModificationDateUtc,
            name = from.Name,
            recordDateUtc = from.RecordDateUtc,
            state = from.State,
            title = from.Title
        )
    }

    override fun fromDomain(domain: SyncCustomerEmailModel): SyncCustomerEmailEntity {
        return SyncCustomerEmailEntity(
            Id = domain.id,
            CompanyName = domain.companyName,
            CustomerId = domain.customerId,
            Email = domain.email,
            ModificationDateUtc = domain.modificationDateUtc,
            Name = domain.name,
            RecordDateUtc = domain.recordDateUtc,
            State = domain.state,
            Title = domain.title
        )
    }
    //endregion

    //region Protected Method
    //endregion

    //region Private Method
    //endregion
}
