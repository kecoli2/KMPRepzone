package com.repzone.data.mapper

import com.repzone.core.enums.StateType
import com.repzone.core.util.extensions.enumToLong
import com.repzone.core.util.extensions.toEnum
import com.repzone.data.util.Mapper
import com.repzone.database.SyncCustomerOrganizationScopeEntity
import com.repzone.domain.model.SyncCustomerOrganizationScopeModel

class SyncCustomerOrganizationScopeEntityDbMapper : Mapper<SyncCustomerOrganizationScopeEntity, SyncCustomerOrganizationScopeModel> {
    //region Public Method
    override fun toDomain(from: SyncCustomerOrganizationScopeEntity): SyncCustomerOrganizationScopeModel {
        return SyncCustomerOrganizationScopeModel(
            id = from.Id,
            customerId = from.CustomerId,
            fulfillment = from.Fulfillment,
            manufacturerId = from.ManufacturerId,
            modificationDateUtc = from.ModificationDateUtc,
            organizationId = from.OrganizationId,
            organizationName = from.OrganizationName,
            recordDateUtc = from.RecordDateUtc,
            salesNotAllowed = from.SalesNotAllowed,
            state = from.State?.toEnum<StateType>() ?: StateType.ACTIVE
        )
    }

    override fun fromDomain(domain: SyncCustomerOrganizationScopeModel): SyncCustomerOrganizationScopeEntity {
        return SyncCustomerOrganizationScopeEntity(
            Id = domain.id,
            CustomerId = domain.customerId,
            Fulfillment = domain.fulfillment,
            ManufacturerId = domain.manufacturerId,
            ModificationDateUtc = domain.modificationDateUtc,
            OrganizationId = domain.organizationId,
            OrganizationName = domain.organizationName,
            RecordDateUtc = domain.recordDateUtc,
            SalesNotAllowed = domain.salesNotAllowed,
            State = domain.state.enumToLong()
        )
    }
    //endregion

}
